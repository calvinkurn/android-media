package com.tokopedia.talk.common.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk.ProductTalkItemViewModel
import com.tokopedia.talk.R
import com.tokopedia.talk.TalkState
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import kotlinx.android.synthetic.main.talk_item.view.*

/**
 * @author by nisie on 9/5/18.
 */
class CommentTalkViewHolder(val v: View,
                            val listener: TalkCommentItemListener,
                            private val productListener: TalkProductAttachmentAdapter.ProductAttachmentItemClickListener) :
        AbstractViewHolder<ProductTalkItemViewModel>(v) {

    interface TalkCommentItemListener {
        fun onCommentMenuButtonClicked(menu: TalkState)
    }

    private val profileAvatar: ImageView = itemView.prof_pict
    private val profileName: TextView = itemView.username
    private val datetime: TextView = itemView.timestamp
    private val menuButton: ImageView = itemView.menu
    private val talkContent: TextView = itemView.talk_content
    private val listProduct: RecyclerView = itemView.productAttachment

    private lateinit var adapter: TalkProductAttachmentAdapter

    companion object {
        val LAYOUT = R.layout.talk_item
    }


    override fun bind(element: ProductTalkItemViewModel?) {
        element?.run {

            if (!element.productAttachment.isEmpty()) {

                adapter = TalkProductAttachmentAdapter(productListener, element.productAttachment)
                listProduct.layoutManager = LinearLayoutManager(itemView.context,
                        LinearLayoutManager
                                .VERTICAL, false)
                listProduct.adapter = adapter
                listProduct.visibility = View.VISIBLE
            }

            ImageHandler.loadImageCircle2(profileAvatar.context, profileAvatar, element.avatar)
            profileName.text = element.name
            talkContent.text = element.comment
            datetime.text = element.timestamp

            setupMenuButton(element.menu)

        }

    }

    private fun setupMenuButton(menu: TalkState) {
        if (menu.allowDelete || menu.allowFollow || menu.allowReport || menu.allowUnfollow) {
            menuButton.visibility = View.VISIBLE
        } else {
            menuButton.visibility = View.GONE
        }

        menuButton.setOnClickListener { listener.onCommentMenuButtonClicked(menu) }
    }

}