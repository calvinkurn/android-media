package com.tokopedia.talk.inboxtalk.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.talk.R
import com.tokopedia.talk.inboxtalk.viewmodel.InboxTalkItemViewModel
import kotlinx.android.synthetic.main.inbox_talk_item_product_header.view.*
import kotlinx.android.synthetic.main.talk_item.view.*

/**
 * @author by nisie on 8/30/18.
 */

class InboxTalkItemViewHolder(val v: View) :
        AbstractViewHolder<InboxTalkItemViewModel>(v) {

    private val productName: TextView = itemView.productName
    private val productAvatar: ImageView = itemView.productAvatar
    private val profileAvatar: ImageView = itemView.prof_pict
    private val profileName: TextView = itemView.username
    private val timestamp: TextView = itemView.timestamp
    private val menuButton: ImageView = itemView.menu
    private val talkContent: TextView = itemView.talk_content

    companion object {
        val LAYOUT = R.layout.inbox_talk_item
    }


    override fun bind(element: InboxTalkItemViewModel?) {
        element?.run {
            productName.text = MethodChecker.fromHtml(element.productHeader.productName)
            ImageHandler.LoadImage(productAvatar, element.productHeader.productAvatar)

            ImageHandler.LoadImage(profileAvatar, element.talkThread.headThread.avatar)
            profileName.text = element.talkThread.headThread.name
            talkContent.text = element.talkThread.headThread.comment
            timestamp.text = element.talkThread.headThread.timestamp

            if (element.talkThread.headThread.menu.isEmpty())
                menuButton.visibility = View.GONE
            else {
                menuButton.visibility = View.VISIBLE
            }

        }

    }

}