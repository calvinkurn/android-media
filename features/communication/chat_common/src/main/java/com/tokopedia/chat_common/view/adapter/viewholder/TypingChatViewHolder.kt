package com.tokopedia.chat_common.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.TypingChatModel
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.R
import com.tokopedia.media.loader.loadAsGif

class TypingChatViewHolder(itemView: View) : AbstractViewHolder<TypingChatModel>(itemView) {

    var logo: ImageView = itemView.findViewById(R.id.image)

    override fun bind(element: TypingChatModel) {
        logo.loadAsGif(R.raw.typing)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.typing_chat_layout
    }

}
