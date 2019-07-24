package com.tokopedia.home.account.presentation.viewholder

import android.support.annotation.LayoutRes
import android.support.v4.widget.TextViewCompat
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.viewmodel.AccountRecommendationTitleViewModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Author devarafikry on 24/07/19
 */
class AccountRecommendationTitleViewHolder(itemView: View) : AbstractViewHolder<AccountRecommendationTitleViewModel>(itemView) {

    private val textView: Typography

    init {
        textView = itemView.findViewById(R.id.title)
    }

    override fun bind(element: AccountRecommendationTitleViewModel) {
        textView.text = element.title
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_account_recommendation_title
    }
}
