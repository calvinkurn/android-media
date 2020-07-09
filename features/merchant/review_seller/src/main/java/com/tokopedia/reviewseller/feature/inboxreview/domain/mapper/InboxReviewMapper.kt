package com.tokopedia.reviewseller.feature.inboxreview.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewseller.feature.inboxreview.domain.response.InboxReviewResponse
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewUiModel
import com.tokopedia.user.session.UserSessionInterface

object InboxReviewMapper {

    fun mapToInboxReviewUiModel(inboxReviewResponse: InboxReviewResponse.ProductGetInboxReviewByShop): InboxReviewUiModel {
        return InboxReviewUiModel(
                filterBy = inboxReviewResponse.filterBy.orEmpty(),
                page = inboxReviewResponse.page.orZero(),
                hasNext = inboxReviewResponse.hasNext ?: false,
                remainder = inboxReviewResponse.remainder.orZero(),
                useAutoReply = inboxReviewResponse.useAutoReply ?: false
        )
    }

    fun mapToFeedbackUiModel(inboxReviewResponse: InboxReviewResponse.ProductGetInboxReviewByShop,
                                     userSession: UserSessionInterface): List<FeedbackInboxUiModel> {
        val feedbackListUiModel = mutableListOf<FeedbackInboxUiModel>()
        inboxReviewResponse.list.map {
            val mapAttachment = mutableListOf<FeedbackInboxUiModel.Attachment>()
            it.attachments.map { attachment ->
                mapAttachment.add(FeedbackInboxUiModel.Attachment(
                        thumbnailURL = attachment.thumbnailURL,
                        fullSizeURL = attachment.fullSizeURL
                ))
            }
            feedbackListUiModel.add(
                    FeedbackInboxUiModel(
                            attachments = mapAttachment,
                            isAutoReply = it.isAutoReply ?: false,
                            feedbackId = it.feedbackID.orZero(),
                            rating = it.rating.orZero(),
                            replyText = it.replyText.orEmpty(),
                            replyTime = it.replyTime.orEmpty(),
                            reviewText = it.reviewText.orEmpty(),
                            isMoreReply = it.replyText?.length.orZero() >= 150,
                            reviewTime = it.reviewTime.orEmpty(),
                            userID = it.user.userID.orZero(),
                            username = it.user.userName.orEmpty(),
                            productID = it.product.productID.orZero(),
                            productName = it.product.productName.orEmpty(),
                            variantID = it.product.productVariant.variantID.orZero(),
                            variantName = it.product.productVariant.variantName.orEmpty(),
                            invoiceID = it.invoiceID.orEmpty(),
                            sellerUser = userSession.name
                    )
            )
        }

        return feedbackListUiModel
    }
}