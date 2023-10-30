package com.tokopedia.sellerpersona.view.model

import androidx.compose.runtime.Immutable

/**
 * Created by @ilhamsuaib on 03/10/23.
 */

@Immutable
data class QuestionnaireDataUiModel(
    val currentPage: Int = 0,
    val isNextButtonLoading: Boolean = false,
    val questionnaireList: List<QuestionnairePagerUiModel> = emptyList()
)