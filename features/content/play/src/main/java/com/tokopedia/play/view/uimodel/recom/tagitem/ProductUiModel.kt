package com.tokopedia.play.view.uimodel.recom.tagitem

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.analytic.TrackingField
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

data class ProductUiModel(
    val productSectionList: List<ProductSectionUiModel>,
    val pinnedProduct: PlayProductUiModel.Product?,
    val canShow: Boolean,
) {
    companion object {
        val Empty: ProductUiModel
            get() = ProductUiModel(
                productSectionList = emptyList(),
                pinnedProduct = null,
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
    val sectionInfo: ProductSectionUiModel.Section = ProductSectionUiModel.Section.Empty,
) {
    companion object {
        val Empty: VariantUiModel
            get() = VariantUiModel(
                variantDetail = PlayProductUiModel.Product.Empty,
                parentVariant = ProductVariant(),
                selectedVariants = emptyMap(),
                categories = emptyList(),
                stockWording = "",
                sectionInfo = ProductSectionUiModel.Section.Empty,
            )

        fun isVariantPartiallySelected(variantsMap: Map<String, String>): Boolean {
            return variantsMap.any { it.value.toLongOrZero() == 0L } || variantsMap.isEmpty()
        }
    }
}

sealed class ProductSectionUiModel {

    data class Section(
        val productList: List<PlayProductUiModel.Product>,
        val config: ConfigUiModel,
        val id: String,
        @TrackingField val impressHolder: ImpressHolder = ImpressHolder(),
    ) : ProductSectionUiModel() {

        data class ConfigUiModel(
            val type: ProductSectionType,
            val title: String,
            val serverTime: String, // RFC3339
            val startTime: String, // RFC3339
            val endTime: String, // RFC3339
            val timerInfo: String,
            val background: BackgroundUiModel,
            val reminder: PlayUpcomingBellStatus
        ){
            companion object{
                val Empty: Section
                    get() = Section(
                        productList = emptyList(),
                        config = ConfigUiModel(
                            type = ProductSectionType.Unknown, title = "", startTime = "", timerInfo = "", serverTime = "", background = BackgroundUiModel(
                                gradients = emptyList(), imageUrl = ""
                            ), endTime = "", reminder = PlayUpcomingBellStatus.Unknown
                        ),
                        id = ""
                    )
            }
        }

        data class BackgroundUiModel(
            val gradients: List<String>,
            val imageUrl: String
        )

        companion object {
            val Empty: Section
                get() = Section(
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
                        reminder = PlayUpcomingBellStatus.Unknown
                    ),
                    id = "",
                )
        }
    }

    object Placeholder : ProductSectionUiModel()
}
