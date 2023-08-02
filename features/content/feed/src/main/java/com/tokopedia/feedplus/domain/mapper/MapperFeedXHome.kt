package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.report_content.model.FeedContentData
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.FeedXAuthor
import com.tokopedia.feedplus.data.FeedXCampaign
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_PLAY_LIVE
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_TOP_ADS
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_PLACEHOLDER
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_POST
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_MEDIA_VIDEO
import com.tokopedia.feedplus.data.FeedXComments
import com.tokopedia.feedplus.data.FeedXFollow
import com.tokopedia.feedplus.data.FeedXHomeEntity
import com.tokopedia.feedplus.data.FeedXLike
import com.tokopedia.feedplus.data.FeedXMedia
import com.tokopedia.feedplus.data.FeedXProduct
import com.tokopedia.feedplus.data.FeedXScore
import com.tokopedia.feedplus.data.FeedXView
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignRestrictionModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaGradientModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaModel
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductAffiliate
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedCommentItemModel
import com.tokopedia.feedplus.presentation.model.FeedCommentModel
import com.tokopedia.feedplus.presentation.model.FeedFollowModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.model.FeedMediaModel
import com.tokopedia.feedplus.presentation.model.FeedMediaTagging
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.model.FeedPaginationModel
import com.tokopedia.feedplus.presentation.model.FeedScoreModel
import com.tokopedia.feedplus.presentation.model.FeedShareModel
import com.tokopedia.feedplus.presentation.model.FeedViewModel
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.content.common.R as contentCommonR

/**
 * Created By : Muhammad Furqan on 01/03/23
 */
class MapperFeedXHome @Inject constructor(
    private val userSession: UserSessionInterface
) {
    fun transform(data: FeedXHomeEntity): FeedModel =
        FeedModel(
            items = data.items.filter { shouldShow(it) }.map { card ->
                if (isImagesPost(card)) {
                    transformToFeedCardImage(card)
                } else if (isVideoPost(card)) {
                    transformToFeedCardVideo(card)
                } else if (isLivePreviewPost(card)) {
                    transformToFeedCardLivePreview(card)
                } else {
                    transformToFeedCardImage(card)
                }
            }.toMutableList(),
            pagination = FeedPaginationModel(
                cursor = data.pagination.cursor,
                hasNext = data.pagination.hasNext,
                totalData = data.pagination.totalData
            )
        )

    private fun transformToFeedCardImage(card: FeedXCard): FeedCardImageContentModel {
        val author = transformAuthor(card.author)
        val medias = card.media.map(::transformMedia)
        return FeedCardImageContentModel(
            id = card.id,
            typename = card.typename,
            type = card.type,
            author = author,
            title = card.title,
            subtitle = card.subtitle,
            text = card.text,
            cta = card.cta.let { cta ->
                FeedCardCtaModel(
                    texts = cta.texts,
                    color = cta.color,
                    colorGradient = cta.colorGradient.map { color ->
                        FeedCardCtaGradientModel(
                            color = color.color,
                            position = color.position
                        )
                    }
                )
            },
            ribbonImageUrl = card.ribbonImageUrl,
            applink = card.applink,
            weblink = card.weblink,
            applinkProductList = card.applinkProductList,
            actionButtonLabel = card.actionButtonLabel,
            products = if (card.products.isNotEmpty()) {
                card.products.map { product ->
                    transformProduct(product)
                }
            } else {
                card.tags.map { product -> transformProduct(product) }
            },
            totalProducts = card.totalProducts,
            campaign = transformCampaign(card.campaign),
            hasVoucher = card.hasVoucher,
            media = medias,
            hashtagApplinkFmt = card.hashtagApplinkFmt,
            hashtagWeblinkFmt = card.hashtagWeblinkFmt,
            views = transformView(card.views),
            like = transformLike(card.like),
            comments = transformComment(card.comments),
            share = transformShare(
                contentId = card.id,
                author = author,
                appLink = card.applink,
                webLink = card.weblink,
                imageUrl = medias.firstOrNull()?.mediaUrl.orEmpty()
            ),
            followers = transformFollow(card.followers),
            menuItems = getMenuItems(author, card),
            detailScore = card.detailScore.map { score -> transformDetailScore(score) },
            publishedAt = card.publishedAt,
            maxDiscountPercentage = card.maximumDiscountPercentage,
            maxDiscountPercentageFmt = card.maximumDiscountPercentageFmt,
            topAdsId = if (isTopAdsPost(card)) card.id else "",
        )
    }

    private fun transformToFeedCardVideo(card: FeedXCard): FeedCardVideoContentModel {
        val author = transformAuthor(card.author)
        val medias = card.media.map(::transformMedia)
        return FeedCardVideoContentModel(
            id = card.id,
            typename = card.typename,
            type = card.type,
            author = author,
            title = card.title,
            subtitle = card.subtitle,
            text = card.text,
            cta = card.cta.let { cta ->
                FeedCardCtaModel(
                    color = cta.color,
                    texts = cta.texts,
                    colorGradient = cta.colorGradient.map { color ->
                        FeedCardCtaGradientModel(
                            color = color.color,
                            position = color.position
                        )
                    }
                )
            },
            applink = card.applink,
            weblink = card.weblink,
            actionButtonLabel = card.actionButtonLabel,
            campaign = transformCampaign(card.campaign),
            hasVoucher = card.hasVoucher,
            products = if (card.products.isNotEmpty()) {
                card.products.map { product ->
                    transformProduct(product)
                }
            } else {
                card.tags.map { product -> transformProduct(product) }
            },
            totalProducts = card.totalProducts,
            media = medias,
            hashtagApplinkFmt = card.hashtagApplinkFmt,
            hashtagWeblinkFmt = card.hashtagWeblinkFmt,
            views = transformView(card.views),
            like = transformLike(card.like),
            comments = transformComment(card.comments),
            share = transformShare(
                contentId = card.id,
                author = author,
                appLink = card.applink,
                webLink = card.weblink,
                imageUrl = medias.firstOrNull()?.coverUrl.orEmpty()
            ),
            followers = transformFollow(card.followers),
            menuItems = getMenuItems(author, card),
            detailScore = card.detailScore.map { score -> transformDetailScore(score) },
            publishedAt = card.publishedAt,
            playChannelId = card.playChannelId
        )
    }

    private fun transformToFeedCardLivePreview(card: FeedXCard): FeedCardLivePreviewContentModel =
        FeedCardLivePreviewContentModel(
            id = card.id,
            typename = card.typename,
            type = card.type,
            author = transformAuthor(card.author),
            title = card.title,
            subtitle = card.subtitle,
            text = card.text,
            applink = card.applink,
            media = card.media.map { media -> transformMedia(media) },
            hashtagApplinkFmt = card.hashtagApplinkFmt,
            hashtagWeblinkFmt = card.hashtagWeblinkFmt,
            playChannelId = card.playChannelId,
            followers = transformFollow(card.followers),
            detailScore = card.detailScore.map { score -> transformDetailScore(score) },
            hasVoucher = card.hasVoucher,
            campaign = transformCampaign(card.campaign),
            products = if (card.products.isNotEmpty()) {
                card.products.map { product ->
                    transformProduct(product)
                }
            } else {
                card.tags.map { product -> transformProduct(product) }
            }
        )

    private fun transformAuthor(author: FeedXAuthor): FeedAuthorModel = FeedAuthorModel(
        id = author.id,
        type = AuthorType.from(author.type),
        name = MethodChecker.fromHtml(author.name).toString(),
        badgeUrl = author.badgeUrl,
        logoUrl = author.logoUrl,
        appLink = author.applink,
        encryptedUserId = author.encryptedUserId,
        isLive = author.isLive
    )

    private fun transformProduct(product: FeedXProduct): FeedCardProductModel =
        FeedCardProductModel(
            id = product.id,
            isParent = product.isParent,
            parentID = product.parentID,
            hasVariant = product.hasVariant,
            name = product.name,
            coverUrl = product.coverUrl,
            weblink = product.weblink,
            applink = product.applink,
            affiliate = product.affiliate.let {
                FeedCardProductAffiliate(
                    it.id,
                    it.channel
                )
            },
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
            isAvailable = product.isAvailable,
        )

    private fun transformMedia(media: FeedXMedia): FeedMediaModel =
        FeedMediaModel(
            type = media.type,
            id = media.id,
            coverUrl = media.coverUrl,
            mediaUrl = media.mediaUrl,
            appLink = media.applink,
            tagging = media.tagging.map { tag ->
                FeedMediaTagging(
                    tagIndex = tag.tagIndex
                )
            }
        )

    private fun transformCampaign(campaign: FeedXCampaign): FeedCardCampaignModel =
        FeedCardCampaignModel(
            id = campaign.id,
            status = campaign.status,
            name = campaign.name,
            shortName = campaign.shortName,
            startTime = campaign.startTime,
            endTime = campaign.endTime,
            restrictions = campaign.restrictions.map { restriction ->
                FeedCardCampaignRestrictionModel(
                    isActive = restriction.isActive,
                    label = restriction.label
                )
            }
        )

    private fun transformView(view: FeedXView): FeedViewModel = FeedViewModel(
        label = view.label,
        count = view.count,
        countFmt = view.countFmt
    )

    private fun transformLike(like: FeedXLike): FeedLikeModel = FeedLikeModel(
        label = like.label,
        count = like.count,
        countFmt = like.countFmt,
        likedBy = like.likedBy,
        isLiked = like.isLiked
    )

    private fun transformComment(comment: FeedXComments): FeedCommentModel =
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
                            type = AuthorType.from(author.type),
                            name = author.name,
                            badgeUrl = author.badgeUrl,
                            logoUrl = author.logoUrl,
                            appLink = author.applink,
                            encryptedUserId = author.encryptedUserId,
                            isLive = author.isLive
                        )
                    },
                    text = item.text
                )
            }
        )

    private fun transformShare(
        contentId: String,
        author: FeedAuthorModel,
        appLink: String,
        webLink: String,
        imageUrl: String
    ) = FeedShareModel(
        contentId,
        author,
        appLink,
        webLink,
        imageUrl
    )

    private fun transformFollow(follow: FeedXFollow): FeedFollowModel = FeedFollowModel(
        label = follow.label,
        count = follow.count,
        countFmt = follow.countFmt,
        isFollowed = follow.isFollowed
    )

    private fun transformDetailScore(score: FeedXScore): FeedScoreModel = FeedScoreModel(
        label = score.label,
        value = score.value
    )

    private fun isReportable(card: FeedXCard): Boolean {
        return if (card.typename == TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT) {
            return card.author.id != userSession.shopId
        } else {
            card.reportable
        }
    }

    private fun isMyContent(author: FeedAuthorModel): Boolean {
        return (author.type.isShop && author.id == userSession.shopId) ||
            (author.type.isUser && author.id == userSession.userId)
    }

    private fun getMenuItems(author: FeedAuthorModel, card: FeedXCard): List<FeedMenuItem> {
        val contentData = FeedContentData(
            card.text,
            card.id,
            card.author.id
        )
        return buildList {
            if (isMyContent(author)) {
                if (card.performanceSummaryPageLink.isNotBlank()) {
                    add(
                        FeedMenuItem(
                            iconUnify = IconUnify.GRAPH,
                            name = contentCommonR.string.performance_see,
                            type = FeedMenuIdentifier.SeePerformance,
                            appLink = card.performanceSummaryPageLink,
                            contentData = contentData
                        )
                    )
                }
                if (card.insightSummaryPageLink.isNotBlank()) {
                    add(
                        FeedMenuItem(
                            iconUnify = IconUnify.GRAPH_REPORT,
                            name = contentCommonR.string.performance_learn_video_insight,
                            type = FeedMenuIdentifier.LearnVideoInsight,
                            appLink = card.insightSummaryPageLink,
                            contentData = contentData
                        )
                    )
                }
            }
            add(
                FeedMenuItem(
                    iconUnify = IconUnify.VISIBILITY,
                    name = R.string.feed_watch_mode,
                    type = FeedMenuIdentifier.WatchMode,
                    contentData = contentData
                )
            )
            if (isReportable(card)) {
                add(
                    FeedMenuItem(
                        iconUnify = IconUnify.WARNING,
                        name = contentCommonR.string.content_common_menu_report,
                        type = FeedMenuIdentifier.Report,
                        contentData = contentData
                    )
                )
            }
            if (card.deletable) {
                add(
                    FeedMenuItem(
                        iconUnify = IconUnify.DELETE,
                        name = contentCommonR.string.content_common_menu_delete,
                        type = FeedMenuIdentifier.Delete,
                        contentData = contentData
                    )
                )
            }
        }
    }

    private fun isImagesPost(card: FeedXCard): Boolean {
        return (
            (card.typename == TYPE_FEED_X_CARD_POST) ||
                (card.typename == TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT) ||
                isTopAdsPost(card)
            ) &&
            card.media.none { it.type.contains(TYPE_MEDIA_VIDEO) }
    }

    private fun isVideoPost(card: FeedXCard): Boolean {
        val isVideo = card.media.isNotEmpty() && card.media.first().type.contains(TYPE_MEDIA_VIDEO)
        return ((card.typename == TYPE_FEED_X_CARD_POST) || (card.typename == TYPE_FEED_X_CARD_PLAY)) &&
            card.type != TYPE_FEED_PLAY_LIVE && isVideo
    }

    private fun isLivePreviewPost(card: FeedXCard) =
        card.typename == TYPE_FEED_X_CARD_PLAY && card.type == TYPE_FEED_PLAY_LIVE &&
            card.media.isNotEmpty() && card.media[0].type == TYPE_MEDIA_VIDEO

    private fun isTopAdsPost(card: FeedXCard) =
        card.typename == TYPE_FEED_X_CARD_PLACEHOLDER && card.type == TYPE_FEED_TOP_ADS

    private fun shouldShow(card: FeedXCard) =
        isImagesPost(card) || isVideoPost(card) || isLivePreviewPost(card)
}
