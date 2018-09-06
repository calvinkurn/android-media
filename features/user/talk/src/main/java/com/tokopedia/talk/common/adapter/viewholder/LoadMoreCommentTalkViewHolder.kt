package com.tokopedia.talk.common.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.LoadProductTalkViewModel
import com.tokopedia.talk.R

/**
 * @author by nisie on 9/5/18.
 */

class LoadMoreCommentTalkViewHolder(val v: View) :
        AbstractViewHolder<LoadProductTalkViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.inbox_talk_item
    }


    override fun bind(element: LoadProductTalkViewModel?) {
        element?.run {


        }

    }

}