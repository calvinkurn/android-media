package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.RecommendationTitleUiModel
import com.tokopedia.unifyprinciples.Typography

class RecommendationTitleViewHolder(
        itemView: View?
) : AbstractViewHolder<RecommendationTitleUiModel>(itemView) {

    private val title: Typography? = itemView?.findViewById(R.id.txt_recommendation_tile)

    override fun bind(element: RecommendationTitleUiModel) {
        bindTitle(element)
    }

    private fun bindTitle(element: RecommendationTitleUiModel) {
        title?.text = element.title
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_recommendation_title
    }
}