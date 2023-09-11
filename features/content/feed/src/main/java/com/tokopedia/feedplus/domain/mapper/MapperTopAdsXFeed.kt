package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.content.common.report_content.model.FeedContentData
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedMediaModel
import com.tokopedia.feedplus.presentation.model.FeedMediaTagging
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.widget.TopAdsBannerView

/**
 * Created By : Muhammad Furqan on 05/04/23
 */
object MapperTopAdsXFeed {
    fun transformCpmToFeedTopAds(
        currentModel: FeedCardImageContentModel,
        cpmModel: CpmModel
    ): FeedCardImageContentModel {
        val data = cpmModel.data.firstOrNull()
        val topAdsShop = data?.cpm?.cpmShop

        val authorName = TopAdsBannerView.escapeHTML(data?.cpm?.cpmShop?.name.orEmpty())
        val shopName = topAdsShop?.domain.orEmpty()
        val redirectLinkShop = data?.redirect.orEmpty()
        val merchantVouchers = topAdsShop?.merchantVouchers.orEmpty()

        val feedAuthor = FeedAuthorModel(
            id = data?.cpm?.cpmShop?.id.orEmpty(),
            type = AuthorType.Shop,
            name = authorName,
            badgeUrl = data?.cpm?.badges?.firstOrNull()?.imageUrl.orEmpty(),
            logoUrl = data?.cpm?.cpmImage?.fullEcs.orEmpty(),
            appLink = data?.applinks.orEmpty(),
            encryptedUserId = ""
        )

        val mediaList = topAdsShop?.products?.mapIndexed { index, product ->
            transformTopAdsMediaToFeed(
                product,
                index
            )
        }

        val productList = topAdsShop?.products?.map { product ->
            transformTopAdsProductToFeed(
                product,
                merchantVouchers,
                topAdsShop.id,
                shopName
            )
        }

        val followers = currentModel.followers.copy(
            isFollowed = data?.cpm?.cpmShop?.isFollowed.orFalse()
        )

        return currentModel.copy(
            isFetched = true,
            id = data?.id ?: "",
            author = feedAuthor,
            title = data?.cpm?.name.orEmpty(),
            subtitle = data?.cpm?.decription.orEmpty(),
            text = data?.cpm?.cpmShop?.slogan.orEmpty(),
            applink = data?.applinks.orEmpty(),
            weblink = redirectLinkShop,
            products = productList.orEmpty(),
            totalProducts = productList?.size.orZero(),
            media = mediaList.orEmpty(),
            followers = followers,
            menuItems = listOf(
                FeedMenuItem(
                    iconUnify = IconUnify.VISIBILITY,
                    name = R.string.feed_watch_mode,
                    type = FeedMenuIdentifier.WatchMode,
                    contentData = FeedContentData(
                        data?.cpm?.cpmShop?.slogan.orEmpty(),
                        data?.id.orEmpty(),
                        feedAuthor.id
                    )
                )
            ),
            adViewUri = data?.cpm?.uri.orEmpty(),
            adViewUrl = data?.cpm?.cpmImage?.fullUrl.orEmpty(),
            share = currentModel.share.copy(
                contentId = data?.id.orEmpty(),
                author = feedAuthor,
                appLink = data?.applinks.orEmpty(),
                webLink = redirectLinkShop,
                mediaUrl = mediaList?.firstOrNull()?.mediaUrl.orEmpty()
            )
        )
    }

    private fun transformTopAdsMediaToFeed(
        product: Product,
        index: Int
    ) = FeedMediaModel(
        type = "image",
        id = product.id,
        coverUrl = "",
        mediaUrl = product.image.m_url,
        appLink = product.applinks,
        tagging = listOf(FeedMediaTagging(index))
    )

    private fun transformTopAdsProductToFeed(
        product: Product,
        merchantVouchers: List<String>,
        shopId: String,
        shopName: String
    ) = FeedCardProductModel(
        id = product.id,
        name = product.name,
        coverUrl = product.imageProduct.imageUrl,
        weblink = product.uri,
        applink = product.applinks,
        star = product.productRating.toDouble(),
        price = 0.0,
        priceFmt = product.priceFormat.replace(" ", ""),
        isDiscount = product.campaign.discountPercentage > 0,
        discount = product.campaign.discountPercentage.toDouble(),
        discountFmt = if (product.campaign.discountPercentage > 0) "${product.campaign.discountPercentage}%" else "",
        priceOriginal = 0.0,
        priceOriginalFmt = product.campaign.originalPrice.replace(" ", ""),
        priceDiscount = 0.0,
        priceDiscountFmt = product.priceFormat.replace(" ", ""),
        totalSold = product.countSold.toIntSafely(),
        isBebasOngkir = product.freeOngkir.isActive,
        bebasOngkirStatus = "",
        bebasOngkirUrl = product.freeOngkir.imageUrl,
        shopId = shopId,
        shopName = shopName,
        priceMasked = 0.0,
        priceMaskedFmt = "",
        stockWording = "",
        stockSoldPercentage = 0f,
        cartable = true,
        isCashback = product.isProductCashback,
        cashbackFmt = merchantVouchers.firstOrNull().orEmpty()
    )
}
