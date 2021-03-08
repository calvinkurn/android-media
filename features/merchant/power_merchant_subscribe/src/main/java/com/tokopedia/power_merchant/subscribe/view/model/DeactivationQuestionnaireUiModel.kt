package com.tokopedia.power_merchant.subscribe.view.model

data class DeactivationQuestionnaireUiModel(
        val expiredDate: String = "",
        val listQuestion: List<QuestionnaireUiModel> = mutableListOf()
)