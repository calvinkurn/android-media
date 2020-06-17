package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.view.uimodel.ChatReplyUiModel
import com.tokopedia.unifyprinciples.Typography

class ItemSearchChatReplyViewHolder(itemView: View?) : AbstractViewHolder<ChatReplyUiModel>(itemView) {

    private var counter: Typography? = itemView?.findViewById(R.id.unread_counter)
    private var username: Typography? = itemView?.findViewById(R.id.user_name)
    private var message: Typography? = itemView?.findViewById(R.id.message)
    private var time: Typography? = itemView?.findViewById(R.id.time)
    private var thumbnail: SquareImageView? = itemView?.findViewById(R.id.thumbnail)

    override fun bind(element: ChatReplyUiModel) {
        hideUnusedElement()
        bindUserImageProfile(element)
        bindUserName(element)
        bindLastMessage(element)
        bindTimeStamp(element)
        bindClick(element)
    }

    private fun hideUnusedElement() {

    }

    private fun bindUserImageProfile(element: ChatReplyUiModel) {
        ImageHandler.loadImageCircle2(itemView.context, thumbnail, element.thumbnailUrl)
    }

    private fun bindUserName(element: ChatReplyUiModel) {
        username?.text = element.contact.attributes.name
        username?.setWeight(Typography.REGULAR)
    }

    private fun bindLastMessage(element: ChatReplyUiModel) {
        message?.text = element.lastMessage
    }

    private fun bindTimeStamp(element: ChatReplyUiModel) {
        time?.hide()
    }

    private fun bindClick(element: ChatReplyUiModel) {
        itemView.setOnClickListener {
            val chatRoomIntent = RouteManager.getIntent(it.context, ApplinkConst.TOPCHAT, element.msgId.toString())
            it.context.startActivity(chatRoomIntent)
//            listener?.finishSearchActivity()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chat_search_reply
    }
}