package com.tokopedia.review.feature.reviewreply.util.mapper

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyInsertTemplateResponse
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyTemplateListResponse
import com.tokopedia.review.feature.reviewreply.view.model.InsertTemplateReplyUiModel
import com.tokopedia.review.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.toPx

object SellerReviewReplyMapper {

    fun mapToItemTemplateUiModel(
        templateList:
        ReviewReplyTemplateListResponse.ReviewResponseTemplateList
    ): List<ReplyTemplateUiModel> {
        val data = mutableListOf<ReplyTemplateUiModel>()
        templateList.list.map {
            data.add(
                ReplyTemplateUiModel(
                    message = it.message,
                    status = it.status.toLongOrZero(),
                    templateId = it.templateId.toLongOrZero(),
                    title = it.title,
                    isSelected = true
                )
            )
        }
        return data
    }

    fun mapToInsertTemplateReplyUiModel(
        reviewReplyInsertTemplateResponse:
        ReviewReplyInsertTemplateResponse.InsertResponseTemplate
    ): InsertTemplateReplyUiModel {
        return InsertTemplateReplyUiModel(
            isSuccess = reviewReplyInsertTemplateResponse.success,
            defaultTemplateID = reviewReplyInsertTemplateResponse.defaultTemplateID.toLongOrZero(),
            error = reviewReplyInsertTemplateResponse.error
        )
    }

    fun mapToItemUnifyMenuReport(context: Context): ArrayList<ListItemUnify> {
        val itemUnifyList: ArrayList<ListItemUnify> = arrayListOf()
        val iconSize = ICON_SIZE.toPx()

        itemUnifyList.apply {
            val iconReport = ContextCompat.getDrawable(context, R.drawable.ic_report_flag)
            add(
                ListItemUnify(
                    title = context.getString(R.string.report_label),
                    description = ""
                ).apply {
                    listDrawable = iconReport
                    listIconHeight = iconSize
                    listIconWidth = iconSize
                }
            )
        }
        return itemUnifyList
    }

    private const val ICON_SIZE = 24
}