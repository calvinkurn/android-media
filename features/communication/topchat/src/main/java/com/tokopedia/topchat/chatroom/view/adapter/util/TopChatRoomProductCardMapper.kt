package com.tokopedia.topchat.chatroom.view.adapter.util

import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.reimagine.LABEL_PREVENTIVE_BLOCK
import com.tokopedia.productcard.reimagine.LABEL_PREVENTIVE_OVERLAY
import com.tokopedia.productcard.reimagine.LabelGroupStyle
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.topchat.common.Constant.BACKGROUND_COLOR_LABEL
import com.tokopedia.topchat.common.Constant.BACKGROUND_COLOR_LABEL_COMPACT
import com.tokopedia.topchat.common.Constant.BACKGROUND_OPACITY_OOS
import com.tokopedia.topchat.common.Constant.BACKGROUND_OPACITY_PREORDER
import com.tokopedia.topchat.common.Constant.EMPTY_STOCK
import com.tokopedia.topchat.common.Constant.PREORDER
import com.tokopedia.topchat.common.Constant.STATUS
import com.tokopedia.topchat.common.Constant.TEXT_COLOR_LABEL

object TopChatRoomProductCardMapper {

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
            rating = productAttachment.rating.score.toString(),
            shopBadge = ProductCardModel.ShopBadge(),
            hasAddToCart = false,
            videoUrl = "",
            hasThreeDots = false,
            // TODO: get data from BE
            stockInfo = ProductCardModel.StockInfo(
                percentage = 30,
                label = "Segera Habis",
                labelColor = ""
            ),
            isSafeProduct = false,
            isInBackground = false,
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
                            value = BACKGROUND_COLOR_LABEL
                        ),
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.BACKGROUND_OPACITY,
                            value = BACKGROUND_OPACITY_PREORDER
                        ),
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.TEXT_COLOR,
                            value = TEXT_COLOR_LABEL
                        )
                    )
                )
            )
        }
        if (!productAttachment.isProductActive() &&
            !productAttachment.isProductArchived() &&
            !productAttachment.isProductDummySeeMore() &&
            !productAttachment.isUpcomingCampaign &&
            productAttachment.remainingStock < 1
        ) {
            labelGroupList.add(
                ProductCardModel.LabelGroup(
                    position = LABEL_PREVENTIVE_BLOCK,
                    title = EMPTY_STOCK,
                    styles = listOf(
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.BACKGROUND_COLOR,
                            value = BACKGROUND_COLOR_LABEL
                        ),
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.BACKGROUND_OPACITY,
                            value = BACKGROUND_OPACITY_OOS
                        ),
                        ProductCardModel.LabelGroup.Style(
                            key = LabelGroupStyle.TEXT_COLOR,
                            value = TEXT_COLOR_LABEL
                        )
                    )
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
            minOrder = productAttachment.minOrder,
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
            hasBeenWishlist = productAttachment.isWishListed(),
            isVariant = productAttachment.hasVariant(),
            labelGroupList = getProductCardCompactLabelList(productAttachment)
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
        if (!productAttachment.isProductActive() &&
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

