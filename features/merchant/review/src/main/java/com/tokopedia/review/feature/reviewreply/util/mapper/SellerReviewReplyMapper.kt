package com.tokopedia.review.feature.reviewreply.util.mapper

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyInsertResponse
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyInsertTemplateResponse
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyTemplateListResponse
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyUpdateResponse
import com.tokopedia.review.feature.reviewreply.view.model.InsertReplyResponseUiModel
import com.tokopedia.review.feature.reviewreply.view.model.InsertTemplateReplyUiModel
import com.tokopedia.review.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.review.feature.reviewreply.view.model.UpdateReplyResponseUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.toPx

object SellerReviewReplyMapper {


    fun mapToInsertReplyUiModel(inboxReviewInsertReviewResponse: ReviewReplyInsertResponse.InboxReviewInsertReviewResponse): InsertReplyResponseUiModel {
        return InsertReplyResponseUiModel(inboxReviewInsertReviewResponse.isSuccesss)
    }

    fun mapToUpdateReplyUiModel(productrevUpdateSellerResponse: ReviewReplyUpdateResponse.ProductrevUpdateSellerResponse): UpdateReplyResponseUiModel {
        return UpdateReplyResponseUiModel(
                isSuccess = productrevUpdateSellerResponse.success,
                feedbackId = productrevUpdateSellerResponse.data.feedbackID,
                responseBy = productrevUpdateSellerResponse.data.responseBy,
                shopId = productrevUpdateSellerResponse.data.shopID,
                responseMessage = productrevUpdateSellerResponse.data.responseMessage)
    }

    fun mapToItemTemplateUiModel(templateList: ReviewReplyTemplateListResponse.ReviewResponseTemplateList): List<ReplyTemplateUiModel> {
        val data = mutableListOf<ReplyTemplateUiModel>()
        templateList.list.map {
            data.add(ReplyTemplateUiModel(
                    message = it.message,
                    status = it.status,
                    templateId = it.templateId,
                    title = it.title,
                    isSelected = true
            ))
        }
        return data
    }

    fun mapToInsertTemplateReplyUiModel(reviewReplyInsertTemplateResponse: ReviewReplyInsertTemplateResponse.InsertResponseTemplate): InsertTemplateReplyUiModel {
        return InsertTemplateReplyUiModel(
                isSuccess = reviewReplyInsertTemplateResponse.success,
                defaultTemplateID = reviewReplyInsertTemplateResponse.defaultTemplateID,
                error = reviewReplyInsertTemplateResponse.error
        )
    }

    fun mapToItemUnifyMenuReport(context: Context): ArrayList<ListItemUnify> {
        val itemUnifyList: ArrayList<ListItemUnify> = arrayListOf()
        val iconSize = 24.toPx()

        itemUnifyList.apply {
            val iconReport = ContextCompat.getDrawable(context, R.drawable.ic_report_flag)

            add(
                    ListItemUnify(
                            title = context.getString(R.string.report_label),
                            description = "").apply {
                        listDrawable = iconReport
                        listIconHeight = iconSize
                        listIconWidth = iconSize
                    }

            )
        }

        return itemUnifyList
    }

}