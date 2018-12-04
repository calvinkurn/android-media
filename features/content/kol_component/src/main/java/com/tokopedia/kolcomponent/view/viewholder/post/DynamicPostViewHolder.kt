package com.tokopedia.kolcomponent.view.viewholder.post

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.adapter.post.PostPagerAdapter
import com.tokopedia.kolcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import kotlinx.android.synthetic.main.item_dynamic_post.view.*

/**
 * @author by milhamj on 28/11/18.
 */
class DynamicPostViewHolder(val v: View) : AbstractViewHolder<DynamicPostViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post
    }

    override fun bind(element: DynamicPostViewModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        itemView.metaTitle.text = element.metaTitle
        itemView.authorImage.loadImageCircle(element.authorImage)
        itemView.authorTitle.text = element.authorTitle
        itemView.authorSubtitile.text = element.authorSubtitle
        itemView.caption.text = element.caption
        bindContentList(element.contentList)
    }

    private fun bindContentList(contentList: MutableList<Any>) {
        val adapter = PostPagerAdapter()
        adapter.setList(contentList)
        itemView.contentViewPager.adapter = adapter
        itemView.contentViewPager.offscreenPageLimit = adapter.count
        itemView.tabLayout.setupWithViewPager(itemView.contentViewPager)
        itemView.tabLayout.visibility = if (adapter.count > 1) View.VISIBLE else View.GONE
    }
}
