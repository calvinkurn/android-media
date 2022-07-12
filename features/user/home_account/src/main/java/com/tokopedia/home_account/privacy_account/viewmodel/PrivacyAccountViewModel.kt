package com.tokopedia.home_account.privacy_account.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.privacy_account.data.DataSetConsent
import com.tokopedia.home_account.privacy_account.data.LinkStatusResponse
import com.tokopedia.home_account.privacy_account.domain.GetConsentUseCase
import com.tokopedia.home_account.privacy_account.domain.GetLinkStatusUseCase
import com.tokopedia.home_account.privacy_account.domain.GetUserProfile
import com.tokopedia.home_account.privacy_account.domain.SetConsentUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yoris on 04/08/21.
 */

class PrivacyAccountViewModel @Inject constructor(
    private val getLinkStatusUseCase: GetLinkStatusUseCase,
    private val getProfileUseCase: GetUserProfile,
    private val getConsentUseCase: GetConsentUseCase,
    private val setConsentUseCase: SetConsentUseCase,
    private val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main), LifecycleObserver {

    private val _linkStatus = MutableLiveData<Result<LinkStatusResponse>>()
    val linkStatus: LiveData<Result<LinkStatusResponse>> get() = _linkStatus

    private val _getUserConsent = MutableLiveData<Result<Boolean>>()
    val getUserConsent: LiveData<Result<Boolean>> get() = _getUserConsent

    private val _setUserConsent = MutableLiveData<Result<DataSetConsent>>()
    val setUserConsent: LiveData<Result<DataSetConsent>> get() = _setUserConsent

    fun getLinkStatus(isGetProfile: Boolean = false) {
        launchCatchError(block = {
            val result = getLinkStatusUseCase(GetLinkStatusUseCase.ACCOUNT_LINKING_TYPE)
            if(isGetProfile) {
                val profile = getProfileUseCase(Unit)
                val phone = profile.profileInfo.phone
                if(phone.isNotEmpty()) {
                    userSession.phoneNumber = phone
                    result.response.linkStatus.forEach { it.phoneNo = phone }
                }
            }

            _linkStatus.value = Success(result)
        }, onError = {
            _linkStatus.value = Fail(it)
        })
    }

    fun getConsentSocialNetwork() {
        launchCatchError(coroutineContext, {
            val response = getConsentUseCase(Unit)
            _getUserConsent.value = Success(response.socialNetworkGetConsent.data.optIn)
        }) {
            _getUserConsent.value = Fail(it)
        }
    }

    fun setConsentSocialNetwork(consentValue: Boolean) {
        launchCatchError(coroutineContext, {
            val response = setConsentUseCase(consentValue)
            _setUserConsent.value = Success(response.socialNetworkSetConsent.data)
        }) {
            _setUserConsent.value = Fail(it)
        }
    }
}