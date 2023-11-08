package com.tokopedia.sellerpersona.view.compose.model.uieffect

/**
 * Created by @ilhamsuaib on 25/07/23.
 */

sealed class SelectTypeUiEffect {
    data class CloseThePage(val persona: String) : SelectTypeUiEffect()
    data class OnPersonaChanged(
        val persona: String,
        val throwable: Throwable? = null
    ) : SelectTypeUiEffect()
}