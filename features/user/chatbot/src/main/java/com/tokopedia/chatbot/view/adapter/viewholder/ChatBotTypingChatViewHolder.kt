package com.tokopedia.chatbot.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.TypingChatModel
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil

class ChatBotTypingChatViewHolder(itemView: View) : AbstractViewHolder<TypingChatModel>(itemView) {
    var logo: ImageView = itemView.findViewById(R.id.image)
    var typingDotContainer: LinearLayout = itemView.findViewById(R.id.typing_dot_container)

    private val bg = ViewUtil.generateBackgroundWithShadow(
            typingDotContainer,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )
    override fun bind(element: TypingChatModel) {
        bindBackground()
        ImageHandler.loadGif(logo, com.tokopedia.chat_common.R.raw.typing, com.tokopedia.chat_common.R.raw.typing)
    }

    private fun bindBackground() {
        typingDotContainer.setMybg(bg)
    }

    private fun View.setMybg(bg: Drawable?) {
        val pl = paddingLeft
        val pt = paddingTop
        val pr = paddingRight
        val pb = paddingBottom
        setBackground(bg)
        setPadding(pl, pt, pr, pb)
    }


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.chatbot_typing_chat_layout
    }

}