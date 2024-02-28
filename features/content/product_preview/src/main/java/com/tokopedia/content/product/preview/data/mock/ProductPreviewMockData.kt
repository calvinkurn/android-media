package com.tokopedia.content.product.preview.data.mock

import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.utils.currency.CurrencyFormatUtil

class ProductPreviewMockData {

    fun mockSourceProduct(
        productId: String,
        productMedia: List<ProductMediaUiModel> = mockProductMediaList(),
        hasReview: Boolean = true,
        ): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            source = ProductPreviewSourceModel.ProductSourceData(
                productSourceList = productMedia,
                hasReviewMedia = hasReview,
            )
        )
    }

    fun mockSourceReview(
        productId: String,
        reviewSourceId: String,
        attachmentId: String,
    ): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            source = ProductPreviewSourceModel.ReviewSourceData(
                reviewSourceId = reviewSourceId,
                attachmentSourceId = attachmentId,
            )
        )
    }

    fun mockSourceUnknown(productId: String): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            source = ProductPreviewSourceModel.UnknownSource,
        )
    }

    fun mockProductMediaList(): List<ProductMediaUiModel> {
        return listOf(
            ProductMediaUiModel(
                contentId = "1",
                selected = true,
                variantName = "Content 1",
                type = MediaType.Video,
                thumbnailUrl = "thumbnail1.url",
                videoLastDuration = 0L,
                videoTotalDuration = 150000L,
                url = "video.source.url",
            ),
            ProductMediaUiModel(
                contentId = "2",
                selected = false,
                variantName = "Content 2",
                type = MediaType.Image,
                thumbnailUrl = "thumbnail2.url",
                url = "image2.source.url",
            ),
            ProductMediaUiModel(
                contentId = "3",
                selected = false,
                variantName = "Content 3",
                type = MediaType.Image,
                thumbnailUrl = "thumbnail3.url",
                url = "image3.source.url",
            ),
        )
    }

    fun mockProductMiniInfo(
        isCampaignActive: Boolean = true,
        hasVariant: Boolean = false,
        buttonState: BottomNavUiModel.ButtonState = BottomNavUiModel.ButtonState.Active,
    ): BottomNavUiModel {
        val ogPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
            price = 10000,
            hasSpace = false
        )
        return BottomNavUiModel(
            title = "Product Name",
            price = if (isCampaignActive) {
                BottomNavUiModel.DiscountedPrice(
                    discountedPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        price = 5000,
                        hasSpace = false
                    ),
                    ogPriceFmt = ogPrice,
                    discountPercentage = "50%"
                )
            } else {
                BottomNavUiModel.NormalPrice(priceFmt = ogPrice)
            },
            stock = 10,
            shop = BottomNavUiModel.Shop(
                id = "shopId_123",
                name = "Shop 123"
            ),
            hasVariant = hasVariant,
            buttonState = if (hasVariant) {
                BottomNavUiModel.ButtonState.Active
            } else {
                BottomNavUiModel.ButtonState.getByValue(
                    buttonState.value
                )
            },
            categoryTree = listOf(
                BottomNavUiModel.CategoryTree(
                    id = "1",
                    name = "Category 1",
                    title = "This is Category 1",
                ),
                BottomNavUiModel.CategoryTree(
                    id = "2",
                    name = "Category 2",
                    title = "This is Category 2",
                ),
                BottomNavUiModel.CategoryTree(
                    id = "3",
                    name = "Category 3",
                    title = "This is Category 3",
                )
            )
        )
    }

}
