package com.tokopedia.content.product.preview.data.mock

import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewDescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMenuStatus
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewPaging
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel
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

    fun mockProductMediaListSelectedNotFirstIndex(): List<ProductMediaUiModel> {
        return listOf(
            ProductMediaUiModel(
                contentId = "1",
                selected = false,
                variantName = "Content 1",
                type = MediaType.Video,
                thumbnailUrl = "thumbnail1.url",
                url = "image1.source.url",
            ),
            ProductMediaUiModel(
                contentId = "2",
                selected = true,
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

    fun mockReviewDataByIds(
        reviewId: String,
    ): ReviewUiModel {
        val reviewContent = listOf(
            ReviewContentUiModel(
                reviewId = reviewId,
                medias = listOf(
                    ReviewMediaUiModel(
                        mediaId = "mediaId_1",
                        type = MediaType.Video,
                        selected = true,
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_2",
                        type = MediaType.Image,
                        selected = false,
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_3",
                        type = MediaType.Image,
                        selected = false,
                    ),
                ),
                menus = ReviewMenuStatus(isReportable = true),
                likeState = ReviewLikeUiState(
                    count = 5,
                    state = ReviewLikeUiState.ReviewLikeStatus.getByValue(1),
                    withAnimation = true
                ),
                author = ReviewAuthorUiModel(
                    name = "Author Name",
                    type = "Shop",
                    id = "userId_12345",
                    avatarUrl = "avatar.url",
                    appLink = "user.applink"
                ),
                description = ReviewDescriptionUiModel(
                    stars = 5,
                    productType = "product type",
                    timestamp = "12345",
                    description = "description"
                ),
                mediaSelectedPosition = 0,
                isWatchMode = false,
                isScrolling = false,
            )
        )
        return ReviewUiModel(
            reviewPaging = ReviewPaging.Success(0, true),
            reviewContent = reviewContent
        )
    }

    fun mockReviewData(): ReviewUiModel {
        val reviewContent = listOf(
            ReviewContentUiModel(
                reviewId = "reviewId_123",
                medias = listOf(
                    ReviewMediaUiModel(
                        mediaId = "mediaId_1",
                        type = MediaType.Video,
                        selected = true,
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_2",
                        type = MediaType.Image,
                        selected = false,
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_3",
                        type = MediaType.Image,
                        selected = false,
                    ),
                ),
                menus = ReviewMenuStatus(isReportable = true),
                likeState = ReviewLikeUiState(
                    count = 5,
                    state = ReviewLikeUiState.ReviewLikeStatus.getByValue(2),
                    withAnimation = true
                ),
                author = ReviewAuthorUiModel(
                    name = "Author Name",
                    type = "Shop",
                    id = "userId_12345",
                    avatarUrl = "avatar.url",
                    appLink = "user.applink"
                ),
                description = ReviewDescriptionUiModel(
                    stars = 5,
                    productType = "product type",
                    timestamp = "12345",
                    description = "description"
                ),
                mediaSelectedPosition = 0,
                isWatchMode = false,
                isScrolling = false,
            ),
            ReviewContentUiModel(
                reviewId = "reviewId_1234",
                medias = listOf(
                    ReviewMediaUiModel(
                        mediaId = "mediaId_1",
                        type = MediaType.Video,
                        selected = true,
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_2",
                        type = MediaType.Image,
                        selected = false,
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_3",
                        type = MediaType.Image,
                        selected = false,
                    ),
                ),
                menus = ReviewMenuStatus(isReportable = true),
                likeState = ReviewLikeUiState(
                    count = 5,
                    state = ReviewLikeUiState.ReviewLikeStatus.getByValue(2),
                    withAnimation = true
                ),
                author = ReviewAuthorUiModel(
                    name = "Author Name",
                    type = "Shop",
                    id = "userId_12345",
                    avatarUrl = "avatar.url",
                    appLink = "user.applink"
                ),
                description = ReviewDescriptionUiModel(
                    stars = 5,
                    productType = "product type",
                    timestamp = "12345",
                    description = "description"
                ),
                mediaSelectedPosition = 0,
                isWatchMode = false,
                isScrolling = false,
            ),
            ReviewContentUiModel(
                reviewId = "reviewId_1235",
                medias = listOf(
                    ReviewMediaUiModel(
                        mediaId = "mediaId_1",
                        type = MediaType.Video,
                        selected = true,
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_2",
                        type = MediaType.Image,
                        selected = false,
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_3",
                        type = MediaType.Image,
                        selected = false,
                    ),
                ),
                menus = ReviewMenuStatus(isReportable = true),
                likeState = ReviewLikeUiState(
                    count = 5,
                    state = ReviewLikeUiState.ReviewLikeStatus.getByValue(2),
                    withAnimation = true
                ),
                author = ReviewAuthorUiModel(
                    name = "Author Name",
                    type = "Shop",
                    id = "userId_12345",
                    avatarUrl = "avatar.url",
                    appLink = "user.applink"
                ),
                description = ReviewDescriptionUiModel(
                    stars = 5,
                    productType = "product type",
                    timestamp = "12345",
                    description = "description"
                ),
                mediaSelectedPosition = 0,
                isWatchMode = false,
                isScrolling = false,
            ),
        )
        return ReviewUiModel(
            reviewPaging = ReviewPaging.Success(1, true),
            reviewContent = reviewContent
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
                BottomNavUiModel.Price.DiscountedPrice(
                    discountedPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        price = 5000,
                        hasSpace = false
                    ),
                    ogPriceFmt = ogPrice,
                    discountPercentage = "50%"
                )
            } else {
                BottomNavUiModel.Price.NormalPrice(ogPriceFmt = ogPrice)
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

    fun mockReviewReport(): ReviewReportUiModel {
        return ReviewReportUiModel(
            text = "Muka sellernya ngeselin",
            reasonCode = 12,
        )
    }

}
