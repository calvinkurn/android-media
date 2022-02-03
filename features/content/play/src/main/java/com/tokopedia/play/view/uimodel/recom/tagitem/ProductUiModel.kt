package com.tokopedia.play.view.uimodel.recom.tagitem

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.view.uimodel.PlayProductSectionUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

data class ProductUiModel(
    val productList: List<PlayProductUiModel.Product>,
    val canShow: Boolean,
) {
    companion object {
        val Empty: ProductUiModel
            get() = ProductUiModel(
                productList = emptyList(),
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

data class SectionUiModel(
    val sections : List<PlayProductSectionUiModel.ProductSection>
){
    companion object{
        val Empty: SectionUiModel
            get() = SectionUiModel(
                sections = emptyList()
            )
    }
}