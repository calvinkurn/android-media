package com.tokopedia.privacycenter.main.section.recommendation

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.common.domain.DevicePermissionUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class RecommendationViewModel @Inject constructor(
    private val devicePermissionUseCase: DevicePermissionUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _isShakeShakeAllowed = SingleLiveEvent<Boolean>()
    val isShakeShakeAllowed: SingleLiveEvent<Boolean> get() = _isShakeShakeAllowed

    private val _isGeolocationAllowed = SingleLiveEvent<Boolean>()
    val isGeolocationAllowed: SingleLiveEvent<Boolean> get() = _isGeolocationAllowed

    fun isShakeShakeAllowed() {
        _isShakeShakeAllowed.value = devicePermissionUseCase.isShakeShakeAllowed()
    }

    fun setShakeShakePermission(isAllowed: Boolean) {
        devicePermissionUseCase.setShakeShakePermission(isAllowed)
    }

    private fun isGeolocationAllowed(): Boolean {
        return devicePermissionUseCase.isLocationAllowed()
    }

    fun permissionGeolocationChange(isAllowed: Boolean) {
        _isGeolocationAllowed.value = isAllowed
    }

    fun refreshGeolocationPermission() {
        isShakeShakeAllowed()
        permissionGeolocationChange(isGeolocationAllowed())
    }
}
