package com.tokopedia.additional_check.view

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.additional_check.data.OfferingData
import com.tokopedia.additional_check.data.ShowInterruptData
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.domain.usecase.GetSimpleProfileUseCase
import com.tokopedia.additional_check.domain.usecase.OfferInterruptUseCase
import com.tokopedia.additional_check.domain.usecase.ShowInterruptUseCase
import com.tokopedia.additional_check.internal.AdditionalCheckConstants
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Named

class TwoFactorViewModel @Inject constructor (@Named(SessionModule.SESSION_MODULE)
                                              private val userSession: UserSessionInterface,
                                              private val additionalCheckPreference: AdditionalCheckPreference,
                                              private val showInterruptUseCase: ShowInterruptUseCase,
                                              private val offerInterruptUseCase: OfferInterruptUseCase,
                                              private val fingerprintPreference: FingerprintPreference,
                                              private val getSimpleProfileUseCase: GetSimpleProfileUseCase,
                                              private val aeadEncryptorImpl: AeadEncryptor,
                                              dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private var job: Job? = null

    fun getOffering(isSupportBiometric: Boolean, onSuccess: (MutableList<OfferingData>) -> Unit, onError: (Throwable) -> Unit) {
        if(userSession.isLoggedIn && additionalCheckPreference.isNeedCheck()) {
            launchCatchError(block = {
                val offering = offerInterruptUseCase(mapOf(
                    OfferInterruptUseCase.PARAM_SUPPORT_BIOMETRIC to isSupportBiometric,
                    OfferInterruptUseCase.PARAM_DEVICE_BIOMETRIC to fingerprintPreference.getUniqueId()
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
            }, onError = {
                onError(it)
            })
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

    fun refreshUserSession(onSuccess: (Boolean) -> Unit) {
        if(job == null) {
            job = launchCatchError(block = {
                val profile = getSimpleProfileUseCase(Unit).data

                // delete Android Keystore, expected
                aeadEncryptorImpl.delete()

                userSession.name = profile.fullName
                userSession.email = profile.email
                userSession.profilePicture = profile.profilePicture
                userSession.phoneNumber = profile.phone
                onSuccess.invoke(true)
                job = null
            }, onError = {
                onSuccess.invoke(false)
                job = null
            })
        }
    }
}