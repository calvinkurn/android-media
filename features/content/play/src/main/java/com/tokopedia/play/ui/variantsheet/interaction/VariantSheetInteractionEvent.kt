package com.tokopedia.play.ui.variantsheet.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 05/03/20
 */
sealed class VariantSheetInteractionEvent : ComponentEvent {

    object OnCloseVariantSheet : VariantSheetInteractionEvent()
}