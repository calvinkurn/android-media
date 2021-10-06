package com.tokopedia.feedcomponent.view.mapper

import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.widget.TopAdsBannerView

object TopadsFeedXMapper {
    fun cpmModelToFeedXDataModel(impressHolder: ImpressHolder, cpmData: CpmModel , variant:Int): FeedXCard {
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

        val image = "https://ecs7.tokopedia.net/img/cache/300/default_picture_user/default_toped-25.jpg"
        val listOf = arrayListOf("medias:layout_single")
        val feedXAuthor = FeedXAuthor(
            appLink = data.applinks,
            badgeURL = data.cpm.badges[0].imageUrl,
            description = data.cpm.decription,
            id = data.id,
            logoURL = data.cpm.cpmImage.fullEcs,
            name = TopAdsBannerView.escapeHTML(data.cpm.cpmShop.name),
            type = 2,
            webLink = data.applinks
        )

        val feedXCardDataItem =
            FeedXCardDataItem(
                id = cpmData.data[0].id,
                appLink = data.applinks,
                webLink = data.applinks,
                coverUrl = image,
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
            typename = "FeedXCardPost",
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
            text = data.cpm.promotedText,
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
            shopId = data.cpm.cpmShop.id
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
                mediaUrl = image.m_url.replaceFirst("images","ecs7"),
                tagging = arrayListOf(FeedXMediaTagging(index,0.5f,0.5f)),
                isImageImpressedFirst = false,
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
        return FeedXMediaTagging(index,0.5f, 0.5f)
    }

    private fun cpmProductToFeedXProduct(
        product: Product,
        merchantVoucher: ArrayList<String>
    ): FeedXProduct {
        var cashback=""
        if (!merchantVoucher.isNullOrEmpty()){
            cashback=merchantVoucher[0]
        }
        product.run {
            return FeedXProduct(
                appLink = applinks,
                discount = product.campaign.discountPercentage,
                cashbackFmt = cashback,
                isBebasOngkir = freeOngkir.isActive,
                isCashback = isProductCashback,
                bebasOngkirURL = freeOngkir.imageUrl,
                name = name,
                priceOriginalFmt = priceFormat,
                priceFmt = priceFormat,
                isDiscount = false,
                coverURL = imageProduct.imageUrl,
                id = id,
                webLink = applinks
            )
        }
    }
}