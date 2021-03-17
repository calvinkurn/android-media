package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMFinalPeriodDataUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMGradeBenefitAndShopInfoUseCase
import com.tokopedia.power_merchant.subscribe.view.model.PMFinalPeriodUiModel
import com.tokopedia.power_merchant.subscribe.view.model.PMGradeBenefitAndShopInfoUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/03/21
 */

class PowerMerchantSubscriptionViewModel @Inject constructor(
        private val getPMGradeWithBenefitAndShopInfoUseCase: Lazy<GetPMGradeBenefitAndShopInfoUseCase>,
        private val getPMFinalPeriodDataUseCase: Lazy<GetPMFinalPeriodDataUseCase>,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val pmFinalPeriod: LiveData<Result<PMFinalPeriodUiModel>>
        get() = _pmFinalPeriod
    val shopInfoAndPMGradeBenefits: LiveData<Result<PMGradeBenefitAndShopInfoUiModel>>
        get() = _pmGradeAndShopInfo

    private val _pmFinalPeriod: MutableLiveData<Result<PMFinalPeriodUiModel>> = MutableLiveData()
    private val _pmGradeAndShopInfo: MutableLiveData<Result<PMGradeBenefitAndShopInfoUiModel>> = MutableLiveData()

    fun getPMRegistrationData() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getPMGradeWithBenefitAndShopInfoUseCase.get().executeOnBackground()
            }
            _pmGradeAndShopInfo.value = Success(result)
        }, onError = {
            _pmGradeAndShopInfo.value = Fail(it)
        })
    }

    fun getFinalPeriodData() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getPMFinalPeriodDataUseCase.get().executeOnBackground()
            }
            _pmFinalPeriod.value = Success(result)
        }, onError = {
            _pmFinalPeriod.value = Fail(it)
        })
    }

    fun submitPMActivation() {

    }
}