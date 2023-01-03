package com.tokopedia.kol.feature.postdetail.data.mapper

import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXAuthor
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXComments
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCommentsItem
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXFollowers
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXLike
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMediaRatio
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXShare
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXViews
import com.tokopedia.feedcomponent.domain.model.UserFeedPostsModel
import com.tokopedia.kol.feature.postdetail.domain.mapper.ContentDetailMapper
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction

/**
 * Created by meyta.taliti on 03/08/22.
 */
class ContentDetailMapperImpl : ContentDetailMapper {

    override fun mapContent(contents: List<FeedXCard>, cursor: String) = ContentDetailUiModel(
        postList = contents,
        cursor = cursor
    )

    override fun mapFeedPosts(response: UserFeedPostsModel): ContentDetailUiModel {
        with(response.feedXProfileGetProfilePosts) {
            return ContentDetailUiModel(
                postList = posts.map {
                    FeedXCard(
                        typename = it.typename,
                        id = it.id,
                        publishedAt = it.publishedAt,
                        reportable = it.reportable,
                        editable = it.editable,
                        deletable = it.deletable,
                        mods = it.mods,
                        author = FeedXAuthor(
                            appLink = it.author.appLink,
                            badgeURL = it.author.badgeURL,
                            description = it.author.description,
                            id = it.author.id,
                            logoURL = it.author.logoURL,
                            name = it.author.name,
                            type = it.author.type,
                            webLink = it.author.webLink,
                        ),
                        subTitle = it.subTitle,
                        text = it.text,
                        title = it.title,
                        comments = FeedXComments(
                            label = it.comment.label,
                            count = it.comment.count,
                            countFmt = it.comment.countFmt,
                            commentItems = it.comment.items.map { commentList ->
                                FeedXCommentsItem(
                                    id = commentList.id,
                                    text = commentList.text,
                                    author = FeedXAuthor(
                                        appLink = commentList.author.appLink,
                                        badgeURL = commentList.author.badgeURL,
                                        description = commentList.author.description,
                                        id = commentList.author.id,
                                        logoURL = commentList.author.logoURL,
                                        name = commentList.author.name,
                                        type = commentList.author.type,
                                        webLink = commentList.author.webLink,
                                    )
                                )
                            },
                            mods = it.comment.mods,
                        ),
                        like = FeedXLike(
                            label = it.like.label,
                            countFmt = it.like.countFmt,
                            count = it.like.count,
                            likedBy = it.like.likedBy,
                            isLiked = it.like.isLiked,
                            mods = it.like.mods,
                        ),
                        appLink = it.appLink,
                        share = FeedXShare(
                            label = it.share.label,
                            operation = it.share.operation,
                            mods = it.share.mods,
                        ),
                        followers = FeedXFollowers(
                            isFollowed = it.followers.isFollowed,
                            label = it.followers.label,
                            count = it.followers.count,
                            countFmt = it.followers.countFmt,
                            mods = it.followers.mods,
                        ),
                        webLink = it.webLink,
                        views = FeedXViews(
                            label = it.views.label,
                            countFmt = it.views.countFmt,
                            count = it.views.count,
                            mods = it.views.mods,
                        ),
                        mediaRatio = FeedXMediaRatio(
                            width = it.mediaRatio.width,
                            height = it.mediaRatio.height,
                        ),
                        hashtagAppLinkFmt = it.hashtagAppLinkFmt,
                        hashtagWebLinkFmt = it.hashtagWebLinkFmt,
                        actionButtonLabel = it.actionButtonLabel,
                        actionButtonOperationApp = it.actionButtonOperationApp,
                        actionButtonOperationWeb = it.actionButtonOperationWeb,
                        media = it.media.map { media ->
                            FeedXMedia(
                                id = media.id,
                                type = media.type,
                                appLink = media.appLink,
                                webLink = media.webLink,
                                coverUrl = media.coverURL,
                                mediaUrl = media.mediaURL,
                                tagging = media.tagging.map { tagging ->
                                    FeedXMediaTagging(
                                        tagIndex = tagging.tagIndex,
                                        posX = tagging.posX,
                                        posY = tagging.posY,
                                        mediaIndex = 0,
                                    )
                                },
                                mods = media.mods,
                            )
                        },
                        tags = it.tags.map { tag ->
                            FeedXProduct(
                                appLink = tag.appLink,
                                bebasOngkirStatus = tag.bebasOngkirStatus,
                                bebasOngkirURL = tag.bebasOngkirURL,
                                coverURL = tag.coverURL,
                                discount = tag.discount,
                                discountFmt = tag.discountFmt,
                                isCashback = tag.isCashback,
                                cashbackFmt = tag.cashbackFmt,
                                id = tag.id,
                                isBebasOngkir = tag.isBebasOngkir,
                                isDiscount = tag.isDiscount,
                                mods = tag.mods,
                                name = tag.name,
                                price = tag.price.toInt(),
                                priceDiscount = tag.priceDiscount.toInt(),
                                priceDiscountFmt = tag.priceDiscountFmt,
                                priceFmt = tag.priceFmt,
                                priceOriginal = tag.priceOriginal.toInt(),
                                priceOriginalFmt = tag.priceOriginalFmt,
                                star = tag.star,
                                totalSold = tag.totalSold,
                                webLink = tag.webLink,
                                productName = tag.name,
                                shopName = tag.shopName,
                                shopID = tag.shopID,
                            )
                        }
                    )
                },
                cursor = pagination.cursor,
            )
        }
    }

    override fun mapLikeContent(rowNumber: Int, action: ContentLikeAction) = LikeContentModel(
        rowNumber = rowNumber,
        action = action
    )

    override fun mapShopFollow(
        rowNumber: Int,
        action: ShopFollowAction,
        isFollowedFromRSRestrictionBottomSheet: Boolean
    ): ShopFollowModel = ShopFollowModel(
        rowNumber = rowNumber,
        action = action,
        isFollowedFromRSRestrictionBottomSheet = isFollowedFromRSRestrictionBottomSheet
    )

    override fun mapDeleteContent(rowNumber: Int) = DeleteContentModel(rowNumber)

    override fun mapReportContent(rowNumber: Int) = ReportContentModel(rowNumber)

    override fun mapVisitChannel(rowNumber: Int) = VisitContentModel(rowNumber)

    override fun mapWishlistData(rowNumber: Int, productId: String) =
        WishlistContentModel(rowNumber = rowNumber, productId = productId)

}
