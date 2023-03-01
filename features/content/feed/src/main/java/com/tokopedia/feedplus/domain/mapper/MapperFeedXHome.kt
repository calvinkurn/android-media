package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feedplus.data.FeedXHomeEntity
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignRestrictionModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaGradientModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaModel
import com.tokopedia.feedplus.presentation.model.FeedCardItemModel
import com.tokopedia.feedplus.presentation.model.FeedCardModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedCommentItemModel
import com.tokopedia.feedplus.presentation.model.FeedCommentModel
import com.tokopedia.feedplus.presentation.model.FeedFollowModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.model.FeedMediaModel
import com.tokopedia.feedplus.presentation.model.FeedMediaRatioModel
import com.tokopedia.feedplus.presentation.model.FeedMediaTagging
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.model.FeedPaginationModel
import com.tokopedia.feedplus.presentation.model.FeedScoreModel
import com.tokopedia.feedplus.presentation.model.FeedShareModel
import com.tokopedia.feedplus.presentation.model.FeedViewModel

/**
 * Created By : Muhammad Furqan on 01/03/23
 */
object MapperFeedHome {
    fun transform(data: FeedXHomeEntity): FeedModel = FeedModel(
        items = data.items.map { card ->
            FeedCardModel(
                id = card.id,
                mods = card.mods,
                typename = card.typename,
                type = card.type,
                author = card.author.let { author ->
                    FeedAuthorModel(
                        id = author.id,
                        type = author.type,
                        name = author.name,
                        description = author.description,
                        badgeUrl = author.badgeUrl,
                        logoUrl = author.logoUrl,
                        applink = author.applink,
                        encryptedUserId = author.encryptedUserId,
                        isLive = author.isLive,
                    )
                },
                title = card.title,
                subtitle = card.subtitle,
                text = card.text,
                cta = card.cta.let { cta ->
                    FeedCardCtaModel(
                        text = cta.text,
                        subtitle = cta.subtitle,
                        color = cta.color,
                        colorGradient = cta.colorGradient.map { color ->
                            FeedCardCtaGradientModel(
                                color = color.color, position = color.position
                            )
                        }.toList(),
                    )
                },
                ribbonImageUrl = card.ribbonImageUrl,
                applink = card.applink,
                weblink = card.weblink,
                applinkProductList = card.applinkProductList,
                weblinkProductList = card.weblinkProductList,
                actionButtonLabel = card.actionButtonLabel,
                products = card.products.map { product ->
                    FeedCardProductModel(
                        id = product.id,
                        name = product.name,
                        coverUrl = product.coverUrl,
                        weblink = product.weblink,
                        applink = product.applink,
                        star = product.star,
                        price = product.price,
                        priceFmt = product.priceFmt,
                        isDiscount = product.isDiscount,
                        discount = product.discount,
                        discountFmt = product.discountFmt,
                        priceOriginal = product.priceOriginal,
                        priceOriginalFmt = product.priceOriginalFmt,
                        priceDiscount = product.priceDiscount,
                        priceDiscountFmt = product.priceDiscountFmt,
                        totalSold = product.totalSold,
                        isBebasOngkir = product.isBebasOngkir,
                        bebasOngkirStatus = product.bebasOngkirStatus,
                        bebasOngkirUrl = product.bebasOngkirUrl,
                        shopId = product.shopId,
                        shopName = product.shopName,
                        priceMasked = product.priceMasked,
                        priceMaskedFmt = product.priceMaskedFmt,
                        stockWording = product.stockWording,
                        stockSoldPercentage = product.stockSoldPercentage,
                        cartable = product.cartable,
                        isCashback = product.isCashback,
                        cashbackFmt = product.cashbackFmt,
                    )
                }.toList(),
                totalProducts = card.totalProducts,
                campaign = card.campaign.let { campaign ->
                    FeedCardCampaignModel(
                        id = campaign.id,
                        status = campaign.status,
                        name = campaign.name,
                        shortName = campaign.shortName,
                        startTime = campaign.startTime,
                        endTime = campaign.endTime,
                        restrictions = campaign.restrictions.map { restriction ->
                            FeedCardCampaignRestrictionModel(
                                isActive = restriction.isActive, label = restriction.label
                            )
                        }.toList(),
                    )
                },
                hasVoucher = card.hasVoucher,
                actionButtonOperationWeb = card.actionButtonOperationWeb,
                actionButtonOperationApp = card.actionButtonOperationApp,
                media = card.media.map { media ->
                    FeedMediaModel(
                        type = media.type,
                        id = media.id,
                        coverUrl = media.coverUrl,
                        mediaUrl = media.mediaUrl,
                        applink = media.applink,
                        weblink = media.weblink,
                        tagging = media.tagging.map { tag ->
                            FeedMediaTagging(
                                tagIndex = tag.tagIndex, posX = tag.posX, posY = tag.posY
                            )
                        }.toList(),
                    )
                }.toList(),
                mediaRatio = card.mediaRatio.let { ratio ->
                    FeedMediaRatioModel(
                        width = ratio.width, height = ratio.height
                    )
                },
                tags = card.tags.map { tag ->
                    FeedCardProductModel(
                        id = tag.id,
                        name = tag.name,
                        coverUrl = tag.coverUrl,
                        weblink = tag.weblink,
                        applink = tag.applink,
                        star = tag.star,
                        price = tag.price,
                        priceFmt = tag.priceFmt,
                        isDiscount = tag.isDiscount,
                        discount = tag.discount,
                        discountFmt = tag.discountFmt,
                        priceOriginal = tag.priceOriginal,
                        priceOriginalFmt = tag.priceOriginalFmt,
                        priceDiscount = tag.priceDiscount,
                        priceDiscountFmt = tag.priceDiscountFmt,
                        totalSold = tag.totalSold,
                        isBebasOngkir = tag.isBebasOngkir,
                        bebasOngkirStatus = tag.bebasOngkirStatus,
                        bebasOngkirUrl = tag.bebasOngkirUrl,
                        shopId = tag.shopId,
                        shopName = tag.shopName,
                        priceMasked = tag.priceMasked,
                        priceMaskedFmt = tag.priceMaskedFmt,
                        stockWording = tag.stockWording,
                        stockSoldPercentage = tag.stockSoldPercentage,
                        cartable = tag.cartable,
                        isCashback = tag.isCashback,
                        cashbackFmt = tag.cashbackFmt,
                    )
                }.toList(),
                hashtagApplinkFmt = card.hashtagApplinkFmt,
                hashtagWeblinkFmt = card.hashtagWeblinkFmt,
                views = card.views.let { view ->
                    FeedViewModel(
                        label = view.label,
                        count = view.count,
                        countFmt = view.countFmt
                    )
                },
                like = card.like.let { like ->
                    FeedLikeModel(
                        label = like.label,
                        count = like.count,
                        countFmt = like.countFmt,
                        likedBy = like.likedBy,
                        isLiked = like.isLiked
                    )
                },
                comments = card.comments.let { comment ->
                    FeedCommentModel(
                        label = comment.label,
                        count = comment.count,
                        countFmt = comment.countFmt,
                        items = comment.items.map { item ->
                            FeedCommentItemModel(
                                id = item.id,
                                author = item.author.let { author ->
                                    FeedAuthorModel(
                                        id = author.id,
                                        type = author.type,
                                        name = author.name,
                                        description = author.description,
                                        badgeUrl = author.badgeUrl,
                                        logoUrl = author.logoUrl,
                                        applink = author.applink,
                                        encryptedUserId = author.encryptedUserId,
                                        isLive = author.isLive,
                                    )
                                },
                                text = item.text,
                            )
                        }.toList(),
                    )
                },
                share = card.share.let { share ->
                    FeedShareModel(
                        label = share.label, operation = share.operation
                    )
                },
                followers = card.followers.let { follow ->
                    FeedFollowModel(
                        label = follow.label,
                        count = follow.count,
                        countFmt = follow.countFmt,
                        isFollowed = follow.isFollowed,
                    )
                },
                reportable = card.reportable,
                editable = card.editable,
                deletable = card.deletable,
                playChannelId = card.playChannelId,
                detailScore = card.detailScore.map { score ->
                    FeedScoreModel(
                        label = score.label, value = score.value
                    )
                }.toList(),
                publishedAt = card.publishedAt,
                promos = card.promos,
                items = card.items.map { item ->
                    FeedCardItemModel(
                        id = item.id,
                        applink = item.applink,
                        weblink = item.weblink,
                        coverUrl = item.coverUrl,
                        product = item.product.let { product ->
                            FeedCardProductModel(
                                id = product.id,
                                name = product.name,
                                coverUrl = product.coverUrl,
                                weblink = product.weblink,
                                applink = product.applink,
                                star = product.star,
                                price = product.price,
                                priceFmt = product.priceFmt,
                                isDiscount = product.isDiscount,
                                discount = product.discount,
                                discountFmt = product.discountFmt,
                                priceOriginal = product.priceOriginal,
                                priceOriginalFmt = product.priceOriginalFmt,
                                priceDiscount = product.priceDiscount,
                                priceDiscountFmt = product.priceDiscountFmt,
                                totalSold = product.totalSold,
                                isBebasOngkir = product.isBebasOngkir,
                                bebasOngkirStatus = product.bebasOngkirStatus,
                                bebasOngkirUrl = product.bebasOngkirUrl,
                                shopId = product.shopId,
                                shopName = product.shopName,
                                priceMasked = product.priceMasked,
                                priceMaskedFmt = product.priceMaskedFmt,
                                stockWording = product.stockWording,
                                stockSoldPercentage = product.stockSoldPercentage,
                                cartable = product.cartable,
                                isCashback = product.isCashback,
                                cashbackFmt = product.cashbackFmt,
                            )
                        },
                    )
                }.toList(),
                maximumDiscountPercentage = card.maximumDiscountPercentage,
                maximumDiscountPercentageFmt = card.maximumDiscountPercentageFmt
            )
        }.toList(), pagination = FeedPaginationModel(
            cursor = data.pagination.cursor,
            hasNext = data.pagination.hasNext,
            totalData = data.pagination.totalData
        )
    )
}
