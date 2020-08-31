package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.uimodel.HeaderDateUiModel
import com.tokopedia.unifyprinciples.Typography

class HeaderDateViewHolder(itemView: View?) : AbstractViewHolder<HeaderDateUiModel>(itemView) {

    private var date: Typography? = null

    override fun bind(element: HeaderDateUiModel?) {
        if (element == null) return
        initView()
        bindDate(element)
    }

    private fun initView() {
        date = itemView.findViewById(R.id.header_date)
    }

    private fun bindDate(element: HeaderDateUiModel) {
        date?.text = element.relativeDate
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_header_date
    }
}