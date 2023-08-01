package com.tokopedia.buy_more_get_more.olp.domain.entity

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactory

data class OfferProductListUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val productList: List<Product> = emptyList()
) : Visitable<OlpAdapterTypeFactory> {
    data class ResponseHeader(
        val success: Boolean = true,
        val error_code: Long = 0,
        val processTime: String = ""
    )

    data class Product(
        val offerId: Int = 0,
        val parentId: Int = 0,
        val productId: Int = 0,
        val warehouseId: Int = 0,
        val imageUrl: String = "",
        val name: String = "",
        val price: String = "",
        val rating: String = "",
        val soldCount: Int = 0,
        val minOrder: Int = 0,
        val maxOrder: Int = 0,
        val stock: Int = 0,
        val isVbs: Boolean = false,
        val campaign: Campaign = Campaign()
    ) {
        data class Campaign(
            val name: String = "",
            val originalPrice: String = "",
            val discountedPrice: String = "",
            val discountedPercentage: String = "",
            val minOrder: Int = 0,
            val maxOrder: Int = 0,
            val customStock: Int = 0
        )
    }

    override fun type(typeFactory: OlpAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
