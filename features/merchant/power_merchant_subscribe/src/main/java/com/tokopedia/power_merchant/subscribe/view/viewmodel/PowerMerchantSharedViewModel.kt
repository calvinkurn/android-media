package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantBasicInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMBasicInfoUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetShopModerationStatusUseCase
import com.tokopedia.power_merchant.subscribe.view.model.ModerationShopStatusUiModel
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantRemoteConfig
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 05/05/21
 */

class PowerMerchantSharedViewModel @Inject constructor(
        private val getPmBasicInfo: Lazy<GetPMBasicInfoUseCase>,
        private val getShopModerationStatusUseCase: Lazy<GetShopModerationStatusUseCase>,
        private val remoteConfig: Lazy<PowerMerchantRemoteConfig>,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val powerMerchantBasicInfo: LiveData<Result<PowerMerchantBasicInfoUiModel>>
        get() = _powerMerchantBasicInfo
    val shopModerationStatus: LiveData<Result<ModerationShopStatusUiModel>>
        get() = _shopModerationStatus

    private val _powerMerchantBasicInfo: MutableLiveData<Result<PowerMerchantBasicInfoUiModel>> = MutableLiveData()
    private val _shopModerationStatus: MutableLiveData<Result<ModerationShopStatusUiModel>> = MutableLiveData()

    fun getPowerMerchantBasicInfo() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val isFreeShippingEnabled = remoteConfig.get().isFreeShippingEnabled()
                val data = getPmBasicInfo.get().executeOnBackground()
                return@withContext data.copy(isFreeShippingEnabled = isFreeShippingEnabled)
            }
            _powerMerchantBasicInfo.value = Success(result)
        }, onError = {
            _powerMerchantBasicInfo.value = Fail(it)
        })
    }

    fun getShopModerationStatus(shopId: Long) {
        launchCatchError(block = {
            getShopModerationStatusUseCase.get().params = GetShopModerationStatusUseCase.createParam(shopId)
            val result = withContext(dispatchers.io) {
                return@withContext getShopModerationStatusUseCase.get().executeOnBackground()
            }
            _shopModerationStatus.value = Success(result)
        }, onError = {
            _shopModerationStatus.value = Fail(it)
        })
    }
}