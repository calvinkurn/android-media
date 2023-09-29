package com.tokopedia.home_account.privacy_account.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.consentWithdrawal.data.ConsentGroupListDataModel
import com.tokopedia.home_account.consentWithdrawal.domain.GetConsentGroupListUseCase
import com.tokopedia.home_account.privacy_account.data.DataSetConsent
import com.tokopedia.home_account.privacy_account.domain.GetConsentSocialNetworkUseCase
import com.tokopedia.home_account.privacy_account.domain.SetConsentSocialNetworkUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Yoris on 04/08/21.
 */

class PrivacyAccountViewModel @Inject constructor(
    private val getConsentSocialNetworkUseCase: GetConsentSocialNetworkUseCase,
    private val setConsentSocialNetworkUseCase: SetConsentSocialNetworkUseCase,
    private val getConsentGroupListUseCase: GetConsentGroupListUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main), LifecycleObserver {

    private val _getConsentSocialNetwork = MutableLiveData<Result<Boolean>>()
    val getConsentSocialNetwork: LiveData<Result<Boolean>> get() = _getConsentSocialNetwork

    private val _setConsentSocialNetwork = MutableLiveData<Result<DataSetConsent>>()
    val setConsentSocialNetwork: LiveData<Result<DataSetConsent>> get() = _setConsentSocialNetwork

    private val _getConsentGroupList = MutableLiveData<Result<ConsentGroupListDataModel>>()
    val getConsentGroupList: LiveData<Result<ConsentGroupListDataModel>>
        get() = _getConsentGroupList

    init {
        getConsentSocialNetwork()
    }

    fun getConsentSocialNetwork() {
        launchCatchError(coroutineContext, {
            val response = getConsentSocialNetworkUseCase(Unit)
            _getConsentSocialNetwork.value = Success(response.socialNetworkGetConsent.data.optIn)
        }) {
            _getConsentSocialNetwork.value = Fail(it)
        }
    }

    fun setConsentSocialNetwork(consentValue: Boolean) {
        launchCatchError(coroutineContext, {
            val response = setConsentSocialNetworkUseCase(consentValue)
            _setConsentSocialNetwork.value = Success(response.socialNetworkSetConsent.data)
        }) {
            _setConsentSocialNetwork.value = Fail(it)
        }
    }

    fun getConsentGroupList() {
        launchCatchError(coroutineContext, {
            val response = getConsentGroupListUseCase(Unit)
            if (response.consentGroupList.success) {
                _getConsentGroupList.value = Success(response.consentGroupList)
            } else {
                _getConsentGroupList.value = Fail(MessageErrorException(response.consentGroupList.errorMessages.toString()))
            }
        }) {
            _getConsentGroupList.value = Fail(it)
        }
    }
}
