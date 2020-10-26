package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel

class BigDividerViewHolder(itemView: View?) : AbstractViewHolder<BigDividerUiModel>(itemView) {

    override fun bind(element: BigDividerUiModel?) {}

    companion object {
        val LAYOUT = R.layout.item_notifcenter_big_divider
    }
}