package com.tokopedia.kolcomponent.view.adapter.viewholder.post

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.adapter.post.PostPagerAdapter
import com.tokopedia.kolcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.kolcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_dynamic_post.view.*

/**
 * @author by milhamj on 28/11/18.
 */
class DynamicPostViewHolder(v: View, private val listener: DynamicPostListener)
    : AbstractViewHolder<DynamicPostViewModel>(v) {

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
        itemView.headerAction.setOnClickListener { listener.onHeaderActionClick() }
        itemView.menu.setOnClickListener { listener.onMenuClick() }
    }

    private fun bindContentList(contentList: MutableList<BasePostViewModel>) {
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

        bindLike(element.isLiked, element.likeCountNumber, element.likeCountText)
        bindComment(element.commentCountNumber, element.commentCountText)
    }

    private fun bindLike(isLiked: Boolean, totalLikeNumber: Int, totalLikeText: String) {
        itemView.likeIcon.setOnClickListener { listener.onLikeClick() }
        itemView.likeText.setOnClickListener { listener.onLikeClick() }
        when {
            isLiked -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_green)
                itemView.likeText.text = totalLikeText
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeText.context, R.color.tkpd_main_green)
                )
            }
            totalLikeNumber > 0 -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb)
                itemView.likeText.text = totalLikeText
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeText.context, R.color.black_54)
                )
            }
            else -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb)
                itemView.likeText.setText(R.string.kol_action_like)
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeIcon.context, R.color.black_54)
                )
            }
        }
    }

    private fun bindComment(totalCommentNumber: Int, totalCommentText: String) {
        itemView.commentIcon.setOnClickListener { listener.onCommentClick() }
        itemView.commentText.setOnClickListener { listener.onCommentClick() }
        itemView.commentText.text =
                if (totalCommentNumber == 0) getString(R.string.kol_action_comment)
                else totalCommentText
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
