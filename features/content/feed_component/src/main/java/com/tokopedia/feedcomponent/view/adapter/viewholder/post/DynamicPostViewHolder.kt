package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.support.annotation.LayoutRes
import android.text.TextUtils
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
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_dynamic_post.view.*

/**
 * @author by milhamj on 28/11/18.
 */
class DynamicPostViewHolder(v: View,
                            private val listener: DynamicPostListener,
                            private val cardTitleListener: CardTitleView.CardTitleListener,
                            private val imagePostListener: ImagePostViewHolder.ImagePostListener,
                            private val youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                            private val pollOptionListener: PollAdapter.PollOptionListener,
                            private val gridItemListener: GridPostAdapter.GridItemListener)
    : AbstractViewHolder<DynamicPostViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post

        const val PAYLOAD_LIKE = 13
        const val PAYLOAD_COMMENT = 14
        const val PAYLOAD_FOLLOW = 15

        const val MAX_CHAR = 140
        const val CAPTION_END = 90

        const val NEWLINE = "(\r\n|\n)"
    }

    override fun bind(element: DynamicPostViewModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        bindTitle(element.title, element.template.cardpost.title)
        bindHeader(element.id, element.header, element.template.cardpost.header)
        bindCaption(element.caption, element.template.cardpost.body)
        bindContentList(element.id, element.contentList, element.template.cardpost.body)
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
            PAYLOAD_FOLLOW -> bindFollow(element.header.followCta)
            else -> bind(element)
        }
    }

    fun onViewRecycled() {
        itemView.authorImage.clearImage()
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

    private fun bindHeader(postId: Int, header: Header, template: TemplateHeader) {
        itemView.header.shouldShowWithAction(shouldShowHeader(template)) {
            itemView.authorImage.shouldShowWithAction(template.avatar) {
                if (!TextUtils.isEmpty(header.avatar)) {
                    itemView.authorImage.loadImageCircle(header.avatar)
                } else {
                    itemView.authorImage.setImageDrawable(
                            MethodChecker.getDrawable(itemView.context, R.drawable.error_drawable)
                    )
                }
                itemView.authorImage.setOnClickListener { onAvatarClick(header.avatarApplink) }
            }

            itemView.authorTitle.shouldShowWithAction(template.avatarTitle) {
                itemView.authorTitle.text = header.avatarTitle
                itemView.authorTitle.setOnClickListener { onAvatarClick(header.avatarApplink) }
            }

            itemView.authorSubtitile.shouldShowWithAction(template.avatarDate) {
                header.avatarDate = TimeConverter.generateTime(itemView.context, header.avatarDate)
                itemView.authorSubtitile.text = header.avatarDate
                itemView.authorSubtitile.setOnClickListener { onAvatarClick(header.avatarApplink) }
            }

            itemView.headerAction.shouldShowWithAction(template.followCta) {
                bindFollow(header.followCta)
            }

            itemView.menu.shouldShowWithAction(template.report) {
                itemView.menu.setOnClickListener {
                    listener.onMenuClick(adapterPosition, postId, header.reportable, header.deletable, header.editable)
                }
            }
        }
    }

    private fun onAvatarClick(redirectUrl: String) {
        listener.onAvatarClick(adapterPosition, redirectUrl)
    }

    private fun bindFollow(followCta: FollowCta) {
        if (followCta.isFollow) {
            itemView.headerAction.text = followCta.textTrue
            itemView.headerAction.buttonCompatType = ButtonCompat.SECONDARY
        } else {
            itemView.headerAction.text = followCta.textFalse
            itemView.headerAction.buttonCompatType = ButtonCompat.PRIMARY
        }

        itemView.headerAction.setOnClickListener {
            listener.onHeaderActionClick(
                    adapterPosition,
                    followCta.authorID,
                    followCta.authorType,
                    followCta.isFollow
            )
        }
    }


    private fun shouldShowHeader(template: TemplateHeader): Boolean {
        return template.avatar || template.avatarBadge || template.avatarDate
                || template.avatarTitle || template.followCta || template.report
    }

    private fun bindCaption(caption: Caption, template: TemplateBody) {
        itemView.caption.shouldShowWithAction(template.caption) {
            if (caption.text.length > MAX_CHAR) {
                val captionText = caption.text.substring(0, CAPTION_END)
                        .replace(NEWLINE, "<br />")
                        .plus("... ")
                        .plus("<font color='#42b549'><b>")
                        .plus(caption.buttonName)
                        .plus("</b></font>")

                itemView.caption.text = MethodChecker.fromHtml(captionText)
                itemView.caption.setOnClickListener {
                    if (!TextUtils.isEmpty(caption.appLink)) {
                        listener.onCaptionClick(adapterPosition, caption.appLink)
                    } else {
                        itemView.caption.text = caption.text
                    }
                }
            } else {
                itemView.caption.text = caption.text.replace(NEWLINE, " ")
            }
        }
    }

    private fun bindContentList(postId: Int,
                                contentList: MutableList<BasePostViewModel>,
                                template: TemplateBody) {
        itemView.contentLayout.shouldShowWithAction(template.media) {
            contentList.forEach { it.postId = postId }
            contentList.forEach { it.positionInFeed = adapterPosition }

            val adapter = PostPagerAdapter(imagePostListener, youtubePostListener, pollOptionListener, gridItemListener)
            adapter.setList(contentList)
            itemView.contentViewPager.adapter = adapter
            itemView.contentViewPager.offscreenPageLimit = adapter.count
            itemView.tabLayout.setupWithViewPager(itemView.contentViewPager)
            itemView.tabLayout.visibility = if (adapter.count > 1) View.VISIBLE else View.GONE
        }
    }

    private fun bindFooter(id: Int, footer: Footer, template: TemplateFooter) {
        itemView.footer.shouldShowWithAction(shouldShowFooter(template)) {
            if (template.ctaLink && !TextUtils.isEmpty(footer.buttonCta.text)) {
                itemView.footerAction.show()
                itemView.footerAction.text = footer.buttonCta.text
                itemView.footerAction.setOnClickListener { listener.onFooterActionClick(adapterPosition, footer.buttonCta.appLink) }
            } else {
                itemView.footerAction.hide()
            }

            if (template.like) {
                itemView.likeIcon.show()
                itemView.likeText.show()
                itemView.likeIcon.setOnClickListener { listener.onLikeClick(adapterPosition, id, footer.like.isChecked) }
                itemView.likeText.setOnClickListener { listener.onLikeClick(adapterPosition, id, footer.like.isChecked) }
                bindLike(footer.like)
            } else {
                itemView.likeIcon.hide()
                itemView.likeText.hide()
            }

            if (template.comment) {
                itemView.commentIcon.show()
                itemView.commentText.show()
                itemView.commentIcon.setOnClickListener { listener.onCommentClick(adapterPosition, id) }
                itemView.commentText.setOnClickListener { listener.onCommentClick(adapterPosition, id) }
                bindComment(footer.comment)
            } else {
                itemView.commentIcon.hide()
                itemView.commentText.hide()
            }

            if (template.share) {
                itemView.shareIcon.show()
                itemView.shareText.show()
                itemView.shareText.text = footer.share.text
                itemView.shareIcon.setOnClickListener {
                    listener.onShareClick(
                            adapterPosition,
                            id,
                            footer.share.title,
                            footer.share.description,
                            footer.share.url,
                            footer.share.imageUrl
                    )
                }
                itemView.shareText.setOnClickListener {
                    listener.onShareClick(
                            adapterPosition,
                            id,
                            footer.share.title,
                            footer.share.description,
                            footer.share.url,
                            footer.share.imageUrl
                    )
                }

            } else {
                itemView.shareIcon.hide()
                itemView.shareText.hide()
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
        fun onAvatarClick(positionInFeed: Int, redirectUrl: String)

        fun onHeaderActionClick(positionInFeed: Int, id: String, type: String, isFollow: Boolean)

        fun onMenuClick(positionInFeed: Int, postId: Int, reportable: Boolean, deletable: Boolean, editable: Boolean)

        fun onCaptionClick(positionInFeed: Int, redirectUrl: String)

        fun onLikeClick(positionInFeed: Int,  id: Int, isLiked: Boolean)

        fun onCommentClick(positionInFeed: Int,  id: Int)

        fun onShareClick(positionInFeed: Int, id: Int, title: String, description: String, url: String, iamgeUrl: String)

        fun onFooterActionClick(positionInFeed: Int, redirectUrl: String)
    }
}
