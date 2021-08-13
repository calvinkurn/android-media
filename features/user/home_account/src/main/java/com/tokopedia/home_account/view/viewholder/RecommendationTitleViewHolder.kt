package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.RecommendationTitleView
import kotlinx.android.synthetic.main.home_account_item_recommendation_title.view.*

class RecommendationTitleViewHolder (itemView: View): BaseViewHolder(itemView) {

    fun bind(setting: RecommendationTitleView) {
        with(itemView) {
            home_account_recommendation_title_tv?.text = setting.title
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_recommendation_title
    }

}