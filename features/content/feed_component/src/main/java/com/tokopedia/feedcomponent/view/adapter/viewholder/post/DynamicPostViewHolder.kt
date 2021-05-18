package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.os.Handler
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.*
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateBody
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateHeader
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle
import com.tokopedia.feedcomponent.util.ColorUtil
import com.tokopedia.feedcomponent.util.TagConverter
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.adapter.post.PostPagerAdapter
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagAdapter
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagTypeFactoryImpl
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.BasePostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.CtaPostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.item_dynamic_post.view.*
import kotlinx.android.synthetic.main.item_posttag.view.*
import java.net.URLEncoder

/**
 * @author by milhamj on 28/11/18.
 */
open class DynamicPostViewHolder(v: View,
                                 private val listener: DynamicPostListener,
                                 private val cardTitleListener: CardTitleView.CardTitleListener,
                                 private val imagePostListener: ImagePostViewHolder.ImagePostListener,
                                 private val youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                                 private val pollOptionListener: PollAdapter.PollOptionListener,
                                 private val gridItemListener: GridPostAdapter.GridItemListener,
                                 private val videoViewListener: VideoViewHolder.VideoViewListener,
                                 private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener,
                                 private val userSession: UserSessionInterface)
    : AbstractViewHolder<DynamicPostViewModel>(v) {

    var captionTv: Typography = itemView.caption
    lateinit var adapter: PostPagerAdapter

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post

        const val PAYLOAD_LIKE = 13
        const val PAYLOAD_COMMENT = 14
        const val PAYLOAD_FOLLOW = 15
        const val PAYLOAD_ANIMATE_FOOTER = 16
        const val PAYLOAD_PLAY_VIDEO = 17

        const val MAX_CHAR = 140
        const val CAPTION_END = 90

        const val NEWLINE = "(\r\n|\n)"

        const val TYPE_DETAIL = "detail"
        const val SOURCE_FEEDS = "feeds"
        const val SOURCE_PROFILE = "profile"
        const val SOURCE_SHOP = "shop"
        const val SOURCE_DETAIL = "detail"


        const val POSTTAG_PRODUCT = "product"
        const val POSTTAG_BUTTONCTA = "buttoncta"
    }

    override fun bind(element: DynamicPostViewModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        bindTitle(element.title, element.template.cardpost.title)
        bindHeader(element.id, element.header, element.template.cardpost.header, element.activityName)
        bindCaption(element.caption, element.template.cardpost.body, element.trackingPostModel)
        bindContentList(element.id, element.contentList, element.template.cardpost.body, element.feedType)
        bindPostTag(element.id, element.postTag, element.template.cardpost.body, element.feedType, element.header.followCta.authorType)
        bindFooter(element.id, element.footer, element.template.cardpost.footer, isPostTagAvailable(element.postTag))
        bindTracking(element.impressHolder, element.tracking)

        val cvDynamicPostFeed = itemView.findViewById<CardView>(R.id.cv_dynamic_post_feed)
        if (itemView.context.isDarkMode()) {
            cvDynamicPostFeed.setCardBackgroundColor(
                    ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N50
                    )
            )
        } else {
            cvDynamicPostFeed.setCardBackgroundColor(
                    ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
            )
        }
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
            PAYLOAD_ANIMATE_FOOTER -> animateFooter()
            PAYLOAD_PLAY_VIDEO -> bindContentList(element.id, element.contentList, element.template.cardpost.body, element.feedType)
            else -> bind(element)
        }
    }

    override fun onViewRecycled() {
        itemView.authorImage.clearImage()
    }

    private fun bindTitle(title: Title, template: TemplateTitle) {
        itemView.cardTitle.shouldShowWithAction(shouldShowTitle(template)) {
            itemView.cardTitle.bind(title, template, adapterPosition)
        }
        itemView.cardTitle.listener = cardTitleListener
    }

    private fun shouldShowTitle(template: TemplateTitle): Boolean {
        return template.text || template.textBadge || template.ctaLink
    }

    private fun bindHeader(postId: Int, header: Header, template: TemplateHeader, activityName: String) {
        itemView.header.shouldShowWithAction(shouldShowHeader(template)) {
            itemView.authorImage.shouldShowWithAction(template.avatar) {
                if (!TextUtils.isEmpty(header.avatar)) {
                    itemView.authorImage.loadImageCircle(header.avatar)
                } else {
                    itemView.authorImage.setImageDrawable(
                            MethodChecker.getDrawable(itemView.context, R.drawable.error_drawable)
                    )
                }
                itemView.authorImage.setOnClickListener { onAvatarClick(header.avatarApplink, postId, activityName, header.followCta) }
            }

            if (template.avatarBadge && header.avatarBadgeImage.isNotBlank()) {
                itemView.authorBadge.show()
                itemView.authorBadge.loadImage(header.avatarBadgeImage)
                itemView.authorTitle.setMargin(itemView.getDimens(R.dimen.dp_4), 0, itemView.getDimens(R.dimen.dp_8), 0)
            } else {
                itemView.authorBadge.hide()
                itemView.authorTitle.setMargin(itemView.getDimens(R.dimen.dp_8), 0, itemView.getDimens(R.dimen.dp_8), 0)
            }

            itemView.authorTitle.shouldShowWithAction(template.avatarTitle) {
                itemView.authorTitle.text = MethodChecker.fromHtml(header.avatarTitle)
                itemView.authorTitle.setOnClickListener { onAvatarClick(header.avatarApplink, postId, activityName, header.followCta) }
            }

            itemView.authorSubtitile.shouldShowWithAction(template.avatarDate) {
                header.avatarDate = TimeConverter.generateTime(itemView.context, header.avatarDate)
                val spannableString: SpannableString =
                        if (header.cardSummary.isNotEmpty()) {
                            SpannableString(String.format(
                                    getString(R.string.feed_header_time_format),
                                    header.avatarDate,
                                    header.cardSummary))
                        } else {
                            SpannableString(header.avatarDate)
                        }
                itemView.authorSubtitile.text = spannableString
                itemView.authorSubtitile.setOnClickListener { onAvatarClick(header.avatarApplink, postId, activityName, header.followCta) }
            }

            itemView.headerAction.shouldShowWithAction(template.followCta
                    && header.followCta.authorID != userSession.userId) {
                bindFollow(header.followCta)
            }

            itemView.menu.shouldShowWithAction(template.report) {
                if (canShowMenu(header.reportable, header.deletable, header.editable)) {
                    itemView.menu.setOnClickListener {
                        listener.onMenuClick(adapterPosition, postId, header.reportable, header.deletable, header.editable)
                    }
                } else{
                    itemView.menu.hide()
                }
            }
        }
    }

    private fun canShowMenu(reportable: Boolean, deletable: Boolean, editable: Boolean): Boolean {
        return reportable || deletable || editable
    }

    private fun onAvatarClick(redirectUrl: String, activityId: Int, activityName: String, followCta: FollowCta) {
        listener.onAvatarClick(adapterPosition, redirectUrl, activityId, activityName, followCta)
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

    private fun animateFooter() {
        Handler().postDelayed({
            itemView.footerBackground.animation = AnimationUtils.loadAnimation(itemView.context, R.anim.anim_fade_in)
            itemView.footerBackground.visibility = View.VISIBLE
        }, 2000)
    }


    private fun shouldShowHeader(template: TemplateHeader): Boolean {
        return template.avatar || template.avatarBadge || template.avatarDate
                || template.avatarTitle || template.followCta || template.report
    }

    open fun bindCaption(caption: Caption, template: TemplateBody, trackingPostModel: TrackingPostModel) {
        val tagConverter = TagConverter()
        itemView.caption.shouldShowWithAction(template.caption) {
            if (caption.text.isEmpty()) {
                itemView.caption.visibility = View.GONE
            } else if (caption.text.length > MAX_CHAR ||
                   hasSecondLine(caption)) {
                itemView.caption.visibility = View.VISIBLE
                val captionEnd = if (findSubstringSecondLine(caption) < CAPTION_END)
                    findSubstringSecondLine(caption)
                else
                    CAPTION_END
                val captionText = caption.text.substring(0, captionEnd)
                        .replace("\n","<br/>")
                        .replace(NEWLINE, "<br/>")
                        .plus("... ")
                        .plus("<font color='${ColorUtil.getColorFromResToString(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G400)}'>" + "<b>")
                        .plus(caption.buttonName)
                        .plus("</b></font>")

                itemView.caption.text = tagConverter.convertToLinkifyHashtag(
                        SpannableString(MethodChecker.fromHtml(captionText)), colorLinkHashtag) { hashtag -> onHashtagClicked(hashtag, trackingPostModel) }
                itemView.caption.setOnClickListener {
                    if (!TextUtils.isEmpty(caption.appLink)) {
                        listener.onCaptionClick(adapterPosition, caption.appLink)
                    } else {
                        if (itemView.caption.text.endsWith(caption.buttonName)) listener.onReadMoreClicked(trackingPostModel)

                        itemView.caption.text = tagConverter.convertToLinkifyHashtag(SpannableString(caption.text),
                                colorLinkHashtag) { hashtag -> onHashtagClicked(hashtag, trackingPostModel) }
                    }
                }
                itemView.caption.movementMethod = LinkMovementMethod.getInstance()
            } else {
                itemView.caption.text = tagConverter
                        .convertToLinkifyHashtag(SpannableString(caption.text.replace(NEWLINE, " ")),
                                colorLinkHashtag) { hashtag -> onHashtagClicked(hashtag, trackingPostModel) }
                itemView.caption.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    private val colorLinkHashtag: Int
        get() = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G400)

    private fun onHashtagClicked(hashtag: String, trackingPostModel: TrackingPostModel){
        val encodeHashtag = URLEncoder.encode(hashtag)
        RouteManager.route(itemView.context, ApplinkConstInternalContent.HASHTAG_PAGE, encodeHashtag)
        listener.onHashtagClicked(hashtag, trackingPostModel)
    }

    private fun hasSecondLine(caption: Caption): Boolean {
        val firstIndex = caption.text.indexOf("\n", 0)
        return caption.text.indexOf("\n", firstIndex + 1) != -1
    }

    private fun findSubstringSecondLine(caption: Caption): Int {
        val firstIndex = caption.text.indexOf("\n", 0)
        return if (hasSecondLine(caption)) caption.text.indexOf("\n",
                firstIndex + 1) else caption.text.length
    }

    private fun bindContentList(postId: Int,
                                contentList: MutableList<BasePostViewModel>,
                                template: TemplateBody,
                                feedType: String) {
        itemView.contentLayout.shouldShowWithAction(template.media && contentList.size !=0) {
            contentList.forEach { it.postId = postId }
            contentList.forEach { it.positionInFeed = adapterPosition }

            adapter = PostPagerAdapter(imagePostListener,
                    youtubePostListener,
                    pollOptionListener,
                    gridItemListener,
                    videoViewListener,
                    feedMultipleImageViewListener,
                    feedType)
            adapter.setList(contentList)
            itemView.contentViewPager.adapter = adapter
            itemView.contentViewPager.offscreenPageLimit = adapter.count
            itemView.tabLayout.setupWithViewPager(itemView.contentViewPager)
            itemView.tabLayout.visibility = if (adapter.count > 1) View.VISIBLE else View.GONE
        }
    }

    private fun bindFooter(id: Int, footer: Footer, template: TemplateFooter, isPostTagAvailable: Boolean) {
        itemView.footer.shouldShowWithAction(shouldShowFooter(template)) {
            itemView.footerBackground.visibility = View.GONE
            if (template.ctaLink && !TextUtils.isEmpty(footer.buttonCta.text) && !isPostTagAvailable) {
                itemView.layoutFooterAction.show()
                itemView.footerAction.text = footer.buttonCta.text
                itemView.footerAction.setOnClickListener { listener.onFooterActionClick(adapterPosition, footer.buttonCta.appLink) }
            } else {
                itemView.layoutFooterAction.hide()
            }

            if (template.like) {
                itemView.likeGroup.show()
                itemView.likeIcon.setOnClickListener { listener.onLikeClick(adapterPosition, id, footer.like.isChecked) }
                itemView.likeText.setOnClickListener { listener.onLikeClick(adapterPosition, id, footer.like.isChecked) }
                bindLike(footer.like)
            } else {
                itemView.likeGroup.hide()
            }

            if (template.comment) {
                itemView.commentGroup.show()
                itemView.commentIcon.setOnClickListener { listener.onCommentClick(adapterPosition, id) }
                itemView.commentText.setOnClickListener { listener.onCommentClick(adapterPosition, id) }
                bindComment(footer.comment)
            } else {
                itemView.commentGroup.hide()
            }

            if (template.share) {
                itemView.shareGroup.show()
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
                itemView.shareGroup.hide()
            }

            if (template.stats) {
                itemView.statsIcon.shouldShowWithAction(true) {
                    itemView.statsIcon.setOnClickListener { listener.onStatsClick(footer.stats.text, id.toString(), footer.stats.productIDs, footer.like.value, footer.comment.value) }
                }
            } else itemView.statsIcon.hide()
        }
    }

    private fun shouldShowFooter(template: TemplateFooter): Boolean {
        return template.comment || template.ctaLink || template.like || template.share || template.stats
    }

    private fun bindLike(like: Like) {
        when {
            like.isChecked -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_green)
                val likeCount = if (like.fmt.isEmpty()) like.value.toString() else like.fmt
                itemView.likeText.text = likeCount
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeText.context, com.tokopedia.unifyprinciples.R.color.Unify_G400)
                )
            }
            like.value > 0 -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_feed_thumb)
                itemView.likeText.text = like.fmt
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeText.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
                )
            }
            else -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_feed_thumb)
                val text : String  = if (like.fmt.isNotEmpty() && !like.fmt.equals("0")) like.fmt else getString(R.string.kol_action_like)
                itemView.likeText.text = text
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeIcon.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
                )
            }
        }
    }

    private fun bindComment(comment: Comment) {
        itemView.commentText.text =
                if (comment.value == 0) if(comment.fmt.isNotEmpty()) comment.fmt else getString(R.string.kol_action_comment)
                else {
                    if (comment.fmt.isNotEmpty()) comment.fmt else comment.value.toString()
                }
    }

    private fun bindPostTag(postId: Int, postTag: PostTag, template: TemplateBody, feedType: String, authorType: String) {
        itemView.layoutPostTag.shouldShowWithAction(shouldShowPostTag(postTag, template)) {
            if (postTag.text.isNotEmpty()) {
                itemView.cardTitlePostTag.text = postTag.text
                itemView.cardTitlePostTag.show()
            } else {
                itemView.cardTitlePostTag.hide()
            }
            if (postTag.totalItems > 0) {
                itemView.rvPosttag.show()
                itemView.rvPosttag.setHasFixedSize(true)
                val layoutManager: RecyclerView.LayoutManager = when (feedType) {
                    SOURCE_DETAIL -> LinearLayoutManager(itemView.context)
                    else -> LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                }
                itemView.rvPosttag.isNestedScrollingEnabled = false
                itemView.rvPosttag.layoutManager = layoutManager
                itemView.rvPosttag.adapter = PostTagAdapter(mapPostTag(postTag.items, feedType, postId, adapterPosition, authorType),
                        PostTagTypeFactoryImpl(listener,  DeviceScreenInfo.getScreenWidth(itemView.context)))
                (itemView.rvPosttag.adapter as PostTagAdapter).notifyDataSetChanged()
            } else {
                itemView.rvPosttag.hide()
            }
        }
    }

    private fun shouldShowPostTag(postTag: PostTag, template: TemplateBody): Boolean {
        return template.postTag || isPostTagAvailable(postTag)
    }

    private fun isPostTagAvailable(postTag: PostTag): Boolean {
        return postTag.totalItems != 0 || postTag.items.isNotEmpty()
    }

    private fun mapPostTag(postTagItemList: List<PostTagItem>, feedType: String, postId: Int, positionInFeed: Int, authorType: String): MutableList<BasePostTagViewModel> {
        val needToRezise = postTagItemList.size > 1
        val itemList: MutableList<BasePostTagViewModel> = ArrayList()
        for (postTagItem in postTagItemList) {
            when (postTagItem.type) {
                POSTTAG_PRODUCT -> {
                    val item = ProductPostTagViewModel(
                            postTagItem.id,
                            postTagItem.text,
                            postTagItem.price,
                            postTagItem.type,
                            postTagItem.applink,
                            postTagItem.weblink,
                            postTagItem.thumbnail,
                            postTagItem.percentage,
                            postTagItem.isSelected,
                            postTagItem.position,
                            postTagItem.isWishlisted,
                            postTagItem.tags,
                            postTagItem,
                            postTagItem.rating,
                            needToRezise,
                            authorType
                    )
                    item.feedType = feedType
                    item.postId = postId
                    item.positionInFeed = positionInFeed
                    itemList.add(item)
                }
                POSTTAG_BUTTONCTA -> {
                    val item = CtaPostTagViewModel(
                            postTagItem.id,
                            postTagItem.text,
                            postTagItem.type,
                            postTagItem.applink,
                            postTagItem.weblink,
                            postTagItem.position,
                            postTagItem)
                    item.feedType = feedType
                    item.postId = postId
                    item.positionInFeed = positionInFeed
                    itemList.add(item)
                }
            }
        }
        return itemList
    }

    private fun bindTracking(impressHolder: ImpressHolder, trackList: MutableList<TrackingViewModel>) {
        itemView.addOnImpressionListener(impressHolder) {
            listener.onAffiliateTrackClicked(trackList, false)
        }
    }

    interface DynamicPostListener {
        fun onAvatarClick(positionInFeed: Int, redirectUrl: String, activityId: Int, activityName: String, followCta: FollowCta)

        fun onHeaderActionClick(positionInFeed: Int, id: String, type: String, isFollow: Boolean)

        fun onMenuClick(positionInFeed: Int, postId: Int, reportable: Boolean, deletable: Boolean, editable: Boolean)

        fun onCaptionClick(positionInFeed: Int, redirectUrl: String)

        fun onLikeClick(positionInFeed: Int, id: Int, isLiked: Boolean)

        fun onCommentClick(positionInFeed: Int, id: Int)

        fun onStatsClick(title: String, activityId: String, productIds: List<String>, likeCount: Int, commentCount: Int)

        fun onShareClick(positionInFeed: Int, id: Int, title: String, description: String, url: String, imageUrl: String)

        fun onFooterActionClick(positionInFeed: Int, redirectUrl: String)

        fun onPostTagItemClick(positionInFeed: Int, redirectUrl: String, postTagItem: PostTagItem, itemPosition: Int)

        fun onAffiliateTrackClicked(trackList: List<TrackingViewModel>, isClick: Boolean)

        fun onPostTagItemBuyClicked(positionInFeed: Int, postTagItem: PostTagItem, authorType: String)

        fun onHashtagClicked(hashtagText: String, trackingPostModel: TrackingPostModel)

        fun onReadMoreClicked(trackingPostModel: TrackingPostModel)
    }
}
