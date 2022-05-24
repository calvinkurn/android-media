package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantBasicInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMBasicInfoUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 24/05/22.
 */

class MembershipActivityViewModel @Inject constructor(
    private val getPmBasicInfo: Lazy<GetPMBasicInfoUseCase>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val powerMerchantBasicInfo: LiveData<Result<PowerMerchantBasicInfoUiModel>>
        get() = _powerMerchantBasicInfo

    private val _powerMerchantBasicInfo = MutableLiveData<Result<PowerMerchantBasicInfoUiModel>>()

    fun getPowerMerchantBasicInfo(isFirstLoad: Boolean) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                return@withContext getPmBasicInfo.get().executeOnBackground(isFirstLoad)
            }
            _powerMerchantBasicInfo.value = Success(result)
        }, onError = {
            _powerMerchantBasicInfo.value = Fail(it)
        })
    }
}