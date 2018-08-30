package com.tokopedia.talk.inboxtalk.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R
import com.tokopedia.talk.inboxtalk.viewmodel.EmptyInboxTalkViewModel

/**
 * @author by nisie on 8/29/18.
 */

class EmptyInboxTalkViewHolder(val v: View) :
        AbstractViewHolder<EmptyInboxTalkViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.empty_inbox_talk
    }


    override fun bind(element: EmptyInboxTalkViewModel?) {
        element?.run {  }
    }

}