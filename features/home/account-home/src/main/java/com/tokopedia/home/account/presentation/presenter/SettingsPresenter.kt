package com.tokopedia.home.account.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.preference.PreferenceManager
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.design.dialog.IAccessRequestListener
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.SetUserProfileSettingResponse
import com.tokopedia.home.account.data.model.UserProfileDobResponse
import com.tokopedia.home.account.data.model.UserProfileSettingResponse
import com.tokopedia.home.account.presentation.listener.SettingOptionsView
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class SettingsPresenter(var context: Context?) : BaseDaggerPresenter<SettingOptionsView>(), CoroutineScope {
    private lateinit var userSession: UserSessionInterface
    private val savedSettingValue: Boolean
        get() {
            val settings = PreferenceManager.getDefaultSharedPreferences(context)
            return settings.getBoolean(context?.getString((R.string.pref_safe_mode)), false)
        }

    var adultAgeVerified: Boolean = false

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun verifyUserAge() {
        if (getUserLoginState().isLoggedIn)
            fetchUserDOB()
    }

    private fun fetchUserDOB() {
        launchCatchError(block = {
            val requestParams = java.util.HashMap<String, Any>()
            val query = GraphqlHelper.loadRawString(context?.resources, R.raw.query_user_age)
            val userProfileDobResponse = getGQLData(query, UserProfileDobResponse::class.java, requestParams) as UserProfileDobResponse
            processUserDOB(userProfileDobResponse)
            if (adultAgeVerified) {
                view.refreshSettingOptionsList()
                getAndSaveSafeModeValue()
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun processUserDOB(userProfileDobResponse: UserProfileDobResponse) {
        userProfileDobResponse.userProfileDob.let {
            if (it.isDobVerified) {
                if (it.isAdult || it.age >= 21)
                    adultAgeVerified = true
            }
        }
    }

    fun getAndSaveSafeModeValue() {
        launchCatchError(block = {
            val requestParams = java.util.HashMap<String, Any>()
            val query = GraphqlHelper.loadRawString(context?.resources, R.raw.query_user_safe_mode)
            val userProfileSettingResponse = getGQLData(query, UserProfileSettingResponse::class.java, requestParams) as UserProfileSettingResponse
            saveSettingValue(context?.getString(R.string.pref_safe_mode)!!, userProfileSettingResponse.userProfileSetting.safeMode)
            view.refreshSafeSearchOption()
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun getUserLoginState(): UserSessionInterface {
        if (!(::userSession.isInitialized)) {
            userSession = NetworkClient.getsUserSession()
        }
        return userSession
    }

    fun onClickAcceptButton() {
        val savedValue = !savedSettingValue
        launchCatchError(block = {
            val requestParams = java.util.HashMap<String, Any>()
            requestParams[PARAM_SAFE_MODE] = savedValue
            val query = GraphqlHelper.loadRawString(context?.resources, R.raw.mutation_user_safe_mode)
            val userProfileSettingResponse = getGQLData(query, SetUserProfileSettingResponse::class.java, requestParams) as SetUserProfileSettingResponse
            if (userProfileSettingResponse.userProfileSettingUpdate.isSuccess) {
                saveSettingValue(context?.getString(R.string.pref_safe_mode)!!, savedValue)
                view.refreshSafeSearchOption()
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun saveSettingValue(key: String, isChecked: Boolean) {
        val settings = PreferenceManager.getDefaultSharedPreferences(context!!)
        val editor = settings.edit()
        editor.putBoolean(key, isChecked)
        editor.apply()
    }

    suspend fun <T : Any> getGQLData(gqlQuery: String,
                                     gqlResponseType: Class<T>,
                                     gqlParams: Map<String, Any>): Any? {
        try {
            val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
            val gqlUseCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<T>(graphqlRepository)
            gqlUseCase.setTypeClass(gqlResponseType)
            gqlUseCase.setGraphqlQuery(gqlQuery)
            gqlUseCase.setRequestParams(gqlParams)
            gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
            return gqlUseCase.executeOnBackground()
        } catch (t: Throwable) {
            throw t
        }
    }

    companion object {
        private const val PARAM_SAFE_MODE = "safeMode"
    }

}