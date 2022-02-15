package com.tokopedia.play.view.uimodel

import com.tokopedia.play.data.Section
import com.tokopedia.play.view.type.ProductSectionType

/**
 * @author by astidhiyaa on 02/02/22
 */
sealed class PlayProductSectionUiModel {
    data class ProductSection(
        val type: ProductSectionType,
        val title: String,
        val productList: List<PlayProductUiModel>,
        val serverTime: String,
        val startTime: String,
        val endTime: String,
        val timerInfo: String,
        val background: Section.Background,
        val hasReminder: Boolean
    ): PlayProductSectionUiModel()

    object Shimmer: PlayProductSectionUiModel()
}