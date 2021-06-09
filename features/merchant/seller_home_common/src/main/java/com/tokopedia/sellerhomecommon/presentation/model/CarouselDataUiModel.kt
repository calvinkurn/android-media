package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class CarouselDataUiModel (
        override var dataKey: String = "",
        val items: List<CarouselItemUiModel> = emptyList(),
        override var error: String = "",
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false
): BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return items.isEmpty()
    }
}

class CarouselItemUiModel (
        val id: String,
        val url: String,
        val appLink: String,
        val creativeName: String,
        val featuredMediaURL: String,
        val impressHolder: ImpressHolder
)