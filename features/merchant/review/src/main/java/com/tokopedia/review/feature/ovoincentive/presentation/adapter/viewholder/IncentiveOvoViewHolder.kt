package com.tokopedia.review.feature.ovoincentive.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.item_incentive_ovo.view.*

class IncentiveOvoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindHero(explanation: String) {
        itemView.apply {
            tgIncentiveOvoNumber.text = "${adapterPosition+1}."
            tgIncentiveOvoExplanation.text = HtmlLinkHelper(context, explanation).spannedString
        }
    }
}