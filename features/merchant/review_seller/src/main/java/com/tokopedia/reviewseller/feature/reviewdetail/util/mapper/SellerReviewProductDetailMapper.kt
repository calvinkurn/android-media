package com.tokopedia.reviewseller.feature.reviewdetail.util.mapper

import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.*

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

    fun mapToTopicUiModel(productFeedbackDataPerProduct: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct): List<TopicUiModel> {
        val topicListUiModel = mutableListOf<TopicUiModel>()

        productFeedbackDataPerProduct.topics.map {
            topicListUiModel.add(
                    TopicUiModel(
                            title = it.title,
                            count = it.count,
                            formatTitle = it.formatted
                    )
            )
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

}