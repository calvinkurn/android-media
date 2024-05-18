package com.tokopedia.topchat.chatroom.view.adapter.util

import android.content.Context
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.reimagine.LABEL_PREVENTIVE_BLOCK
import com.tokopedia.productcard.reimagine.LABEL_PREVENTIVE_OVERLAY
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_CREDIBILITY
import com.tokopedia.productcard.reimagine.LabelGroupStyle
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.topchat.common.Constant.BACKGROUND_COLOR_LABEL_COMPACT
import com.tokopedia.topchat.common.Constant.BACKGROUND_OPACITY_OOS
import com.tokopedia.topchat.common.Constant.BACKGROUND_OPACITY_PREORDER
import com.tokopedia.topchat.common.Constant.EMPTY_STOCK
import com.tokopedia.topchat.common.Constant.PREORDER
import com.tokopedia.topchat.common.Constant.STATUS
import com.tokopedia.topchat.common.Constant.TEXT_COLOR_SOLD
import com.tokopedia.topchat.common.util.ViewUtil.getColorAsHexString
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TopChatRoomProductCardMapper(private val context: Context) {

    fun mapToProductCard(
        productAttachment: ProductAttachmentUiModel
    ): ProductCardModel {
        return ProductCardModel(
            imageUrl = productAttachment.productImage,
            isAds = false,
            name = productAttachment.productName,
            price = productAttachment.productPrice,
            slashedPrice = productAttachment.priceBefore,
            discountPercentage = productAttachment.dropPercentage.toIntOrZero(),
            labelGroupList = getProductCardLabelList(productAttachment),
            rating = if (productAttachment.rating.score > 0) productAttachment.rating.score.toString() else "",
            hasAddToCart = false,
            videoUrl = "",
            hasThreeDots = false,
            stockInfo = ProductCardModel.StockInfo(
                percentage = productAttachment.campaign.percentage.orZero(),
                label = productAttachment.campaign.label.orEmpty(),
                labelColor = productAttachment.campaign.labelColor.orEmpty()
            ),
            isSafeProduct = false,
            isInBackground = true,
            nonVariant = null,
            colorMode = null
        )
    }

    private fun getProductCardLabelList(
        productAttachment: ProductAttachmentUiModel
    ): List<ProductCardModel.LabelGroup> {
        val labelGroupList: MutableList<ProductCardModel.LabelGroup> = mutableListOf()
        if (productAttachment.isPreOrder) {
            labelGroupList.add(
                ProductCardModel.LabelGroup(
                    position = LABEL_PREVENTIVE_OVERLAY,
                    title = PREORDER,
                    styles = listOf(
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.BACKGROUND_COLOR,
                            value = context.getColorAsHexString(unifyprinciplesR.color.Unify_Static_Black)
                        ),
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.BACKGROUND_OPACITY,
                            value = BACKGROUND_OPACITY_PREORDER
                        ),
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.TEXT_COLOR,
                            value = context.getColorAsHexString(unifyprinciplesR.color.Unify_Static_White)
                        )
                    )
                )
            )
        }
        if (productAttachment.hasEmptyStock() &&
            !productAttachment.isProductArchived() &&
            !productAttachment.isProductDummySeeMore() &&
            !productAttachment.isUpcomingCampaign
        ) {
            labelGroupList.add(
                ProductCardModel.LabelGroup(
                    position = LABEL_PREVENTIVE_BLOCK,
                    title = EMPTY_STOCK,
                    styles = listOf(
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.BACKGROUND_COLOR,
                            value = context.getColorAsHexString(unifyprinciplesR.color.Unify_Static_Black)
                        ),
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.BACKGROUND_OPACITY,
                            value = BACKGROUND_OPACITY_OOS
                        ),
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.TEXT_COLOR,
                            value = context.getColorAsHexString(unifyprinciplesR.color.Unify_Static_White)
                        )
                    )
                )
            )
        }
        if (productAttachment.sold.isNotBlank()) {
            labelGroupList.add(
                ProductCardModel.LabelGroup(
                    position = LABEL_REIMAGINE_CREDIBILITY,
                    title = productAttachment.sold,
                    type = TEXT_COLOR_SOLD
                )
            )
        }
        return labelGroupList
    }

    fun mapToProductCardCompact(
        productAttachment: ProductAttachmentUiModel
    ): ProductCardCompactUiModel {
        return ProductCardCompactUiModel(
            productId = productAttachment.productId,
            imageUrl = productAttachment.productImage,
            minOrder = productAttachment.minOrder.coerceAtLeast(1),
            availableStock = productAttachment.remainingStock,
            price = productAttachment.productPrice,
            discount = productAttachment.dropPercentage,
            slashPrice = productAttachment.priceBefore,
            name = productAttachment.productName,
            rating = if (productAttachment.rating.score == 0f) {
                ""
            } else {
                productAttachment.rating.score.toString()
            },
            sold = productAttachment.sold,
            hasBeenWishlist = productAttachment.isWishListed(),
            isVariant = productAttachment.hasVariant(),
            labelGroupList = getProductCardCompactLabelList(productAttachment),
            isPreOrder = productAttachment.isPreOrder
        )
    }

    private fun getProductCardCompactLabelList(
        productAttachment: ProductAttachmentUiModel
    ): List<ProductCardCompactUiModel.LabelGroup> {
        val labelGroupList: MutableList<ProductCardCompactUiModel.LabelGroup> = mutableListOf()
        if (productAttachment.isPreOrder) {
            labelGroupList.add(
                ProductCardCompactUiModel.LabelGroup(
                    position = STATUS,
                    type = BACKGROUND_COLOR_LABEL_COMPACT,
                    title = PREORDER
                )
            )
        }
        if (productAttachment.hasEmptyStock() &&
            !productAttachment.isProductArchived() &&
            !productAttachment.isProductDummySeeMore() &&
            !productAttachment.isUpcomingCampaign &&
            productAttachment.remainingStock < 1
        ) {
            labelGroupList.add(
                ProductCardCompactUiModel.LabelGroup(
                    position = STATUS,
                    type = BACKGROUND_COLOR_LABEL_COMPACT,
                    title = EMPTY_STOCK
                )
            )
        }
        return labelGroupList
    }
}
