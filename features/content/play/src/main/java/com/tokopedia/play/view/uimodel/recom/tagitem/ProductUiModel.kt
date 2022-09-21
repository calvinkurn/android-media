package com.tokopedia.play.view.uimodel.recom.tagitem

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.analytic.TrackingField
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.utils.date.DateUtil
import java.util.*

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
    val sectionInfo: ProductSectionUiModel.Section = ProductSectionUiModel.Section.Empty,
    val isFeatured: Boolean, //TODO("Temporary workaround, I don't think this is the best place to put it though")
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
                isFeatured = false,
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
    ) : ProductSectionUiModel() {

        @TrackingField
        val impressHolder: ImpressHolder = ImpressHolder()

        data class ConfigUiModel(
            val type: ProductSectionType,
            val title: String,
            val controlTime: Date,
            val serverTime: Date?, // RFC3339
            val startTime: Date?, // RFC3339
            val endTime: Date?, // RFC3339
            val timerInfo: String,
            val background: BackgroundUiModel,
            val reminder: PlayUpcomingBellStatus
        ){
            companion object{
                val Empty: ConfigUiModel
                    get() = ConfigUiModel(
                        type = ProductSectionType.Unknown,
                        title = "",
                        controlTime = DateUtil.getCurrentDate(),
                        startTime = null,
                        serverTime = null,
                        endTime = null,
                        timerInfo = "",
                        background = BackgroundUiModel(
                            gradients = emptyList(),
                            imageUrl = ""
                        ),
                        reminder = PlayUpcomingBellStatus.Unknown,
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
                    config = ConfigUiModel.Empty,
                    id = "",
                )
        }
    }

    object Placeholder : ProductSectionUiModel()
}
