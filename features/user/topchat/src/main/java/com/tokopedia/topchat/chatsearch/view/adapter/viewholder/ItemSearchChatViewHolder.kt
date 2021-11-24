package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.util.Utils
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchResultUiModel
import com.tokopedia.topchat.common.Constant
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
        thumbnail?.loadImageCircle(element.thumbnailUrl)
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
            chatRoomIntent.putExtra(Constant.CHAT_USER_ROLE_KEY, element.contact.role)
            chatRoomIntent.putExtra(Constant.CHAT_CURRENT_ACTIVE, element.msgId)
            Utils.putExtraForFoldable(chatRoomIntent, element.msgId, element.contact.role)
            it.context.startActivity(chatRoomIntent)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chat_search_contact
    }
}