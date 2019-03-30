package com.tokopedia.talk.inboxtalk.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk.R
import com.tokopedia.talk.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import kotlinx.android.synthetic.main.empty_inbox_talk.view.*

/**
 * @author by nisie on 8/29/18.
 */

class EmptyInboxTalkViewHolder(val v: View) :
        AbstractViewHolder<EmptyInboxTalkViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.empty_inbox_talk
    }

    private val emptyIcon: ImageView = itemView.empty_image


    override fun bind(element: EmptyInboxTalkViewModel?) {
        ImageHandler.loadImageWithIdWithoutPlaceholder(emptyIcon, R.drawable.ic_empty_state)
    }

    override fun onViewRecycled() {
        ImageHandler.clearImage(emptyIcon)
    }

}