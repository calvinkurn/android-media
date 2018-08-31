package com.tokopedia.talk.inboxtalk.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.talk.R
import com.tokopedia.talk.inboxtalk.viewmodel.InboxTalkItemViewModel
import kotlinx.android.synthetic.main.inbox_talk_item_product_header.view.*

/**
 * @author by nisie on 8/30/18.
 */

class InboxTalkItemViewHolder(val v: View) :
        AbstractViewHolder<InboxTalkItemViewModel>(v) {


    companion object {
        val LAYOUT = R.layout.inbox_talk_item
    }


    override fun bind(element: InboxTalkItemViewModel?) {
        element?.run {
            itemView.productName.text = MethodChecker.fromHtml(element.productName)
            ImageHandler.LoadImage(itemView.productAvatar, "https://cdn-images-1.medium.com/fit/c/72/72/1*EZ5_P3ZgjQwUJWO9CosQHw.jpeg")
        }

    }

}