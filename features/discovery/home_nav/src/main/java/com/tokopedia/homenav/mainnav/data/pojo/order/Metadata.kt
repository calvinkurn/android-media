package com.tokopedia.homenav.mainnav.data.pojo.order

data class Metadata(
    val detailURL: DetailURL,
    val products: List<Product>,
    val status: Status
)