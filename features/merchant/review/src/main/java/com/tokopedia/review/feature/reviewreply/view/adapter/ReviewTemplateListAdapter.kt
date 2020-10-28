package com.tokopedia.review.feature.reviewreply.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.review.R
import com.tokopedia.review.common.util.AdapterDiffCallback
import com.tokopedia.review.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.review.feature.reviewreply.view.viewholder.ReviewTemplateListViewHolder

class ReviewTemplateListAdapter(private val reviewTemplateListener: ReviewTemplateListViewHolder.ReviewTemplateListener): ListAdapter<ReplyTemplateUiModel,
        ReviewTemplateListViewHolder>(AdapterDiffCallback.replyTemplateDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewTemplateListViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_chat_template, parent, false)
        return ReviewTemplateListViewHolder(view, reviewTemplateListener)
    }

    override fun onBindViewHolder(holder: ReviewTemplateListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}