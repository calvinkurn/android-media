package com.tokopedia.sellerpersona.view.model

/**
 * Created by @ilhamsuaib on 03/10/23.
 */

data class QuestionnaireDataUiModel(
    val currentPage: Int = 0,
    val isNextButtonLoading: Boolean = false,
    val questionnaireList: List<QuestionnairePagerUiModel> = emptyList()
)