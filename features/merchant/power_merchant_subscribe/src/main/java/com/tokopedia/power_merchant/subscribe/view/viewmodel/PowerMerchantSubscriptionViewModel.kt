package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.local.model.PMStatusAndShopInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMStatusAndShopInfoUseCase
import com.tokopedia.gm.common.domain.interactor.PowerMerchantActivateUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMActiveDataUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMGradeBenefitAndShopInfoUseCase
import com.tokopedia.power_merchant.subscribe.view.model.PMActiveDataUiModel
import com.tokopedia.power_merchant.subscribe.view.model.PMGradeBenefitAndShopInfoUiModel
import com.tokopedia.power_merchant.subscribe.view_old.util.PowerMerchantRemoteConfig
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
        private val getPMStatusAndShopInfo: Lazy<GetPMStatusAndShopInfoUseCase>,
        private val getPMGradeWithBenefitAndShopInfoUseCase: Lazy<GetPMGradeBenefitAndShopInfoUseCase>,
        private val getPMActiveDataUseCase: Lazy<GetPMActiveDataUseCase>,
        private val activatePMUseCase: Lazy<PowerMerchantActivateUseCase>,
        private val remoteConfig: Lazy<PowerMerchantRemoteConfig>,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val pmPmActiveData: LiveData<Result<PMActiveDataUiModel>>
        get() = _pmActiveData
    val shopInfoAndPMGradeBenefits: LiveData<Result<PMGradeBenefitAndShopInfoUiModel>>
        get() = _pmGradeAndShopInfo
    val pmStatusAndShopInfo: LiveData<Result<PMStatusAndShopInfoUiModel>>
        get() = _pmStatusAndShopInfo
    val pmActivationStatus: LiveData<Result<Boolean>>
        get() = _pmActivationStatus
    val pmCancelDeactivationStatus: LiveData<Result<Boolean>>
        get() = _pmCancelDeactivationStatus

    private val _pmActiveData: MutableLiveData<Result<PMActiveDataUiModel>> = MutableLiveData()
    private val _pmGradeAndShopInfo: MutableLiveData<Result<PMGradeBenefitAndShopInfoUiModel>> = MutableLiveData()
    private val _pmStatusAndShopInfo: MutableLiveData<Result<PMStatusAndShopInfoUiModel>> = MutableLiveData()
    private val _pmActivationStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()
    private val _pmCancelDeactivationStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()

    fun getPmStatusAndShopInfo() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val isFreeShippingEnabled = remoteConfig.get().isFreeShippingEnabled()
                val data = getPMStatusAndShopInfo.get().executeOnBackground()
                return@withContext data.copy(isFreeShippingEnabled = isFreeShippingEnabled)
            }
            _pmStatusAndShopInfo.value = Success(result)
        }, onError = {
            _pmStatusAndShopInfo.value = Fail(it)
        })
    }

    fun getPmRegistrationData() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getPMGradeWithBenefitAndShopInfoUseCase.get().executeOnBackground()
            }
            _pmGradeAndShopInfo.value = Success(result)
        }, onError = {
            _pmGradeAndShopInfo.value = Fail(it)
        })
    }

    fun getPmActiveStateData() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getPMActiveDataUseCase.get().executeOnBackground()
            }
            _pmActiveData.value = Success(result)
        }, onError = {
            _pmActiveData.value = Fail(it)
        })
    }

    fun submitPMActivation() {
        launchCatchError(block = {
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
            val result = withContext(dispatchers.io) {
                activatePMUseCase.get().executeOnBackground()
            }
            _pmCancelDeactivationStatus.value = Success(result)
        }, onError = {
            _pmCancelDeactivationStatus.value = Fail(it)
        })
    }
}