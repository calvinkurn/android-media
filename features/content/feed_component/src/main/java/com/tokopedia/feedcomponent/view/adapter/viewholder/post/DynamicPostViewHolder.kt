package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Footer
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Header
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateHeader
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle
import com.tokopedia.feedcomponent.view.adapter.post.PostPagerAdapter
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_dynamic_post.view.*

/**
 * @author by milhamj on 28/11/18.
 */
class DynamicPostViewHolder(v: View)
    : AbstractViewHolder<DynamicPostViewModel>(v) {

    lateinit var listener: DynamicPostListener

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post
    }

    override fun bind(element: DynamicPostViewModel?) {
        if (element == null) {
            itemView.gone()
            return
        }

        bindTitle(element.title, element.template.cardpost.title)
        bindHeader(element.header, element.template.cardpost.header)
        bindContentList(element.contentList, element.template)
        bindFooter(element.footer, element.template)
    }

    private fun bindTitle(title: Title, template: TemplateTitle) {
        itemView.cardTitle.shouldShowWithAction(shouldShowTitle(template)) {
            itemView.cardTitle.bind(title, template)
        }
    }

    private fun shouldShowTitle(template: TemplateTitle): Boolean {
        return template.ctaLink || template.textBadge || template.ctaLink
    }

    private fun bindHeader(header: Header, template: TemplateHeader) {
        itemView.header.shouldShowWithAction(shouldShowHeader(template)) {
            itemView.authorImage.shouldShowWithAction(template.avatar) {
                itemView.authorImage.loadImageCircle(header.avatar)
            }

            itemView.authorTitle.shouldShowWithAction(template.avatarTitle) {
                itemView.authorTitle.text = header.avatarTitle
            }

            itemView.authorSubtitile.shouldShowWithAction(template.avatarDate) {
                itemView.authorSubtitile.text = header.avatarDate
            }

            itemView.headerAction.shouldShowWithAction(template.followCta) {
                if (header.followCta.isFollow) {
                    itemView.headerAction.text = header.followCta.textTrue
                    itemView.headerAction.buttonCompatType = ButtonCompat.SECONDARY
                } else {
                    itemView.headerAction.text = header.followCta.textFalse
                    itemView.headerAction.buttonCompatType = ButtonCompat.PRIMARY
                }

                itemView.headerAction.setOnClickListener {
                    listener.onHeaderActionClick(header.followCta.isFollow)
                }
            }

            itemView.menu.shouldShowWithAction(template.report) {
                itemView.menu.setOnClickListener {
                    listener.onMenuClick()
                }
            }
        }
    }

    private fun shouldShowHeader(template: TemplateHeader): Boolean {
        return template.avatar || template.avatarBadge || template.avatarDate
                || template.avatarDescription || template.avatarTitle || template.followCta
                || template.report
    }

    private fun bindContentList(contentList: MutableList<BasePostViewModel>, template: Template) {
        val adapter = PostPagerAdapter()
        adapter.setList(contentList)
        itemView.contentViewPager.adapter = adapter
        itemView.contentViewPager.offscreenPageLimit = adapter.count
        itemView.tabLayout.setupWithViewPager(itemView.contentViewPager)
        itemView.tabLayout.visibility = if (adapter.count > 1) View.VISIBLE else View.GONE
    }

    private fun bindFooter(footer: Footer, template: Template) {
        if (footer.buttonCta.text.isNotEmpty()) {
            itemView.shareSpace.gone()
            itemView.footerAction.visible()
            itemView.footerAction.text = footer.buttonCta.text
        } else {
            itemView.shareSpace.visible()
            itemView.footerAction.gone()
        }

        bindLike(footer.like.isChecked, footer.like.value, footer.like.fmt)
        bindComment(footer.comment.value, footer.comment.fmt)
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
        fun onHeaderActionClick(isFollowed: Boolean)

        fun onMenuClick()

        fun onLikeClick()

        fun onCommentClick()

        fun onShareClick()

        fun onFooterActionClick()
    }
}
