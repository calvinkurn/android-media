package com.tokopedia.topads.view.adapter.adgrouplist.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.adgrouplist.model.CreateAdGroupUiModel

class CreateAdGroupViewHolder(itemView:View,listener:CreateAdsCallback) : AbstractViewHolder<CreateAdGroupUiModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.create_ad_group_viewholder_layout
    }

    init{
        itemView.setOnClickListener {
            listener.onCreateAdsClicked()
        }
    }

    override fun bind(element: CreateAdGroupUiModel?) {

    }

    interface CreateAdsCallback{
        fun onCreateAdsClicked()
    }
}
