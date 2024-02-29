package com.tokopedia.catalog.ui.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CatalogProductListUiModel(
    val header: HeaderUiModel = HeaderUiModel(),
    val products: List<CatalogProductUiModel> = listOf()
) {
    data class HeaderUiModel(
        val totalData: Int = 0
    )

    data class CatalogProductUiModel(
        val additionalService: AdditionalServiceUiModel = AdditionalServiceUiModel(),
        val credibility: CredibilityUiModel = CredibilityUiModel(),
        val delivery: DeliveryUiModel = DeliveryUiModel(),
        val isVariant: Boolean = false,
        val labelGroups: List<LabelGroupUiModel> = listOf(),
        val mediaUrl: MediaUrlUiModel =  MediaUrlUiModel(),
        val paymentOption: PaymentOptionUiModel = PaymentOptionUiModel(),
        val price: PriceUiModel= PriceUiModel(),
        val productID: String =  "",
        val shop: ShopUiModel = ShopUiModel(),
        val warehouseID: String = "",
        val stock: StockUiModel = StockUiModel()
    ) {
        data class AdditionalServiceUiModel(
            val name: String = ""
        )

        data class CredibilityUiModel(
            val rating: String = "",
            val ratingCount: String = "",
            val sold: String = ""
        )

        data class DeliveryUiModel(
            val eta: String = "",
            val type: String = ""
        )

        data class LabelGroupUiModel(
            val position: String = "",
            val title: String = "",
            val url: String = ""
        )

        data class MediaUrlUiModel(
            val image: String = "",
            val image300: String = "",
            val image500: String = "",
            val image700: String = ""
        )

        data class PaymentOptionUiModel(
            val desc: String = "",
            val iconUrl: String = ""
        )

        data class PriceUiModel(
            val original: String = "",
            val text: String = ""
        )

        data class ShopUiModel(
            val badge: String = "",
            val city: String = "",
            val id: String = "",
            val name: String = ""
        )

        data class StockUiModel(
            val isHidden: Boolean = false,
            val soldPercentage: Int = 0,
            val wording: String = ""
        )
    }
}
