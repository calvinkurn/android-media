package com.tokopedia.privacycenter.ui.main.section.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.domain.DevicePermissionUseCase
import com.tokopedia.privacycenter.domain.GetRecommendationFriendState
import com.tokopedia.privacycenter.domain.SocialNetworkGetConsentUseCase
import com.tokopedia.privacycenter.domain.SocialNetworkSetConsentUseCase
import javax.inject.Inject

class RecommendationViewModel @Inject constructor(
    private val devicePermissionUseCase: DevicePermissionUseCase,
    private val socialNetworkGetConsentUseCase: SocialNetworkGetConsentUseCase,
    private val socialNetworkSetConsentUseCase: SocialNetworkSetConsentUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _isShakeShakeAllowed = MutableLiveData<Boolean>()
    val isShakeShakeAllowed: LiveData<Boolean> get() = _isShakeShakeAllowed

    private val _isGeolocationAllowed = MutableLiveData<Boolean>()
    val isGeolocationAllowed: LiveData<Boolean> get() = _isGeolocationAllowed

    private val _getConsentSocialNetwork = MutableLiveData<GetRecommendationFriendState>()
    val getConsentSocialNetwork: LiveData<GetRecommendationFriendState>
        get() = _getConsentSocialNetwork

    private val _setConsentSocialNetwork = MutableLiveData<PrivacyCenterStateResult<Boolean>>()
    val setConsentSocialNetwork: LiveData<PrivacyCenterStateResult<Boolean>>
        get() = _setConsentSocialNetwork

    private val _isRecommendationFriendAllowed = MutableLiveData<Boolean>()
    val isRecommendationFriendAllowed: LiveData<Boolean> get() = _isRecommendationFriendAllowed

    init {
        getConsentSocialNetwork()
        isShakeShakeAllowed()
    }

    private fun isShakeShakeAllowed() {
        _isShakeShakeAllowed.value = devicePermissionUseCase.isShakeShakeAllowed()
    }

    fun setShakeShakePermission(isAllowed: Boolean) {
        devicePermissionUseCase.setShakeShakePermission(isAllowed)
    }

    private fun isGeolocationAllowed(): Boolean {
        return devicePermissionUseCase.isLocationAllowed()
    }

    fun setGeolocationChange(isAllowed: Boolean) {
        _isGeolocationAllowed.value = isAllowed
    }

    fun refreshGeolocationPermission() {
        setGeolocationChange(isGeolocationAllowed())
    }

    fun getConsentSocialNetwork() {
        _getConsentSocialNetwork.value = GetRecommendationFriendState.Loading()
        launchCatchError(coroutineContext, {
            val result = socialNetworkGetConsentUseCase(Unit)
            _getConsentSocialNetwork.value = result
            _isRecommendationFriendAllowed.value = result.isAllowed
        }, {
            _getConsentSocialNetwork.value = GetRecommendationFriendState.Failed(it)
        })
    }

    fun setConsentSocialNetwork(isAllowed: Boolean) {
        _setConsentSocialNetwork.value = PrivacyCenterStateResult.Loading()
        launchCatchError(coroutineContext, {
            _setConsentSocialNetwork.value = socialNetworkSetConsentUseCase(isAllowed)
            _isRecommendationFriendAllowed.value = isAllowed
        }, {
            _setConsentSocialNetwork.value = PrivacyCenterStateResult.Fail(it)
        })
    }
}
