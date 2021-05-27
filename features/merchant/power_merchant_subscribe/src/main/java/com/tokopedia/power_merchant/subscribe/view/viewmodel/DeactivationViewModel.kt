package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.domain.interactor.DeactivatePMUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMDeactivationQuestionnaireUseCase
import com.tokopedia.power_merchant.subscribe.view.model.DeactivationQuestionnaireUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class DeactivationViewModel @Inject constructor(
        private val getPMDeactivationQuestionnaireUseCase: GetPMDeactivationQuestionnaireUseCase,
        private val deactivatePmUseCase: DeactivatePMUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _pmDeactivationQuestionnaireData by lazy {
        MutableLiveData<Result<DeactivationQuestionnaireUiModel>>()
    }

    val pmCancellationQuestionnaireData: LiveData<Result<DeactivationQuestionnaireUiModel>> by lazy {
        _pmDeactivationQuestionnaireData
    }

    private val _isSuccessDeactivate by lazy {
        MutableLiveData<Result<Boolean>>()
    }

    val isSuccessDeactivate: LiveData<Result<Boolean>> by lazy {
        _isSuccessDeactivate
    }

    fun getPMCancellationQuestionnaireData(pmTireType: Int) {
        launchCatchError(block = {
            getPMDeactivationQuestionnaireUseCase.params = GetPMDeactivationQuestionnaireUseCase.createParams(PMConstant.PM_SETTING_INFO_SOURCE, pmTireType)
            val result = withContext(dispatchers.io) {
                getPMDeactivationQuestionnaireUseCase.executeOnBackground()
            }
            _pmDeactivationQuestionnaireData.value = Success(result)
        }, onError = {
            _pmDeactivationQuestionnaireData.value = Fail(it)
        })
    }

    fun submitPmDeactivation(questionData: MutableList<PMCancellationQuestionnaireAnswerModel>, currentShopTire: Int, nextShopTire: Int) {
        launchCatchError(block = {
            deactivatePmUseCase.params = DeactivatePMUseCase.createRequestParam(questionData, currentShopTire, nextShopTire)
            val result = Success(withContext(dispatchers.io) {
                deactivatePmUseCase.executeOnBackground()
            })
            _isSuccessDeactivate.value = result
        }, onError = {
            _isSuccessDeactivate.value = Fail(it)
        })
    }
}