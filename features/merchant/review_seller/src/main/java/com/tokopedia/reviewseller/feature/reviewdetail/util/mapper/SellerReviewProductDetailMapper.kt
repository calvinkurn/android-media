package com.tokopedia.reviewseller.feature.reviewdetail.util.mapper

import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

object SellerReviewProductDetailMapper {

    fun mapToProductFeedbackDetailUiModel(productFeedbackDataPerProduct:
                                          ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct): ProductFeedbackDetailUiModel {
        return ProductFeedbackDetailUiModel().apply {
            ratingBarList = mapToRatingBarUiModel(productFeedbackDataPerProduct)
            productFeedbackDetailList = mapToFeedbackUiModel(productFeedbackDataPerProduct)
            topicList = mapToTopicUiModel(productFeedbackDataPerProduct)
        }
    }

    fun mapToRatingBarUiModel(productFeedbackDataPerProduct: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct): List<RatingBarUiModel> {
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

    fun mapToTopicUiModel(productFeedbackDataPerProduct: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct): TopicUiModel {
        val topicListUiModel = TopicUiModel()

        productFeedbackDataPerProduct.topics.map {
            topicListUiModel.apply {
                sortFilterItemList = mapToItemSortFilter(productFeedbackDataPerProduct)
                countFeedback = productFeedbackDataPerProduct.list.size
            }
        }
        return topicListUiModel
    }

    fun mapToFeedbackUiModel(productFeedbackDataPerProduct: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct): List<FeedbackUiModel> {
        val feedbackListUiModel = mutableListOf<FeedbackUiModel>()

        val mapAttachment = mutableListOf<FeedbackUiModel.Attachment>()

        productFeedbackDataPerProduct.list.map {
            it.attachments.onEach { attachment ->
                mapAttachment.add(FeedbackUiModel.Attachment(
                        thumbnailURL = attachment.thumbnailURL,
                        fullSizeURL = attachment.fullSizeURL
                ))
            }
        }

        productFeedbackDataPerProduct.list.map {
            feedbackListUiModel.add(
                    FeedbackUiModel(
                            attachments = mapAttachment,
                            autoReply = it.autoReply,
                            feedbackID = it.feedbackID,
                            rating = it.rating,
                            replyText = it.replyText,
                            replyTime = it.replyTime,
                            reviewText = it.reviewText,
                            reviewTime = it.reviewTime,
                            reviewerName = it.reviewerName,
                            variantName = it.variantName
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
            ratingCount = productFeedbackDetailResponse.ratingCount
        }
    }

    fun mapToItemSortFilter(data: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct): ArrayList<SortFilterItem> {
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