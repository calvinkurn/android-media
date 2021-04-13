package com.tokopedia.power_merchant.subscribe.view_old.model

import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PMCancellationQuestionnaireStepperModel(
        var listQuestionnaireAnswer: MutableList<PMCancellationQuestionnaireAnswerModel> = mutableListOf()
) : StepperModel {

    fun isCurrentQuestionAnswered(currentPosition: Int): Boolean {
        return listQuestionnaireAnswer[currentPosition - 1].answers.isNotEmpty()
    }

}