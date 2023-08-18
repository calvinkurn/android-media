package com.tokopedia.buy_more_get_more.olp.domain.entity

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactory

data class OfferProductListUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val productList: List<Product> = emptyList(),
    val totalProduct: Int = 0
) {
    data class ResponseHeader(
        val success: Boolean = true,
        val errorMessage: List<String> = emptyList()
    )

    data class Product(
        val offerId: Int = 0,
        val parentId: Int = 0,
        val productId: Int = 0,
        val warehouseId: Int = 0,
        val productUrl: String = "",
        val imageUrl: String = "",
        val name: String = "",
        val price: String = "",
        val rating: String = "",
        val soldCount: Int = 0,
        val stock: Int = 0,
        val isVbs: Boolean = false,
        val campaign: Campaign = Campaign(),
        val labelGroup: List<LabelGroup> = emptyList()
    ) : Visitable<OlpAdapterTypeFactory> {
        data class Campaign(
            val name: String = "",
            val originalPrice: String = "",
            val discountedPrice: String = "",
            val discountedPercentage: Int = 0,
            val customStock: Int = 0
        )

        data class LabelGroup(
            val position: String = "",
            val title: String = "",
            val type: String = "",
            val url: String = ""
        )

        override fun type(typeFactory: OlpAdapterTypeFactory): Int {
            return typeFactory.type(this)
        }
    }
}
