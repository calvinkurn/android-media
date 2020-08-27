package com.tokopedia.tokopoints.view.tokopointhome

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.widget.TopAdsImageView
import kotlinx.android.synthetic.main.tp_topads_reward_layout.view.*

class SectionTopadsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: TopAdsImageViewModel) {

        itemView.findViewById<TopAdsImageView>(R.id.topads_reward).background= ContextCompat.getDrawable(itemView.context, R.drawable.bg_tierinfo)
        itemView.topads_reward?.let {
            it.loadImage(model)
        }
    }
}