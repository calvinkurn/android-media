package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.listener.RecyclerViewItemRemoverListener
import com.tokopedia.review.feature.createreputation.presentation.listener.ReviewTemplateListener
import com.tokopedia.unifycomponents.ChipsUnify

class ReviewTemplatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(template: String, reviewTemplateListener: ReviewTemplateListener, itemRemoverListener: RecyclerViewItemRemoverListener) {
        itemView.setOnClickListener {
            reviewTemplateListener.onTemplateSelected(template)
            itemRemoverListener.onItemClicked(adapterPosition)
        }
        itemView.findViewById<ChipsUnify>(R.id.review_template_chip)?.chipText = template
    }
}