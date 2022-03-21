package com.tokopedia.additional_check.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.additional_check.data.ShowInterruptData
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.domain.usecase.ShowInterruptUseCase
import com.tokopedia.additional_check.internal.AdditionalCheckConstants
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class TwoFactorViewModel @Inject constructor (@Named(SessionModule.SESSION_MODULE)
                                              private val userSession: UserSessionInterface,
                                              private val additionalCheckPreference: AdditionalCheckPreference,
                                              private val showInterruptUseCase: ShowInterruptUseCase,
                                              dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    fun check(onSuccess: (ShowInterruptData) -> Unit, onError: (Throwable) -> Unit) {
        if(additionalCheckPreference.isNeedCheck() && userSession.isLoggedIn) {
            launchCatchError(block = {
                val result = showInterruptUseCase(ShowInterruptUseCase.MODULE_ACCOUNT_LINKING).data
                if(result.popupType == AdditionalCheckConstants.POPUP_TYPE_NONE) {
                    additionalCheckPreference.setInterval(result.accountLinkReminderData.interval)
                } else {
                    additionalCheckPreference.setInterval(result.interval)
                }
                onSuccess(result)
            }, onError = {
                onError(it)
            })
        }
    }
}