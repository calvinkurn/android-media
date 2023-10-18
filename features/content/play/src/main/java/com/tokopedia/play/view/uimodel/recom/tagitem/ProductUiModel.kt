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

val ProductSectionType.isUpcoming: Boolean
    get() = this == ProductSectionType.Upcoming
