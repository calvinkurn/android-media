package com.tokopedia.talk.talkdetails.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk.R
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsThreadItemViewModel

/**
 * Created by Hendri on 29/08/18.
 */

class TalkDetailsThreadViewHolder(itemView: View):
        AbstractViewHolder<TalkDetailsThreadItemViewModel>(itemView){

    val profPic: ImageView = itemView.findViewById(R.id.prof_pict)
    val name: TextView = itemView.findViewById(R.id.username)
    val time: TextView = itemView.findViewById(R.id.timestamp)
    val talkContent: TextView = itemView.findViewById(R.id.talk_content)

    override fun bind(element: TalkDetailsThreadItemViewModel?) {
        element?.let {
            name.text = it.name
            time.text = it.timestamp
            ImageHandler.loadImageCircle2(itemView.context,profPic,it.avatar)
            talkContent.text = it.comment
        }
    }

    companion object {
        val LAYOUT = R.layout.talk_item
    }
}