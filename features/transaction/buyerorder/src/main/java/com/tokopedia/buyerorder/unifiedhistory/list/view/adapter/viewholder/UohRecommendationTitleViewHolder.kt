package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohRecommendationTitle
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import kotlinx.android.synthetic.main.uoh_recommendation_title.view.*

/**
 * Created by fwidjaja on 25/07/20.
 */
class UohRecommendationTitleViewHolder(itemView: View) : UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {
    override fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is UohRecommendationTitle) {
            itemView.uoh_recommendation_title.text = item.dataObject.title
        }
    }
}