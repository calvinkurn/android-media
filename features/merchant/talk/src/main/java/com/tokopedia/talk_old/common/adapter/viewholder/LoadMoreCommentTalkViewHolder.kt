package com.tokopedia.talk_old.common.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk_old.R
import com.tokopedia.talk_old.common.viewmodel.LoadMoreCommentTalkViewModel
import kotlinx.android.synthetic.main.talk_comment_load.view.*

/**
 * @author by nisie on 9/5/18.
 */

class LoadMoreCommentTalkViewHolder(val v: View,
                                    val listener: LoadMoreListener) :
        AbstractViewHolder<LoadMoreCommentTalkViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.talk_comment_load
    }

    interface LoadMoreListener {
        fun onLoadMoreCommentClicked(talkId: String, shopId: String, allowReply: Boolean, productId: String)
    }

    private val textView: TextView = itemView.textLoadPrevious


    override fun bind(element: LoadMoreCommentTalkViewModel?) {
        element?.run {
            textView.text = v.context.getString(R.string.see_more, element.counter.toString())

            itemView.setOnClickListener {
                listener.onLoadMoreCommentClicked(element.talkId, element.shopId, element.allowReply, element.productId)
            }
        }

    }

}