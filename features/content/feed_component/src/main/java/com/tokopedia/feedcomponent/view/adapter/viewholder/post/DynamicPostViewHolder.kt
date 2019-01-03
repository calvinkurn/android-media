package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.*
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateBody
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateHeader
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.adapter.post.PostPagerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewitemView.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_dynamic_post.view.*

/**
 * @author by milhamj on 28/11/18.
 */
class DynamicPostViewHolder(v: View, private var listener: DynamicPostListener,
                            private var cardTitleListener: CardTitleView.CardTitleListener,
                            private var youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                            private var pollOptionListener: PollAdapter.PollOptionListener)
    : AbstractViewHolder<DynamicPostViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post

        const val PAYLOAD_LIKE = 13
        const val PAYLOAD_COMMENT = 14
        const val PAYLOAD_FOLLOW = 15

        const val MAX_CHAR = 140
        const val CAPTION_END = 100
    }

    override fun bind(element: DynamicPostViewModel?) {
        if (element == null) {
            itemView.gone()
            return
        }

        bindTitle(element.title, element.template.cardpost.title)
        bindHeader(element.header, element.template.cardpost.header)
        bindCaption(element.caption, element.template.cardpost.body)
        bindContentList(element.contentList, element.template.cardpost.body)
        bindFooter(element.id, element.footer, element.template.cardpost.footer)
    }

    override fun bind(element: DynamicPostViewModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty() || payloads[0] !is Int) {
            return
        }

        when (payloads[0] as Int) {
            PAYLOAD_LIKE -> bindLike(element.footer.like)
            PAYLOAD_COMMENT -> bindComment(element.footer.comment)
            else -> bind(element)
        }
    }

    private fun bindTitle(title: Title, template: TemplateTitle) {
        itemView.cardTitle.shouldShowWithAction(shouldShowTitle(template)) {
            itemView.cardTitle.bind(title, template)
        }
        itemView.cardTitle.listener = cardTitleListener
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
                header.avatarDate = TimeConverter.generateTime(itemView.context, header.avatarDate)
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

    private fun bindCaption(caption: Caption, template: TemplateBody) {
        itemView.caption.shouldShowWithAction(template.caption) {
            if (caption.text.length > MAX_CHAR) {
                val captionText = caption.text.substring(0, CAPTION_END)
                        .replace("(\r\n|\n)", "<br />")
                        .plus("... ")
                        .plus("<font color='#42b549'><b>")
                        .plus(caption.buttonName)
                        .plus("</b></font>")

                itemView.caption.text = MethodChecker.fromHtml(captionText)
                itemView.caption.setOnClickListener { listener.onCaptionClick(caption.appLink) }
            } else {
                itemView.caption.text = caption.text
            }
        }
    }

    private fun bindContentList(contentList: MutableList<BasePostViewModel>, template: TemplateBody) {
        itemView.contentLayout.shouldShowWithAction(template.media) {
            contentList.forEach { it.rowNumber = adapterPosition }

            val adapter = PostPagerAdapter(youtubePostListener, pollOptionListener)
            adapter.setList(contentList)
            itemView.contentViewPager.adapter = adapter
            itemView.contentViewPager.offscreenPageLimit = adapter.count
            itemView.tabLayout.setupWithViewPager(itemView.contentViewPager)
            itemView.tabLayout.visibility = if (adapter.count > 1) View.VISIBLE else View.GONE
        }
    }

    private fun bindFooter(id: Int, footer: Footer, template: TemplateFooter) {
        itemView.footer.shouldShowWithAction(shouldShowFooter(template)) {
            if (template.ctaLink && footer.buttonCta.text.isNotEmpty()) {
                itemView.shareSpace.gone()
                itemView.footerAction.visible()
                itemView.footerAction.text = footer.buttonCta.text
                itemView.footerAction.setOnClickListener { listener.onFooterActionClick() }
            } else {
                itemView.shareSpace.visible()
                itemView.footerAction.gone()
            }

            if (template.like) {
                itemView.likeIcon.visible()
                itemView.likeText.visible()
                itemView.likeIcon.setOnClickListener { listener.onLikeClick(adapterPosition, id, footer.like.isChecked) }
                itemView.likeText.setOnClickListener { listener.onLikeClick(adapterPosition, id, footer.like.isChecked) }
                bindLike(footer.like)
            } else {
                itemView.likeIcon.gone()
                itemView.likeText.gone()
            }

            if (template.comment) {
                itemView.commentIcon.visible()
                itemView.commentText.visible()
                itemView.commentIcon.setOnClickListener { listener.onCommentClick(adapterPosition, id) }
                itemView.commentText.setOnClickListener { listener.onCommentClick(adapterPosition, id) }
                bindComment(footer.comment)
            } else {
                itemView.commentIcon.gone()
                itemView.commentText.gone()
            }

            if (template.share) {
                itemView.shareIcon.visible()
                itemView.shareText.visible()
                itemView.shareIcon.setOnClickListener { listener.onShareClick() }
                itemView.shareText.setOnClickListener { listener.onShareClick() }
            } else {
                itemView.shareIcon.gone()
                itemView.shareText.gone()
            }
        }
    }

    private fun shouldShowFooter(template: TemplateFooter): Boolean {
        return template.comment || template.ctaLink || template.like || template.share
    }

    private fun bindLike(like: Like) {
        when {
            like.isChecked -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_green)
                itemView.likeText.text = like.fmt
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeText.context, R.color.tkpd_main_green)
                )
            }
            like.value > 0 -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb)
                itemView.likeText.text = like.fmt
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

    private fun bindComment(comment: Comment) {
        itemView.commentText.text =
                if (comment.value == 0) getString(R.string.kol_action_comment)
                else comment.fmt
    }

    interface DynamicPostListener {
        fun onHeaderActionClick(isFollowed: Boolean)

        fun onMenuClick()

        fun onCaptionClick(redirectUrl: String)

        fun onLikeClick(position: Int, id: Int, isLiked: Boolean)

        fun onCommentClick(position: Int, id: Int)

        fun onShareClick()

        fun onFooterActionClick()
    }
}
