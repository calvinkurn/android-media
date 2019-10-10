package com.tokopedia.home.account.presentation.presenter

import android.content.Context
import android.preference.PreferenceManager
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.*
import com.tokopedia.home.account.domain.SetUserProfileSafeModeUseCase
import com.tokopedia.home.account.domain.UserProfileDobUseCase
import com.tokopedia.home.account.domain.UserProfileSafeModeUseCase
import com.tokopedia.home.account.presentation.listener.SettingOptionsView
import com.tokopedia.user.session.UserSessionInterface

class SettingsPresenter(var context: Context?,
                        var userProfileSafeModeUseCase: UserProfileSafeModeUseCase?,
                        var userProfileDobUseCase: UserProfileDobUseCase?,
                        var setUserProfileSafeModeUseCase: SetUserProfileSafeModeUseCase?) : BaseDaggerPresenter<SettingOptionsView>() {

    private lateinit var userSession: UserSessionInterface

    private val savedSafeModeValue: Boolean
        get() {
            val settings = PreferenceManager.getDefaultSharedPreferences(context)
            return settings.getBoolean(context?.getString((R.string.pref_safe_mode)), false)
        }

    var adultAgeVerified: Boolean = false

    fun verifyUserAge() {
        if (getUserLoginState().isLoggedIn)
            fetchUserDOB()
    }

    private fun fetchUserDOB() {
        userProfileDobUseCase?.executeQuerySafeMode(
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
                    adultAgeVerified = true
                    showSafeModeOption()
                }
            }
        }
    }

    private fun showSafeModeOption() {
        if (view != null)
            view.refreshSettingOptionsList()
        getAndSaveSafeModeValue()
    }

    private fun getAndSaveSafeModeValue() {
        userProfileSafeModeUseCase?.executeQuerySafeMode(
                { (profileSettingResponse) ->
                    saveSettingValue(context?.getString(R.string.pref_safe_mode)!!, profileSettingResponse.safeMode)
                    refreshSafeModeSwitch()
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    private fun refreshSafeModeSwitch() {
        if (view != null)
            view.refreshSafeSearchOption()
    }

    private fun getUserLoginState(): UserSessionInterface {
        if (!(::userSession.isInitialized)) {
            userSession = NetworkClient.getsUserSession()
        }
        return userSession
    }

    fun onClickAcceptButton() {
        val savedValue: Boolean = !savedSafeModeValue
        setUserProfileSafeModeUseCase?.executeQuerySetSafeMode(
                { (userProfileSettingUpdate) ->
                    if (userProfileSettingUpdate.isSuccess) {
                        saveSettingValue(context?.getString(R.string.pref_safe_mode)!!, savedValue)
                        refreshSafeModeSwitch()
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