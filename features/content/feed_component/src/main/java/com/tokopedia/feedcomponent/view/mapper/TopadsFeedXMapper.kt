package com.tokopedia.feedcomponent.view.mapper

import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.domain.mapper.TYPE_TOPADS_HEADLINE_NEW
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.widget.TopAdsBannerView

object TopadsFeedXMapper {
    var authorName = ""
    fun cpmModelToFeedXDataModel(impressHolder: ImpressHolder, cpmData: CpmModel , variant:Int=0): FeedXCard {
        val media = arrayListOf<FeedXMedia>()
        val data = cpmData.data[0]
        val merchantVouchers = data.cpm.cpmShop.merchantVouchers

        val feedXTagging = arrayListOf<FeedXMediaTagging>()
        cpmData.data[0].cpm.cpmShop.products.forEachIndexed { index, _ ->
            feedXTagging.add(getFeedxMediaTagging(index))
        }

        cpmData.data[0].cpm.cpmShop.products.forEachIndexed { index, product ->
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
        cpmData.data[0].cpm.cpmShop.products.forEach {
            feedXProducts.add(cpmProductToFeedXProduct(it, merchantVouchers as ArrayList<String>))
        }

        val listOf = arrayListOf("medias:layout_single")
        authorName = TopAdsBannerView.escapeHTML(data.cpm.cpmShop.name)
        val feedXAuthor = FeedXAuthor(
            appLink = data.applinks,
            badgeURL = data.cpm.badges[0].imageUrl,
            description = data.cpm.decription,
            id = data.id,
            logoURL = data.cpm.cpmImage.fullEcs,
            name = authorName,
            type = 2,
            webLink = data.applinks
        )

        val feedXCardDataItem =
            FeedXCardDataItem(
                id = cpmData.data[0].id,
                appLink = data.applinks,
                webLink = data.applinks,
                coverUrl = data.cpm.cpmImage.fullUrl,
                mods = listOf,
                products = feedXProducts
            )

        val share = FeedXShare("Share", "", listOf())
        val followers = FeedXFollowers(
            isFollowed = cpmData.data[0].cpm.cpmShop.isFollowed,
            mods = listOf(),
            transitionFollow = cpmData.data[0].cpm.cpmShop.isFollowed
        )



        return FeedXCard(
            typename = TYPE_TOPADS_HEADLINE_NEW,
            id = cpmData.data[0].id,
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
            subTitle = data.cpm.decription,
            text = data.cpm.cpmShop.slogan,
            title = data.cpm.name,
            like = FeedXLike(),
            comments = FeedXComments(),
            share = share,
            followers = followers,
            appLink = data.applinks,
            webLink = data.applinks,
            media = media,
            tags = feedXProducts,
            impressHolder = impressHolder,
            isTopAds = true,
            adId = data.adClickUrl,
            shopId = data.cpm.cpmShop.id,
            adClickUrl = data.adClickUrl,
            adViewUrl = data.cpm.cpmImage.fullUrl,
            cpmData = data,
            listProduct = data.cpm.cpmShop.products
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
                mediaUrl = image.m_url,
                tagging = arrayListOf(FeedXMediaTagging(index,0.5f,0.44f,mediaIndex = index)),
                isImageImpressedFirst = true,
                productName = name,
                price = product.priceFormat,
                slashedPrice = product.campaign.originalPrice,
                discountPercentage = if (product.campaign.discountPercentage != 0) "${product.campaign.discountPercentage}%" else "",
                isCashback = isProductCashback,
                variant = variant,
                cashBackFmt = cashback,
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
                priceOriginalFmt = campaign.originalPrice,
                priceFmt = priceFormat,
                isDiscount = isDiscount,
                coverURL = imageProduct.imageUrl,
                id = id,
                webLink = applinks,
                authorName = authorName,
                isTopads = true,
                adClickUrl = imageProduct.imageClickUrl,
                star = productRating,
                totalSold = countSold.toIntOrNull()?:0,
                discountFmt = if (product.campaign.discountPercentage != 0) "${product.campaign.discountPercentage}%" else "",
                priceDiscountFmt = priceFormat
                    )
        }
    }
}