package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMGradeBenefitAndShopInfoUseCase
import com.tokopedia.power_merchant.subscribe.view.model.PMGradeBenefitAndShopInfoUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/03/21
 */

class PowerMerchantSubscriptionViewModel @Inject constructor(
        private val getPMGradeWithBenefitAndShopInfoUseCase: Lazy<GetPMGradeBenefitAndShopInfoUseCase>,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val shopInfoAndPMGradeBenefits: LiveData<Result<PMGradeBenefitAndShopInfoUiModel>>
        get() = _pmGradeAndShopInfo

    private val _pmGradeAndShopInfo: MutableLiveData<Result<PMGradeBenefitAndShopInfoUiModel>> = MutableLiveData()

    fun getPMRegistrationData() {
        launchCatchError(block = {
            val result = Success(withContext(dispatcher.io) {
                return@withContext getPMGradeWithBenefitAndShopInfoUseCase.get().executeOnBackground()
            })
            _pmGradeAndShopInfo.postValue(result)
        }, onError = {
            _pmGradeAndShopInfo.value = Fail(it)
        })
    }
}