package com.tokopedia.sellerhome.view.model

data class CarouselDataUiModel (
    var data: List<CarouselDataModel>
)

class CarouselDataModel (
    var id: String,
    var url: String,
    var applink: String,
    var featuredMediaURL: String
)
