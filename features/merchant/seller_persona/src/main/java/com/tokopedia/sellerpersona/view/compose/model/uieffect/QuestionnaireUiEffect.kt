package com.tokopedia.sellerpersona.view.compose.model.uieffect

/**
 * Created by @ilhamsuaib on 04/10/23.
 */

sealed interface QuestionnaireUiEffect {
    data class NavigateToResultPage(
        val personaName: String
    ) : QuestionnaireUiEffect

    object ShowGeneralErrorToast : QuestionnaireUiEffect
}