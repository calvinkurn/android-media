package com.tokopedia.feedcomponent.view.adapter.viewholder.relatedpost

import androidx.annotation.LayoutRes
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.relatedpost.RelatedPostViewModel
import com.tokopedia.kotlin.extensions.view.gone
import kotlinx.android.synthetic.main.item_related_post.view.*

/**
 * @author by milhamj on 2019-08-12.
 */
class RelatedPostViewHolder(v: View,
                            private val listener: RelatedPostAdapter.RelatedPostListener)
    : AbstractViewHolder<RelatedPostViewModel>(v) {

    private var adapter: RelatedPostAdapter? = null

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_related_post
    }

    override fun bind(element: RelatedPostViewModel?) {
        with(itemView) {
            if (element == null) {
                gone()
                return
            }

            if (!TextUtils.isEmpty(element.title)) {
                title.text = element.title
            }
            adapter = RelatedPostAdapter(element.relatedPostList, listener)
            relatedPostRv.adapter = adapter
        }
    }
}