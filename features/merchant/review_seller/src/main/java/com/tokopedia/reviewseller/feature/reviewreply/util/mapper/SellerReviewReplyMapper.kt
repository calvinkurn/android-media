package com.tokopedia.reviewseller.feature.reviewreply.util.mapper

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewreply.data.ReviewReplyTemplateListResponse
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.toPx

object SellerReviewReplyMapper {


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
        return data.take(5)
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