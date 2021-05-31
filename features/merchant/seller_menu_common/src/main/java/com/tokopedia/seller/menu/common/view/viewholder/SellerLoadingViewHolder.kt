package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.view.uimodel.SettingLoadingUiModel

class SellerLoadingViewHolder(itemView: View): AbstractViewHolder<SettingLoadingUiModel>(itemView) {

    override fun bind(element: SettingLoadingUiModel?) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}