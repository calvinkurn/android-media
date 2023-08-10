package com.tokopedia.topads.dashboard.recommendation.data.model.local

interface ProductListUiModel {
    fun id(): String
    fun equalsWith(newItem: ProductListUiModel): Boolean
}

data class ProductItemUiModel(
    val productId: String = "",
    val productName: String = "",
    val imgUrl: String = "",
    val searchCount: String = "",
    var isSelected: Boolean = false
) : ProductListUiModel {
    override fun id(): String {
        return productId
    }

    override fun equalsWith(newItem: ProductListUiModel): Boolean {
        return this == newItem
    }

}
