package com.tokopedia.kolcomponent.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kolcomponent.view.viewmodel.DynamicPostViewModel

/**
 * @author by milhamj on 28/11/18.
 */
class DynamicPostViewHolder(val v: View) : AbstractViewHolder<DynamicPostViewModel>(v) {

    companion object {
//        @LayoutRes val LAYOUT = R.layout.item_dynamic_post
    }

    override fun bind(element: DynamicPostViewModel?) {
//        if (element == null) {
//            itemView.hide()
//            return
//        }

//        itemView.metaTitle.text = element.metaTitle
//        itemView.authorImage.loadImageCircle(element.authorImage)
//        itemView.authorTitle.text = element.authorTitle
//        itemView.authorSubtitile.text = element.authorSubtitle
//        itemView.caption.text = element.caption
    }
}
