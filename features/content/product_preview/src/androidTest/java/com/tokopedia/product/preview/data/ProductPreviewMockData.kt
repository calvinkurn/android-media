package com.tokopedia.product.preview.data

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
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel.ProductPreviewSourceName
import com.tokopedia.utils.currency.CurrencyFormatUtil

class ProductPreviewMockData {

    fun mockSourceProduct(
        productId: String,
        productMedia: List<ProductMediaUiModel> = mockProductMediaList(),
        hasReview: Boolean = true
    ): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            sourceName = ProductPreviewSourceName.PRODUCT,
            source = ProductPreviewSourceModel.ProductSourceData(
                productSourceList = productMedia,
                hasReviewMedia = hasReview
            )
        )
    }

    fun mockSourceReview(
        productId: String,
        reviewSourceId: String,
        attachmentId: String
    ): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            sourceName = ProductPreviewSourceName.REVIEW,
            source = ProductPreviewSourceModel.ReviewSourceData(
                reviewSourceId = reviewSourceId,
                attachmentSourceId = attachmentId
            )
        )
    }

    fun mockSourceUnknown(productId: String): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            sourceName = ProductPreviewSourceName.UNKNOWN,
            source = ProductPreviewSourceModel.UnknownSource
        )
    }

    fun mockProductMediaList(): List<ProductMediaUiModel> {
        return listOf(
            ProductMediaUiModel(
                contentId = "1",
                selected = true,
                variantName = "Content 1",
                type = MediaType.Video,
                thumbnailUrl = "https://images.tokopedia.net/img/jJtrdn/2022/1/21/2f1ba9eb-a8d4-4de1-b445-ed66b96f26a9.jpg?b=UaM%25G%23Rjn4WYVBx%5DjFWX%3D~t6bbWB0PkWkqoL",
                videoLastDuration = 0L,
                videoTotalDuration = 150000L,
                url = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372"
            ),
            ProductMediaUiModel(
                contentId = "2",
                selected = false,
                variantName = "Content 2",
                type = MediaType.Image,
                thumbnailUrl = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g"
            ),
            ProductMediaUiModel(
                contentId = "3",
                selected = false,
                variantName = "Content 3",
                type = MediaType.Image,
                thumbnailUrl = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
            )
        )
    }

    fun mockProductMediaListSelectedNotFirstIndex(): List<ProductMediaUiModel> {
        return listOf(
            ProductMediaUiModel(
                contentId = "1",
                selected = false,
                variantName = "Content 1",
                type = MediaType.Video,
                thumbnailUrl = "https://images.tokopedia.net/img/jJtrdn/2022/1/21/2f1ba9eb-a8d4-4de1-b445-ed66b96f26a9.jpg?b=UaM%25G%23Rjn4WYVBx%5DjFWX%3D~t6bbWB0PkWkqoL",
                url = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372"
            ),
            ProductMediaUiModel(
                contentId = "2",
                selected = true,
                variantName = "Content 2",
                type = MediaType.Image,
                thumbnailUrl = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g"
            ),
            ProductMediaUiModel(
                contentId = "3",
                selected = false,
                variantName = "Content 3",
                type = MediaType.Image,
                thumbnailUrl = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
            )
        )
    }

    fun mockReviewDataByIds(
        reviewId: String
    ): ReviewUiModel {
        val reviewContent = listOf(
            ReviewContentUiModel(
                reviewId = reviewId,
                medias = listOf(
                    ReviewMediaUiModel(
                        mediaId = "mediaId_1",
                        type = MediaType.Video,
                        selected = true
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_2",
                        type = MediaType.Image,
                        selected = false
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_3",
                        type = MediaType.Image,
                        selected = false
                    )
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
                isShareAble = true
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
                reviewId = "reviewId_1",
                medias = listOf(
                    ReviewMediaUiModel(
                        mediaId = "mediaId_1",
                        type = MediaType.Image,
                        selected = true,
                        url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_2",
                        type = MediaType.Image,
                        selected = false,
                        url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_3",
                        type = MediaType.Image,
                        selected = false,
                        url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
                    )
                ),
                menus = ReviewMenuStatus(isReportable = true),
                likeState = ReviewLikeUiState(
                    count = 5,
                    state = ReviewLikeUiState.ReviewLikeStatus.getByValue(2),
                    withAnimation = true
                ),
                author = ReviewAuthorUiModel(
                    name = "Author Name 1",
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
                isShareAble = true
            ),
            ReviewContentUiModel(
                reviewId = "reviewId_12",
                medias = listOf(
                    ReviewMediaUiModel(
                        mediaId = "mediaId_1",
                        type = MediaType.Image,
                        selected = true,
                        url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_2",
                        type = MediaType.Image,
                        selected = false,
                        url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_3",
                        type = MediaType.Image,
                        selected = false,
                        url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
                    )
                ),
                menus = ReviewMenuStatus(isReportable = true),
                likeState = ReviewLikeUiState(
                    count = 5,
                    state = ReviewLikeUiState.ReviewLikeStatus.getByValue(2),
                    withAnimation = true
                ),
                author = ReviewAuthorUiModel(
                    name = "Author Name 2",
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
                isShareAble = true
            ),
            ReviewContentUiModel(
                reviewId = "reviewId_1235",
                medias = listOf(
                    ReviewMediaUiModel(
                        mediaId = "mediaId_1",
                        type = MediaType.Image,
                        selected = true,
                        url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_2",
                        type = MediaType.Image,
                        selected = false,
                        url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
                    ),
                    ReviewMediaUiModel(
                        mediaId = "mediaId_3",
                        type = MediaType.Image,
                        selected = false,
                        url = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc"
                    )
                ),
                menus = ReviewMenuStatus(isReportable = true),
                likeState = ReviewLikeUiState(
                    count = 5,
                    state = ReviewLikeUiState.ReviewLikeStatus.getByValue(2),
                    withAnimation = true
                ),
                author = ReviewAuthorUiModel(
                    name = "Author Name 3",
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
                isShareAble = true
            )
        )
        return ReviewUiModel(
            reviewPaging = ReviewPaging.Success(1, true),
            reviewContent = reviewContent
        )
    }

    fun mockProductMiniInfo(
        isCampaignActive: Boolean = true,
        hasVariant: Boolean = false,
        buttonState: BottomNavUiModel.ButtonState = BottomNavUiModel.ButtonState.Active
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
                    title = "This is Category 1"
                ),
                BottomNavUiModel.CategoryTree(
                    id = "2",
                    name = "Category 2",
                    title = "This is Category 2"
                ),
                BottomNavUiModel.CategoryTree(
                    id = "3",
                    name = "Category 3",
                    title = "This is Category 3"
                )
            )
        )
    }

    fun mockReviewReport(): ReviewReportUiModel {
        return ReviewReportUiModel(
            text = "Muka sellernya ngeselin",
            reasonCode = 12
        )
    }
}
