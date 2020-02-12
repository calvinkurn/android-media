package com.tokopedia.sellerhome.view.model

data class CarouselDataUiModel (
        val dataKey: String = "",
        val data: List<CarouselItemUiModel> = emptyList(),
        override var error: String = ""
): BaseDataUiModel

class CarouselItemUiModel (
        val id: String,
        val url: String,
        val appLink: String,
        val featuredMediaURL: String
)
