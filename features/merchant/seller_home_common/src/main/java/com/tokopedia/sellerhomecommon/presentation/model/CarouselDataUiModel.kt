package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class CarouselDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    val items: List<CarouselItemUiModel> = emptyList()
) : BaseDataUiModel {

    override fun shouldRemove(): Boolean {
        return items.isEmpty() || !showWidget
    }
}

class CarouselItemUiModel(
    val id: String,
    val url: String,
    val appLink: String,
    val creativeName: String,
    val featuredMediaURL: String,
    val impressHolder: ImpressHolder
)