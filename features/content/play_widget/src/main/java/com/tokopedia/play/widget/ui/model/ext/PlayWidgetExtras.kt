package com.tokopedia.play.widget.ui.model.ext

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 05/05/23
 */
private const val PLAY_WIDGET_EXTRA_IS_MUTED = "is_muted"

internal val PlayWidgetChannelUiModel.isMuted: Boolean
    get() = extras.getOrElse(PLAY_WIDGET_EXTRA_IS_MUTED) { true } as Boolean

internal fun PlayWidgetChannelUiModel.setMute(shouldMute: Boolean): PlayWidgetChannelUiModel {
    return copy(
        extras = extras + (PLAY_WIDGET_EXTRA_IS_MUTED to shouldMute)
    )
}


private const val PLAY_WIDGET_EXTRA_CAROUSEL_VARIANT_WITH_PRODUCT = "carousel_variant"

internal val PlayWidgetChannelUiModel.isWithProductNoCaptionVariant: Boolean
    get() = extras.getOrElse(PLAY_WIDGET_EXTRA_CAROUSEL_VARIANT_WITH_PRODUCT) { false } as Boolean

internal fun PlayWidgetChannelUiModel.setWithProductNoCaptionVariant(
    isVariantWithProduct: Boolean
): PlayWidgetChannelUiModel {
    return copy(
        extras = extras + (PLAY_WIDGET_EXTRA_CAROUSEL_VARIANT_WITH_PRODUCT to isVariantWithProduct)
    )
}
