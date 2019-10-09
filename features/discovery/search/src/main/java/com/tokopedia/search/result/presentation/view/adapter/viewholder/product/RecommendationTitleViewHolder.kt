package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.model.RecommendationTitleViewModel
import com.tokopedia.search.R

/**
 * Created by Lukas on 08/10/19
 */

class RecommendationTitleViewHolder(
        itemView: View
) :  AbstractViewHolder<RecommendationTitleViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.recommendation_title_layout
    }

    private val title: TextView by lazy { itemView.findViewById<TextView>(R.id.title) }

    override fun bind(element: RecommendationTitleViewModel?) {
        title.text = element?.title
    }


}