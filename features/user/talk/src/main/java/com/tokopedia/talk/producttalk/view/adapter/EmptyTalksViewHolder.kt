package com.tokopedia.talk.producttalk.view.adapter

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk.R
import com.tokopedia.talk.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.EmptyProductTalkViewModel
import kotlinx.android.synthetic.main.empty_inbox_talk.view.*

/**
 * @author by nisie on 8/29/18.
 */

class EmptyTalksViewHolder(val v: View) :
        AbstractViewHolder<EmptyProductTalkViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.empty_inbox_talk
    }

    private val emptyIcon: ImageView = itemView.findViewById(R.id.empty_image)


    override fun bind(element: EmptyProductTalkViewModel?) {
        ImageHandler.loadImageWithIdWithoutPlaceholder(emptyIcon, R.drawable.ic_empty_state)
    }


}