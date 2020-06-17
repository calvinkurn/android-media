package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchResultUiModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_chat_list.view.*

class ItemSearchChatViewHolder(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<SearchResultUiModel>(itemView) {

    private var counter: Typography? = itemView?.findViewById(R.id.unread_counter)
    private var username: Typography? = itemView?.findViewById(R.id.user_name)
    private var message: Typography? = itemView?.findViewById(R.id.message)
    private var time: Typography? = itemView?.findViewById(R.id.time)

    interface Listener {
        fun finishSearchActivity()
    }

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
        ImageHandler.loadImageCircle2(itemView.context, itemView.thumbnail, element.thumbnailUrl)
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
            it.context.startActivity(chatRoomIntent)
            listener?.finishSearchActivity()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chat_list
    }
}