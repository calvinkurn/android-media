package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.view.uimodel.BigDividerUiModel

class ChatBigDividerViewHolder(itemView: View?) : AbstractViewHolder<BigDividerUiModel>(itemView) {

    override fun bind(element: BigDividerUiModel?) {

    }

    companion object {
        var LAYOUT = R.layout.item_chat_big_divider
    }
}