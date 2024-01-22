package com.tokopedia.buy_more_get_more.minicart.presentation.model.effect

/**
 * Created by @ilhamsuaib on 15/12/23.
 */

sealed interface MiniCartEditorEffect {
    data class OnRemoveFailed(val message: String? = "") : MiniCartEditorEffect
    object DismissBottomSheet : MiniCartEditorEffect
}