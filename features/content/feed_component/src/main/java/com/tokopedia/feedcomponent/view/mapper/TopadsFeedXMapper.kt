package com.tokopedia.feedcomponent.view.mapper

import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.domain.mapper.TYPE_TOPADS_HEADLINE_NEW
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.widget.TopAdsBannerView

object TopadsFeedXMapper {
    var authorName = ""
    var shopName = ""
    var redirectWeblinkShop = ""
    fun cpmModelToFeedXDataModel(impressHolder: ImpressHolder, cpmData: CpmModel , variant:Int=0): FeedXCard {
        val data = cpmData.data.firstOrNull()
        val topAdsShop = data?.cpm?.cpmShop
        shopName = topAdsShop?.domain.orEmpty()
        redirectWeblinkShop = data?.redirect ?: ""
        val merchantVouchers = topAdsShop?.merchantVouchers.orEmpty()

        val media = topAdsShop?.products?.mapIndexed { index, product ->
            cpmProductToFeedXMedia(
                product,
                variant,
                merchantVouchers,
                index
            )
        }

        val feedXProducts = topAdsShop?.products?.map { product ->
            cpmProductToFeedXProduct(product, merchantVouchers, topAdsShop.id)
        }

        val listOf = arrayListOf("medias:layout_single")
        authorName = TopAdsBannerView.escapeHTML(data?.cpm?.cpmShop?.name ?: "")
        val feedXAuthor = FeedXAuthor(
            appLink = data?.applinks ?: "",
            badgeURL = data?.cpm?.badges?.firstOrNull()?.imageUrl ?: "",
            description = data?.cpm?.decription ?: "",
            id = data?.cpm?.cpmShop?.id ?: "",
            logoURL = data?.cpm?.cpmImage?.fullEcs ?: "",
            name = authorName,
            type = 2,
            webLink = data?.applinks ?: ""
        )

        val feedXCardDataItem =
            FeedXCardDataItem(
                id = cpmData.data.firstOrNull()?.id ?: "",
                appLink = data?.applinks ?: "",
                webLink = data?.redirect ?: "",
                coverUrl = data?.cpm?.cpmImage?.fullUrl ?: "",
                mods = listOf,
                products = feedXProducts.orEmpty()
            )

        val share = FeedXShare("Share", "", listOf())
        val followers = FeedXFollowers(
            isFollowed = cpmData.data.firstOrNull()?.cpm?.cpmShop?.isFollowed ?: false,
            mods = listOf(),
            transitionFollow = cpmData.data.firstOrNull()?.cpm?.cpmShop?.isFollowed ?: false
        )

        return FeedXCard(
            typename = TYPE_TOPADS_HEADLINE_NEW,
            id = cpmData.data.firstOrNull()?.id ?: "",
            publishedAt = "",
            reportable = true,
            editable = false,
            deletable = false,
            mods = listOf("medias:layout_single"),
            promos = listOf(),
            author = feedXAuthor,
            items = arrayListOf(feedXCardDataItem),
            type = "",
            products = feedXProducts.orEmpty(),
            subTitle = data?.cpm?.decription ?: "",
            text = data?.cpm?.cpmShop?.slogan ?: "",
            title = data?.cpm?.name ?: "",
            like = FeedXLike(),
            comments = FeedXComments(),
            share = share,
            followers = followers,
            appLink = data?.applinks ?: "",
            webLink = redirectWeblinkShop,
            media = media.orEmpty(),
            tags = feedXProducts.orEmpty(),
            impressHolder = impressHolder,
            isTopAds = true,
            adId = data?.adClickUrl ?: "",
            shopId = data?.cpm?.cpmShop?.id ?: "",
            adClickUrl = data?.adClickUrl ?: "",
            adViewUrl = data?.cpm?.cpmImage?.fullUrl ?: "",
            cpmData = data ?: CpmData(),
            listProduct = data?.cpm?.cpmShop?.products ?: listOf()
        )
    }

    private fun cpmProductToFeedXMedia(
        product: Product,
        variant: Int,
        merchantVoucher: List<String>,
        index: Int
    ): FeedXMedia {
        product.run {
            return FeedXMedia(
                id = id,
                type = "image",
                appLink = applinks,
                webLink = uri,
                mediaUrl = image.m_url,
                tagging = arrayListOf(FeedXMediaTagging(index, 0.5f, 0.44f, mediaIndex = index)),
                isImageImpressedFirst = true,
                productName = name,
                price = product.priceFormat,
                slashedPrice = product.campaign.originalPrice,
                discountPercentage = if (product.campaign.discountPercentage != 0) "${product.campaign.discountPercentage}%" else "",
                isCashback = isProductCashback,
                variant = variant,
                cashBackFmt = merchantVoucher.firstOrNull().orEmpty()
            )
        }
    }

    private fun getFeedxMediaTagging(index:Int):FeedXMediaTagging{
        return FeedXMediaTagging(index,0.5f, 0.44f,mediaIndex = index)
    }

    private fun cpmProductToFeedXProduct(
        product: Product,
        merchantVoucher: List<String>,
        shopId: String,
    ): FeedXProduct {
        val isDiscount = product.campaign.discountPercentage > 0
        product.run {
            return FeedXProduct(
                appLink = applinks,
                discount = campaign.discountPercentage,
                cashbackFmt = merchantVoucher.firstOrNull().orEmpty(),
                isBebasOngkir = freeOngkir.isActive,
                isCashback = isProductCashback,
                bebasOngkirURL = freeOngkir.imageUrl,
                name = name,
                priceOriginalFmt = campaign.originalPrice.replace(" ",""),
                priceFmt = priceFormat.replace(" ",""),
                isDiscount = isDiscount,
                coverURL = imageProduct.imageUrl,
                id = id,
                webLink = uri,
                authorName = authorName,
                isTopads = true,
                adClickUrl = imageProduct.imageClickUrl,
                star = productRating,
                totalSold = countSold.toIntOrZero(),
                discountFmt = if (product.campaign.discountPercentage != 0) "${product.campaign.discountPercentage}%" else "",
                priceDiscountFmt = priceFormat.replace(" ",""),
                shopName = shopName,
                shopID = shopId
            )
        }
    }
}
