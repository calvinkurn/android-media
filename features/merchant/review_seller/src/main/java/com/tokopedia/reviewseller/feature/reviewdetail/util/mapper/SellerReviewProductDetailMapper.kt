package com.tokopedia.reviewseller.feature.reviewdetail.util.mapper

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSessionInterface

object SellerReviewProductDetailMapper {

    fun mapToProductFeedbackDetailUiModel(productFeedbackDataPerProduct:
                                          ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct,
                                          userSession: UserSessionInterface): ProductFeedbackDetailUiModel {
        return ProductFeedbackDetailUiModel().apply {
            ratingBarList = mapToRatingBarUiModel(productFeedbackDataPerProduct)
            productFeedbackDetailList = mapToFeedbackUiModel(productFeedbackDataPerProduct, userSession)
            topicList = mapToTopicUiModel(productFeedbackDataPerProduct)
        }
    }

    private fun mapToRatingBarUiModel(productFeedbackDataPerProduct: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct): List<RatingBarUiModel> {
        val ratingBarListUiModel = mutableListOf<RatingBarUiModel>()

        productFeedbackDataPerProduct.aggregatedRating.map {
            ratingBarListUiModel.add(
                    RatingBarUiModel(
                            ratingLabel = it.rating,
                            ratingCount = it.ratingCount
                    )
            )
        }
        return ratingBarListUiModel
    }

    private fun mapToTopicUiModel(productFeedbackDataPerProduct: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct): TopicUiModel {
        val topicListUiModel = TopicUiModel()

        productFeedbackDataPerProduct.topics.map {
            topicListUiModel.apply {
                sortFilterItemList = mapToItemSortFilter(productFeedbackDataPerProduct)
                countFeedback = productFeedbackDataPerProduct.list.size
            }
        }
        return topicListUiModel
    }

    private fun mapToFeedbackUiModel(productFeedbackDataPerProduct: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct,
                                     userSession: UserSessionInterface): List<FeedbackUiModel> {
        val feedbackListUiModel = mutableListOf<FeedbackUiModel>()

        productFeedbackDataPerProduct.list.map {
            val mapAttachment = mutableListOf<FeedbackUiModel.Attachment>()
            it.attachments.map { attachment ->
                mapAttachment.add(FeedbackUiModel.Attachment(
                        thumbnailURL = attachment.thumbnailURL,
                        fullSizeURL = attachment.fullSizeURL
                ))
            }
            feedbackListUiModel.add(
                    FeedbackUiModel(
                            attachments = mapAttachment,
                            autoReply = it.autoReply,
                            feedbackID = it.feedbackID,
                            rating = it.rating,
                            replyText = it.replyText,
                            replyTime = it.replyTime,
                            reviewText = it.reviewText,
                            isMoreReply = it.replyText?.length.orZero() >= 150,
                            reviewTime = it.reviewTime,
                            reviewerName = it.reviewerName,
                            variantName = it.variantName,
                            sellerUser = userSession.name
                    )
            )
        }

        return feedbackListUiModel
    }


    fun mapToRatingDetailOverallUiModel(productFeedbackDetailResponse:
                                        ProductReviewDetailOverallResponse.ProductGetReviewAggregateByProduct): OverallRatingDetailUiModel {
        return OverallRatingDetailUiModel().apply {
            productName = productFeedbackDetailResponse.productName
            ratingAvg = productFeedbackDetailResponse.ratingAverage
            reviewCount = productFeedbackDetailResponse.ratingCount
        }
    }

    fun mapToItemUnifyListFeedback(context: Context, isEmptyReply: Boolean): ArrayList<ListItemUnify> {
        val itemUnifyList: ArrayList<ListItemUnify> = arrayListOf()
        val iconSize = 24.toPx()

        itemUnifyList.apply {
            add(
                    if (!isEmptyReply) {
                        val iconList = ContextCompat.getDrawable(context, R.drawable.ic_pencil_edit)
                        ListItemUnify(title = context.getString(R.string.edit_review_label), description = "").apply {
                            listDrawable = iconList
                            listIconHeight = iconSize
                            listIconWidth = iconSize                        }
                    } else {
                        val iconList = ContextCompat.getDrawable(context, R.drawable.ic_sent)
                        ListItemUnify(title = context.getString(R.string.review_reply_label), description = "").apply {
                            listDrawable = iconList
                            listIconHeight = iconSize
                            listIconWidth = iconSize                        }
                    }
            )
            val iconReport = ContextCompat.getDrawable(context, R.drawable.ic_report_flag)
            add(
                    ListItemUnify(title = context.getString(R.string.report_label), description = "").apply {
                        listDrawable = iconReport
                        listIconHeight = iconSize
                        listIconWidth = iconSize
                    }
            )
        }

        return itemUnifyList
    }


    private fun mapToItemSortFilter(data: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct): ArrayList<SortFilterItem> {
        val itemSortFilterList = ArrayList<SortFilterItem>()
        val maxData = data.topics.take(4)
        maxData.map {
            val sortFilter = SortFilterItem(
                    title = it.formatted,
                    type = ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL)
            itemSortFilterList.add(sortFilter)
        }
        return itemSortFilterList
    }

}