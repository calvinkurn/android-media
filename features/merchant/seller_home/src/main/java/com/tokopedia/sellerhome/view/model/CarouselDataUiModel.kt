package com.tokopedia.sellerhome.view.model

data class CarouselDataUiModel (
        val data: List<CarouselDataModel>,
        override var error: String
): BaseDataUiModel

class CarouselDataModel (
        val id: String,
        val url: String,
        val appLink: String,
        val featuredMediaURL: String
)
