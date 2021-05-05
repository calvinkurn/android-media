package com.tokopedia.tokomart.home.domain.model

data class GetHomeLayoutResponse(
    val data: List<HomeLayoutResponse>
)

data class HomeLayoutResponse(
    val id: String,
    val title: String,
    val type: Int
)