package com.tokopedia.play.view.uimodel.recom.tagitem

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

data class ProductUiModel(
    val productSectionList: List<ProductSectionUiModel>,
    val canShow: Boolean,
) {
    companion object {
        val Empty: ProductUiModel
            get() = ProductUiModel(
                productSectionList = emptyList(),
                canShow = false,
            )
    }
}

data class VariantUiModel(
    val variantDetail: PlayProductUiModel.Product,
    val parentVariant: ProductVariant,
    val selectedVariants: Map<String, String>,
    val categories: List<VariantCategory>,
    val stockWording: String,
) {
    companion object {
        val Empty: VariantUiModel
            get() = VariantUiModel(
                variantDetail = PlayProductUiModel.Product.Empty,
                parentVariant = ProductVariant(),
                selectedVariants = emptyMap(),
                categories = emptyList(),
                stockWording = "",
            )

        fun isVariantPartiallySelected(variantsMap: Map<String, String>): Boolean {
            return variantsMap.any { it.value.toLongOrZero() == 0L } || variantsMap.isEmpty()
        }
    }
}

data class ProductSectionUiModel(
    val productList: List<PlayProductUiModel.Product>,
    val config: ConfigUiModel
) {

    data class ConfigUiModel(
        val type: ProductSectionType,
        val title: String,
        val serverTime: String, // RFC3339
        val startTime: String, // RFC3339
        val endTime: String, // RFC3339
        val timerInfo: String,
        val background: BackgroundUiModel,
        val hasReminder: Boolean
    )

    data class BackgroundUiModel(
        val gradients: List<String>,
        val imageUrl: String
    )

    companion object{
        val Empty: ProductSectionUiModel
            get() = ProductSectionUiModel(
                productList = emptyList(),
                config = ConfigUiModel(
                    type = ProductSectionType.Unknown,
                    title = "",
                    serverTime = "",
                    startTime = "",
                    endTime = "",
                    timerInfo = "",
                    background = BackgroundUiModel(
                        emptyList(),
                        ""
                    ),
                    hasReminder = false
                )
            )
    }
}