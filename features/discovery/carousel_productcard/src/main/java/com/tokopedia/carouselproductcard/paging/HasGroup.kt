package com.tokopedia.carouselproductcard.paging

internal interface HasGroup {

    val group: CarouselPagingGroupModel

    val pageInGroup: Int

    val pageCount: Int
}
