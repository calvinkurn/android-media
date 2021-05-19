package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMActivationStatusUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMGradeBenefitUseCase
import com.tokopedia.gm.common.domain.interactor.PowerMerchantActivateUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMActiveDataUseCase
import com.tokopedia.power_merchant.subscribe.view.model.PMActiveDataUiModel
import com.tokopedia.usecase.RequestParams
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
        private val getPMGradeBenefitUseCase: Lazy<GetPMGradeBenefitUseCase>,
        private val getPMActiveDataUseCase: Lazy<GetPMActiveDataUseCase>,
        private val activatePMUseCase: Lazy<PowerMerchantActivateUseCase>,
        private val userSession: Lazy<UserSessionInterface>,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val pmPmActiveData: LiveData<Result<PMActiveDataUiModel>>
        get() = _pmActiveData
    val pmGradeBenefitInfo: LiveData<Result<List<PMGradeWithBenefitsUiModel>>>
        get() = _pmGradeBenefitInfo
    val pmActivationStatus: LiveData<Result<PMActivationStatusUiModel>>
        get() = _pmActivationStatus
    val pmCancelDeactivationStatus: LiveData<Result<PMActivationStatusUiModel>>
        get() = _pmCancelDeactivationStatus

    private val _pmActiveData: MutableLiveData<Result<PMActiveDataUiModel>> = MutableLiveData()
    private val _pmGradeBenefitInfo: MutableLiveData<Result<List<PMGradeWithBenefitsUiModel>>> = MutableLiveData()
    private val _pmActivationStatus: MutableLiveData<Result<PMActivationStatusUiModel>> = MutableLiveData()
    private val _pmCancelDeactivationStatus: MutableLiveData<Result<PMActivationStatusUiModel>> = MutableLiveData()

    fun getPmRegistrationData() {
        launchCatchError(block = {
            getPMGradeBenefitUseCase.get().params = GetPMGradeBenefitUseCase.createParams(
                    shopId = userSession.get().shopId,
                    source = PMConstant.PM_SETTING_INFO_SOURCE
            )
            val result = withContext(dispatchers.io) {
                getPMGradeBenefitUseCase.get().executeOnBackground()
            }
            _pmGradeBenefitInfo.value = Success(result)
        }, onError = {
            _pmGradeBenefitInfo.value = Fail(it)
        })
    }

    fun getPmActiveData() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getPMActiveDataUseCase.get().executeOnBackground()
            }
            _pmActiveData.value = Success(result)
        }, onError = {
            _pmActiveData.value = Fail(it)
        })
    }

    fun submitPMActivation(nextShopTierType: Int) {
        launchCatchError(block = {
            activatePMUseCase.get().params = PowerMerchantActivateUseCase.createActivationParam(nextShopTierType)
            val result = withContext(dispatchers.io) {
                activatePMUseCase.get().executeOnBackground()
            }
            _pmActivationStatus.value = Success(result)
        }, onError = {
            _pmActivationStatus.value = Fail(it)
        })
    }

    fun cancelPmDeactivationSubmission() {
        launchCatchError(block = {
            activatePMUseCase.get().params = RequestParams.EMPTY
            val result = withContext(dispatchers.io) {
                activatePMUseCase.get().executeOnBackground()
            }
            _pmCancelDeactivationStatus.value = Success(result)
        }, onError = {
            _pmCancelDeactivationStatus.value = Fail(it)
        })
    }
}