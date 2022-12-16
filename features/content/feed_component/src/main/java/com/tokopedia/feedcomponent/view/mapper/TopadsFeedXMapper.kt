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
        val media = arrayListOf<FeedXMedia>()
        val data = cpmData.data.firstOrNull()
        shopName = data?.cpm?.cpmShop?.domain ?: ""
        redirectWeblinkShop = data?.redirect ?: ""
        val merchantVouchers = data?.cpm?.cpmShop?.merchantVouchers

        val feedXTagging = arrayListOf<FeedXMediaTagging>()
        cpmData.data.firstOrNull()?.cpm?.cpmShop?.products?.forEachIndexed { index, _ ->
            feedXTagging.add(getFeedxMediaTagging(index))
        }

        cpmData.data.firstOrNull()?.cpm?.cpmShop?.products?.forEachIndexed { index, product ->
            media.add(
                cpmProductToFeedXMedia(
                    product,
                    variant,
                    merchantVouchers as ArrayList<String>,
                    index
                )
            )
        }

        val feedXProducts = arrayListOf<FeedXProduct>()
        cpmData.data.firstOrNull()?.cpm?.cpmShop?.products?.forEach {
            feedXProducts.add(cpmProductToFeedXProduct(it, merchantVouchers as ArrayList<String>))
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
                products = feedXProducts
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
            products = feedXProducts,
            subTitle = data?.cpm?.decription ?: "",
            text = data?.cpm?.cpmShop?.slogan ?: "",
            title = data?.cpm?.name ?: "",
            like = FeedXLike(),
            comments = FeedXComments(),
            share = share,
            followers = followers,
            appLink = data?.applinks ?: "",
            webLink = redirectWeblinkShop,
            media = media,
            tags = feedXProducts,
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

    private fun cpmProductToFeedXMedia(product: Product,variant: Int, merchantVoucher: ArrayList<String>,index: Int): FeedXMedia {
        var cashback=""
        if (!merchantVoucher.isNullOrEmpty()){
            cashback=merchantVoucher[0]
        }
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
                cashBackFmt = cashback
            )
        }
    }

    private fun getFeedxMediaTagging(index:Int):FeedXMediaTagging{
        return FeedXMediaTagging(index,0.5f, 0.44f,mediaIndex = index)
    }

    private fun cpmProductToFeedXProduct(
        product: Product,
        merchantVoucher: ArrayList<String>
    ): FeedXProduct {
        var cashback=""
        if (!merchantVoucher.isNullOrEmpty()){
            cashback=merchantVoucher[0]
        }
        val isDiscount = product.campaign.discountPercentage > 0

        product.run {
            return FeedXProduct(
                appLink = applinks,
                discount = campaign.discountPercentage,
                cashbackFmt = cashback,
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
                shopName = shopName
                )
        }
    }
}
