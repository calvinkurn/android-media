package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMActivationStatusUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMGradeBenefitInfoUseCase
import com.tokopedia.gm.common.domain.interactor.PowerMerchantActivateUseCase
import com.tokopedia.gm.common.domain.usecase.GetShopLevelUseCase
import com.tokopedia.gm.common.presentation.model.ShopLevelUiModel
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
    private val getPmGradeBenefitInfoUseCase: Lazy<GetPMGradeBenefitInfoUseCase>,
    private val activatePmUseCase: Lazy<PowerMerchantActivateUseCase>,
    private val getShopLevelUseCase: Lazy<GetShopLevelUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val pmPmActiveData: LiveData<Result<PMGradeBenefitInfoUiModel>>
        get() = _pmActiveData
    val pmActivationStatus: LiveData<Result<PMActivationStatusUiModel>>
        get() = _pmActivationStatus
    val pmCancelDeactivationStatus: LiveData<Result<PMActivationStatusUiModel>>
        get() = _pmCancelDeactivationStatus
    val shopLevelInfo: LiveData<Result<ShopLevelUiModel>>
        get() = _shopLevelInfo

    private val _pmActiveData: MutableLiveData<Result<PMGradeBenefitInfoUiModel>> =
        MutableLiveData()
    private val _pmActivationStatus: MutableLiveData<Result<PMActivationStatusUiModel>> =
        MutableLiveData()
    private val _pmCancelDeactivationStatus: MutableLiveData<Result<PMActivationStatusUiModel>> =
        MutableLiveData()
    private val _shopLevelInfo: MutableLiveData<Result<ShopLevelUiModel>> = MutableLiveData()

    fun getPmActiveStateData(pmTire: Int) {
        launchCatchError(block = {
            val fields = if (pmTire == PMConstant.PMTierType.POWER_MERCHANT_PRO) {
                listOf(
                    GetPMGradeBenefitInfoUseCase.FIELD_CURRENT_PM_GRADE,
                    GetPMGradeBenefitInfoUseCase.FIELD_CURRENT_BENEFIT_LIST,
                    GetPMGradeBenefitInfoUseCase.FIELD_NEXT_PM_GRADE,
                    GetPMGradeBenefitInfoUseCase.FIELD_NEXT_BENEFIT_LIST
                )
            } else {
                listOf(
                    GetPMGradeBenefitInfoUseCase.FIELD_CURRENT_PM_GRADE,
                    GetPMGradeBenefitInfoUseCase.FIELD_CURRENT_BENEFIT_LIST
                )
            }

            getPmGradeBenefitInfoUseCase.get().params = GetPMGradeBenefitInfoUseCase.createParams(
                shopId = userSession.get().shopId,
                source = PMConstant.PM_SETTING_INFO_SOURCE,
                fields = fields
            )
            val result = withContext(dispatchers.io) {
                getPmGradeBenefitInfoUseCase.get().executeOnBackground()
            }
            _pmActiveData.value = Success(result)
        }, onError = {
            _pmActiveData.value = Fail(it)
        })
    }

    fun submitPMActivation() {
        launchCatchError(block = {
            activatePmUseCase.get().params = PowerMerchantActivateUseCase.createActivationParam(PMConstant.PM_SETTING_INFO_SOURCE)
            val result = withContext(dispatchers.io) {
                activatePmUseCase.get().executeOnBackground()
            }
            _pmActivationStatus.value = Success(result)
        }, onError = {
            _pmActivationStatus.value = Fail(it)
        })
    }

    fun cancelPmDeactivationSubmission() {
        launchCatchError(block = {
            activatePmUseCase.get().params = PowerMerchantActivateUseCase.createActivationParam(
                PMConstant.PM_SETTING_INFO_SOURCE
            )
            val result = withContext(dispatchers.io) {
                activatePmUseCase.get().executeOnBackground()
            }
            _pmCancelDeactivationStatus.value = Success(result)
        }, onError = {
            _pmCancelDeactivationStatus.value = Fail(it)
        })
    }

    fun getShopLevelInfo() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getShopLevelUseCase.get().execute(userSession.get().shopId)
            }
            _shopLevelInfo.value = Success(result)
        }, onError = {
            _shopLevelInfo.value = Fail(it)
        })
    }
}