package com.tokopedia.sellerhome.view.model

data class CarouselDataUiModel (
        val state: CarouselState,
        var data: List<CarouselDataModel>,
        override var error: String
): BaseDataUiModel

class CarouselDataModel (
        var id: String,
        var url: String,
        var applink: String,
        var featuredMediaURL: String
)
