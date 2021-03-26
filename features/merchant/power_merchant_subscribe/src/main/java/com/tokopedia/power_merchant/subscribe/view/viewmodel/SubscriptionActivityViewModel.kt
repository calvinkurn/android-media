package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.local.model.PMSettingAndShopInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMSettingAndShopInfoUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/03/21
 */

class SubscriptionActivityViewModel @Inject constructor(
        private val getPMSettingAnsShopInfoUseCase: Lazy<GetPMSettingAndShopInfoUseCase>,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val pmSettingAndShopInfo: LiveData<Result<PMSettingAndShopInfoUiModel>>
        get() = _pmSettingAndShopInfo

    private val _pmSettingAndShopInfo: MutableLiveData<Result<PMSettingAndShopInfoUiModel>> = MutableLiveData()

    fun getPowerMerchantSettingInfo() {
        launchCatchError(block = {
            val result = Success(withContext(dispatcher.io) {
                getPMSettingAnsShopInfoUseCase.get().executeOnBackground()
            })
            _pmSettingAndShopInfo.value = result
        }, onError = {
            _pmSettingAndShopInfo.value = Fail(it)
        })
    }
}