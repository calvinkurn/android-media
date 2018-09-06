package com.tokopedia.talk.common.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk.R
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import kotlinx.android.synthetic.main.talk_item.view.*

/**
 * @author by nisie on 9/5/18.
 */
class CommentTalkViewHolder(val v: View) :
        AbstractViewHolder<ProductTalkItemViewModel>(v) {

    private val profileAvatar: ImageView = itemView.prof_pict
    private val profileName: TextView = itemView.username
    private val datetime: TextView = itemView.timestamp
    private val menuButton: ImageView = itemView.menu
    private val talkContent: TextView = itemView.talk_content

    companion object {
        val LAYOUT = R.layout.talk_item
    }


    override fun bind(element: ProductTalkItemViewModel?) {
        element?.run {

            ImageHandler.loadImageCircle2(profileAvatar.context, profileAvatar, element.avatar)
            profileName.text = element.name
            talkContent.text = element.comment
            datetime.text = element.timestamp

        }

    }

}