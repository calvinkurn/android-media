package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatsearch.view.uimodel.ChatReplyUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.time.TimeHelper

class ItemSearchChatReplyViewHolder(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<ChatReplyUiModel>(itemView) {

    private val userName: Typography? = itemView?.findViewById(R.id.user_name)
    private val thumbnail: ImageView? = itemView?.findViewById(R.id.thumbnail)
    private val message: Typography? = itemView?.findViewById(R.id.message)
    private val unreadCounter: Typography? = itemView?.findViewById(R.id.unread_counter)
    private val time: Typography? = itemView?.findViewById(R.id.time)
    private val label: Label? = itemView?.findViewById(R.id.user_label)
    private var productIcon: ImageView? = itemView?.findViewById(R.id.img_product_icon)

    interface Listener {
        fun getSearchKeyWord(): String
        fun onChatReplyClick(element: ChatReplyUiModel)
    }

    override fun bind(element: ChatReplyUiModel) {
        bindUnreadCounter(element)
        bindUserImageProfile(element)
        bindUserName(element)
        bindLastMessage(element)
        bindTimeStamp(element)
        bindLabel(element)
        bindClick(element)
        bindProductIcon(element)
    }

    private fun bindUnreadCounter(element: ChatReplyUiModel) {
        unreadCounter?.hide()
    }

    private fun bindLabel(element: ChatReplyUiModel) {
        when (element.tag) {
            ChatItemListViewHolder.OFFICIAL_TAG -> {
                label?.text = element.tag
                label?.setLabelType(Label.GENERAL_LIGHT_BLUE)
                label?.show()
            }
            ChatItemListViewHolder.SELLER_TAG -> {
                label?.text = element.tag
                label?.setLabelType(Label.GENERAL_LIGHT_GREEN)
                label?.show()
            }
            else -> label?.hide()
        }
    }

    private fun bindUserImageProfile(element: ChatReplyUiModel) {
        ImageHandler.loadImageCircle2(itemView.context, thumbnail, element.thumbnailUrl)
    }

    private fun bindUserName(element: ChatReplyUiModel) {
        userName?.text = element.contact.attributes.name
        userName?.setWeight(Typography.REGULAR)
    }

    private fun bindLastMessage(element: ChatReplyUiModel) {
        message?.text = MethodChecker.fromHtml(element.lastMessage)
        message?.setWeight(Typography.REGULAR)
    }

    private fun bindTimeStamp(element: ChatReplyUiModel) {
        if (element.timeStamp.isEmpty()) return
        time?.text = TimeHelper.getRelativeTimeFromNow(element.timeStampMillis)
    }

    private fun bindClick(element: ChatReplyUiModel) {
        itemView.setOnClickListener {
            listener?.onChatReplyClick(element)
        }
    }

    private fun bindProductIcon(element: ChatReplyUiModel) {
        if (element.productId.isEmpty()) {
            productIcon?.hide()
        } else {
            productIcon?.show()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chat_search_reply
    }
}