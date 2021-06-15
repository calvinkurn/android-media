package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchResultUiModel
import com.tokopedia.unifyprinciples.Typography

class ItemSearchChatViewHolder(
        itemView: View?
) : AbstractViewHolder<SearchResultUiModel>(itemView) {

    private var counter: Typography? = itemView?.findViewById(R.id.unread_counter)
    private var username: Typography? = itemView?.findViewById(R.id.user_name)
    private var message: Typography? = itemView?.findViewById(R.id.message)
    private var time: Typography? = itemView?.findViewById(R.id.time)
    private var thumbnail: ImageView? = itemView?.findViewById(R.id.thumbnail)

    override fun bind(element: SearchResultUiModel) {
        hideUnusedElement()
        bindUserImageProfile(element)
        bindUserName(element)
        bindLastMessage(element)
        bindTimeStamp(element)
        bindClick(element)
    }

    private fun hideUnusedElement() {
        counter?.hide()
    }

    private fun bindUserImageProfile(element: SearchResultUiModel) {
        ImageHandler.loadImageCircle2(itemView.context, thumbnail, element.thumbnailUrl)
    }

    private fun bindUserName(element: SearchResultUiModel) {
        username?.text = MethodChecker.fromHtml(element.userName)
        username?.setWeight(Typography.REGULAR)
    }

    private fun bindLastMessage(element: SearchResultUiModel) {
        if (element.lastMessage.isEmpty()) {
            message?.hide()
        } else {
            message?.text = element.lastMessage
        }
    }

    private fun bindTimeStamp(element: SearchResultUiModel) {
        time?.hide()
    }

    private fun bindClick(element: SearchResultUiModel) {
        itemView.setOnClickListener {
            val chatRoomIntent = RouteManager.getIntent(it.context, ApplinkConst.TOPCHAT, element.msgId.toString())
            chatRoomIntent.putExtra(ApplinkConst.Chat.SOURCE_PAGE, ApplinkConst.Chat.SOURCE_CHAT_SEARCH)
            it.context.startActivity(chatRoomIntent)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chat_search_contact
    }
}