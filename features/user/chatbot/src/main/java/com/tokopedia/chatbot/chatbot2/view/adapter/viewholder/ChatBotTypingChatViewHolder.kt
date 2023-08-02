package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.TypingChatModel
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.util.generateLeftMessageBackground
import com.tokopedia.chatbot.databinding.ItemChatbotBotTypingBinding

class ChatBotTypingChatViewHolder(itemView: View) : AbstractViewHolder<TypingChatModel>(itemView) {
    val view = ItemChatbotBotTypingBinding.bind(itemView)
    var logo: ImageView = view.image
    var typingDotContainer: LinearLayout = view.typingDotContainer

    private val bg = generateLeftMessageBackground(
        typingDotContainer,
        com.tokopedia.unifyprinciples.R.color.Unify_NN0,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_20
    )

    override fun bind(element: TypingChatModel) {
        bindBackground()
        ImageHandler.loadGif(logo, R.drawable.is_typing, R.drawable.is_typing)
    }

    private fun bindBackground() {
        typingDotContainer.setTypingBackground(bg)
    }

    private fun View.setTypingBackground(bg: Drawable?) {
        val pl = paddingLeft
        val pt = paddingTop
        val pr = paddingRight
        val pb = paddingBottom
        background = bg
        setPadding(pl, pt, pr, pb)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_bot_typing
    }
}
