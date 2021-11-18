package com.tokopedia.additional_check.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.domain.usecase.AdditionalCheckUseCase
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class TwoFactorViewModel @Inject constructor (@Named(SessionModule.SESSION_MODULE)
                                              private val userSession: UserSessionInterface,
                                              private val additionalCheckPreference: AdditionalCheckPreference,
                                              private val additionalCheckUseCase: AdditionalCheckUseCase,
                                              dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {

    fun check(onSuccess: (TwoFactorResult) -> Unit, onError: (Throwable) -> Unit) {
        if(additionalCheckPreference.isNeedCheck() && userSession.isLoggedIn) {
            additionalCheckUseCase.getBottomSheetData(onSuccess = {
                additionalCheckPreference.setInterval(it.twoFactorResult.interval)
                onSuccess(it.twoFactorResult)
            }, onError = {
                onError(it)
            })
        }
    }
}