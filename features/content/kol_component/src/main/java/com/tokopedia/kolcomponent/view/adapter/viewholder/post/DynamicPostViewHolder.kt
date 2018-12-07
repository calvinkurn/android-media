package com.tokopedia.kolcomponent.view.adapter.viewholder.post

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.adapter.post.PostPagerAdapter
import com.tokopedia.kolcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
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
            itemView.gone()
            return
        }

        bindTitle(element.metaTitle)
        bindHeader(element)
        bindContentList(element.contentList)
        bindFooter(element)
    }

    private fun bindTitle(title: String) {
        itemView.metaTitle.shouldShowWithAction(title.isEmpty().not()) {
            itemView.metaTitle.text = title
        }
    }

    private fun bindHeader(element: DynamicPostViewModel) {
        itemView.authorImage.loadImageCircle(element.authorImage)
        itemView.authorTitle.text = element.authorTitle
        itemView.authorSubtitile.text = element.authorSubtitle
        itemView.caption.text = element.caption
    }

    private fun bindContentList(contentList: MutableList<Any>) {
        val adapter = PostPagerAdapter()
        adapter.setList(contentList)
        itemView.contentViewPager.adapter = adapter
        itemView.contentViewPager.offscreenPageLimit = adapter.count
        itemView.tabLayout.setupWithViewPager(itemView.contentViewPager)
        itemView.tabLayout.visibility = if (adapter.count > 1) View.VISIBLE else View.GONE
    }

    private fun bindFooter(element: DynamicPostViewModel) {
        if (element.footerActionText.isEmpty().not()) {
            itemView.shareSpace.gone()
            itemView.footerAction.visible()
            itemView.footerAction.text = element.footerActionText
        } else {
            itemView.shareSpace.visible()
            itemView.footerAction.gone()
        }
    }

    interface DynamicPostListener {
        fun onHeaderActionClick()

        fun onMenuClick()

        fun onLikeClick()

        fun onCommentClick()

        fun onShareClick()

        fun onFooterActionClick()
    }
}
