package com.tokopedia.home.account.presentation.presenter

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.preference.PreferenceManager
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.*
import com.tokopedia.home.account.domain.SetUserProfileSafeModeUseCase
import com.tokopedia.home.account.domain.UserProfileDobUseCase
import com.tokopedia.home.account.domain.UserProfileSafeModeUseCase
import com.tokopedia.home.account.presentation.listener.SettingOptionsView
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class SettingsPresenter(var context: Context?) : BaseDaggerPresenter<SettingOptionsView>(), CoroutineScope {
    private lateinit var userSession: UserSessionInterface
    private var graphqlRepository: GraphqlRepository

    var adultAgeVerifiedLiveData: MutableLiveData<Boolean>? = MutableLiveData()
    var savedSafeModeValue: MutableLiveData<Boolean>? = MutableLiveData()

    init {
        adultAgeVerifiedLiveData?.postValue(false)
        adultAgeVerifiedLiveData?.observe(context as LifecycleOwner, Observer {
            view.refreshSettingOptionsList()
            if (it!!)
                getAndSaveSafeModeValue()
        })

        graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val savedSettingValue = settings.getBoolean(context?.getString((R.string.pref_safe_mode)), false)

        savedSafeModeValue?.postValue(savedSettingValue)
        savedSafeModeValue?.observe(context as LifecycleOwner, Observer {
            view.refreshSafeSearchOption()
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun verifyUserAge() {
        if (getUserLoginState().isLoggedIn)
            fetchUserDOB()
    }

    private fun fetchUserDOB() {
        val userProfileDobUseCase = UserProfileDobUseCase(context, graphqlRepository)
        userProfileDobUseCase.executeQuerySafeMode(
                { (profileDobResponse) ->
                    processUserDOB(profileDobResponse)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    private fun processUserDOB(profileDobResponse: UserProfileDob) {
        profileDobResponse.let {
            if (it.isDobVerified) {
                if (it.isAdult || it.age >= minimumAdultAge) {
                    adultAgeVerifiedLiveData?.postValue(true)
                }
            }
        }
    }

    fun getAndSaveSafeModeValue() {
        val userProfileSafeModeUseCase = UserProfileSafeModeUseCase(context, graphqlRepository)
        userProfileSafeModeUseCase.executeQuerySafeMode(
                { (profileSettingResponse) ->
                    saveSettingValue(context?.getString(R.string.pref_safe_mode)!!, profileSettingResponse.safeMode)
                    savedSafeModeValue?.postValue(profileSettingResponse.safeMode)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    private fun getUserLoginState(): UserSessionInterface {
        if (!(::userSession.isInitialized)) {
            userSession = NetworkClient.getsUserSession()
        }
        return userSession
    }

    fun onClickAcceptButton() {
        val savedValue: Boolean = !savedSafeModeValue?.value!!
        val setUserProfileSafeModeUseCase = SetUserProfileSafeModeUseCase(context, graphqlRepository)
        setUserProfileSafeModeUseCase.executeQuerySetSafeMode(
                { (userProfileSettingUpdate) ->
                    if (userProfileSettingUpdate.isSuccess) {
                        saveSettingValue(context?.getString(R.string.pref_safe_mode)!!, savedValue)
                        savedSafeModeValue?.postValue(savedValue)
                    }
                },
                { throwable ->
                    throwable.printStackTrace()
                }, savedValue)
    }

    private fun saveSettingValue(key: String, isChecked: Boolean) {
        val settings = PreferenceManager.getDefaultSharedPreferences(context!!)
        val editor = settings.edit()
        editor.putBoolean(key, isChecked)
        editor.apply()
    }

    companion object {
        private const val minimumAdultAge = 21
    }

}