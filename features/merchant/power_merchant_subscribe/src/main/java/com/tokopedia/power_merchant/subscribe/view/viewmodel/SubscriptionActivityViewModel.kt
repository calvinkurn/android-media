package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantSettingInfoUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/03/21
 */

class SubscriptionActivityViewModel @Inject constructor(
        private val getPowerMerchantSettingInfoUseCase: Lazy<GetPowerMerchantSettingInfoUseCase>,
        private val userSession: Lazy<UserSessionInterface>,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val PM_SETTING_INFO_SOURCE = "power-merchant-subscription-ui"
    }

    val powerMerchantSettingInfo: LiveData<Result<PowerMerchantSettingInfoUiModel>>
        get() = _powerMerchantSettingInfo

    private val _powerMerchantSettingInfo: MutableLiveData<Result<PowerMerchantSettingInfoUiModel>> = MutableLiveData()

    fun getPowerMerchantSettingInfo() {
        launchCatchError(block = {
            val params = GetPowerMerchantSettingInfoUseCase.createParams(userSession.get().shopId, PM_SETTING_INFO_SOURCE)
            getPowerMerchantSettingInfoUseCase.get().params = params
            val result = Success(withContext(dispatcher.io) {
                getPowerMerchantSettingInfoUseCase.get().executeOnBackground()
            })
            _powerMerchantSettingInfo.value = result
        }, onError = {
            _powerMerchantSettingInfo.value = Fail(it)
        })
    }
}