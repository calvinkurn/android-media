package com.tokopedia.product.manage.feature.list.view.model

data class GetFilterTabResult(
    val tabs: List<FilterTabViewModel>,
    val totalProductCount: Int
)