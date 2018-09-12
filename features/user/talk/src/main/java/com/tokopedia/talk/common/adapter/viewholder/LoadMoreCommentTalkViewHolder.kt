package com.tokopedia.talk.common.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R
import com.tokopedia.talk.common.viewmodel.LoadMoreCommentTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.LoadProductTalkThreadViewModel
import kotlinx.android.synthetic.main.talk_comment_load.view.*

/**
 * @author by nisie on 9/5/18.
 */

class LoadMoreCommentTalkViewHolder(val v: View) :
        AbstractViewHolder<LoadMoreCommentTalkViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.talk_comment_load
    }

    private val textView: TextView = itemView.textLoadPrevious


    override fun bind(element: LoadMoreCommentTalkViewModel?) {
        element?.run {
            textView.text = v.context.getString(R.string.see_more, element.counter.toString())
        }

    }

}