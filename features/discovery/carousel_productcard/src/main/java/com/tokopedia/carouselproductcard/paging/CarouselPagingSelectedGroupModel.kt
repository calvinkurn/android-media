package com.tokopedia.carouselproductcard.paging

data class CarouselPagingSelectedGroupModel(
    val group: CarouselPagingGroupModel,
    val direction: CarouselPagingGroupChangeDirection,
) {

    val title: String = group.title
}
