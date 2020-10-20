package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class CarouselDataUiModel (
        override val dataKey: String = "",
        val items: List<CarouselItemUiModel> = emptyList(),
        override var error: String = ""
): BaseDataUiModel

class CarouselItemUiModel (
        val id: String,
        val url: String,
        val appLink: String,
        val creativeName: String,
        val featuredMediaURL: String,
        val impressHolder: ImpressHolder
)