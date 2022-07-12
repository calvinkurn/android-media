package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.domain.interactor.DeactivatePMUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.power_merchant.subscribe.domain.usecase.GetPMDeactivationQuestionnaireUseCase
import com.tokopedia.power_merchant.subscribe.view.model.DeactivationQuestionnaireUiModel
import com.tokopedia.gm.common.presentation.model.DeactivationResultUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class DeactivationViewModel @Inject constructor(
        private val getPmDeactivationQuestionnaireUseCase: Lazy<GetPMDeactivationQuestionnaireUseCase>,
        private val deactivatePmUseCase: Lazy<DeactivatePMUseCase>,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _pmDeactivationQuestionnaireData by lazy {
        MutableLiveData<Result<DeactivationQuestionnaireUiModel>>()
    }

    val pmCancellationQuestionnaireData: LiveData<Result<DeactivationQuestionnaireUiModel>> by lazy {
        _pmDeactivationQuestionnaireData
    }

    private val _pmDeactivateStatus by lazy {
        MutableLiveData<Result<DeactivationResultUiModel>>()
    }

    val pmDeactivateStatus: LiveData<Result<DeactivationResultUiModel>> by lazy {
        _pmDeactivateStatus
    }

    fun getPMCancellationQuestionnaireData(pmTireType: Int, isFirstLoad: Boolean) {
        launchCatchError(block = {
            getPmDeactivationQuestionnaireUseCase.get().setCacheStrategy(GetPMDeactivationQuestionnaireUseCase.getCacheStrategy(isFirstLoad))
            getPmDeactivationQuestionnaireUseCase.get().params = GetPMDeactivationQuestionnaireUseCase.createParams(PMConstant.PM_SETTING_INFO_SOURCE, pmTireType)
            val result = withContext(dispatchers.io) {
                getPmDeactivationQuestionnaireUseCase.get().executeOnBackground()
            }
            _pmDeactivationQuestionnaireData.value = Success(result)
        }, onError = {
            _pmDeactivationQuestionnaireData.value = Fail(it)
        })
    }

    fun submitPmDeactivation(questionData: MutableList<PMCancellationQuestionnaireAnswerModel>) {
        launchCatchError(block = {
            deactivatePmUseCase.get().params = DeactivatePMUseCase.createRequestParam(questionData)
            val result = Success(withContext(dispatchers.io) {
                deactivatePmUseCase.get().executeOnBackground()
            })
            _pmDeactivateStatus.value = result
        }, onError = {
            _pmDeactivateStatus.value = Fail(it)
        })
    }
}