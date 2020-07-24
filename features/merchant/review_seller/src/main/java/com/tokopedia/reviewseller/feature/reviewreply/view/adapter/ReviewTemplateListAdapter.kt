package com.tokopedia.reviewseller.feature.reviewreply.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.AdapterDiffCallback
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.viewholder.ReviewTemplateListViewHolder

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