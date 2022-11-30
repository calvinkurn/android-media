package com.tokopedia.topads.view.adapter.adgrouplist.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.adgrouplist.model.CreateAdGroupUiModel

class CreateAdGroupViewHolder(itemView:View) : AbstractViewHolder<CreateAdGroupUiModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.create_ad_group_viewholder_layout
    }

    override fun bind(element: CreateAdGroupUiModel?) {

    }
}
