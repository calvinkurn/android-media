package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder

import android.text.TextUtils
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.EmptySearchModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by nisie on 9/13/17.
 */
class EmptyReputationSearchViewHolder constructor(itemView: View) :
    AbstractViewHolder<EmptySearchModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.list_empty_search_reputation
    }

    private var button: Typography? = itemView.findViewById(R.id.title)
    private var title: Typography? = itemView.findViewById(R.id.button)

    override fun bind(element: EmptySearchModel) {
        title?.text = element.title
        if (!TextUtils.isEmpty(element.buttonText)) {
            button?.apply {
                visibility = View.VISIBLE
                text = element.buttonText
                setOnClickListener(element.buttonListener)
            }
        } else {
            button?.hide()
        }
    }
}