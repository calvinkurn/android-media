package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder

import android.text.TextUtils
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.EmptySearchModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by nisie on 9/13/17.
 */
class EmptyReputationSearchViewHolder constructor(itemView: View) :
    AbstractViewHolder<EmptySearchModel?>(itemView) {
    var button: Typography
    var title: Typography
    public override fun bind(element: EmptySearchModel) {
        title.setText(element.getTitle())
        if (!TextUtils.isEmpty(element.getButtonText())) {
            button.setVisibility(View.VISIBLE)
            button.setText(element.getButtonText())
            button.setOnClickListener(element.getButtonListener())
        } else {
            button.setVisibility(View.GONE)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.list_empty_search_reputation
    }

    init {
        title = itemView.findViewById<View>(R.id.title) as Typography
        button = itemView.findViewById<View>(R.id.button) as Typography
    }
}