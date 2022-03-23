package com.tokopedia.additional_check.view

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.additional_check.data.OfferingData
import com.tokopedia.additional_check.data.ShowInterruptData
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.domain.usecase.OfferInterruptUseCase
import com.tokopedia.additional_check.domain.usecase.ShowInterruptUseCase
import com.tokopedia.additional_check.internal.AdditionalCheckConstants
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class TwoFactorViewModel @Inject constructor (@Named(SessionModule.SESSION_MODULE)
                                              private val userSession: UserSessionInterface,
                                              private val additionalCheckPreference: AdditionalCheckPreference,
                                              private val showInterruptUseCase: ShowInterruptUseCase,
                                              private val offerInterruptUseCase: OfferInterruptUseCase,
                                              dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    fun getOffering(isSupportBiometric: Boolean, onSuccess: (MutableList<OfferingData>) -> Unit, onError: (Throwable) -> Unit) {
        if(userSession.isLoggedIn && additionalCheckPreference.isNeedCheck()) {
            launch {
                try {
                    val offering = offerInterruptUseCase(mapOf(
                        OfferInterruptUseCase.PARAM_SUPPORT_BIOMETRIC to isSupportBiometric
                    ))
                    additionalCheckPreference.setInterval(offering.data.interval)
                    if (offering.data.errorMessages.isNotEmpty() && offering.data.errorMessages.first().isEmpty()) {
                        if(offering.data.offers.size > 1) {
                            val firstOffering = offering.data.offers[1]
                            val jsonString = Gson().toJson(firstOffering)
                            additionalCheckPreference.setNextOffering(jsonString)
                        }
                        onSuccess(offering.data.offers)
                    } else {
                        onError(Throwable(offering.data.errorMessages.first()))
                    }
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }
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