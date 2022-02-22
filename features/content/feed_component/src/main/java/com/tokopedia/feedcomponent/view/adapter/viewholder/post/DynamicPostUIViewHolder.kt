package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.os.Handler
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
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
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXComments
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.*
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle
import com.tokopedia.feedcomponent.domain.mapper.TYPE_IMAGE
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
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoViewModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.BasePostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.item_dynamic_post.view.*
import kotlinx.android.synthetic.main.item_posttag.view.*
import java.net.URLEncoder

/**
 * @author by milhamj on 28/11/18.
 */
open class DynamicPostUIViewHolder(v: View,
                                   private val listener: DynamicPostViewHolder.DynamicPostListener,
                                   private val cardTitleListener: CardTitleView.CardTitleListener,
                                   private val imagePostListener: ImagePostViewHolder.ImagePostListener,
                                   private val youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                                   private val pollOptionListener: PollAdapter.PollOptionListener,
                                   private val gridItemListener: GridPostAdapter.GridItemListener,
                                   private val videoViewListener: VideoViewHolder.VideoViewListener,
                                   private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener,
                                   private val userSession: UserSessionInterface)
    : AbstractViewHolder<DynamicPostUiModel>(v) {

    lateinit var adapter: PostPagerAdapter

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_ui_post

        const val PAYLOAD_LIKE = 13
        const val PAYLOAD_COMMENT = 14
        const val PAYLOAD_FOLLOW = 15
        const val PAYLOAD_ANIMATE_FOOTER = 16
        const val PAYLOAD_PLAY_VIDEO = 17
        const val PAYLOAD_PLAY_VOD = 18

        const val MAX_CHAR = 140
        const val CAPTION_END = 90

        const val NEWLINE = "(\r\n|\n)"


        private const val CONTENT_IMAGE = "image"
        private const val CONTENT_VIDEO = "video"
        private const val CONTENT_LONG_VIDEO = "long-video"


        const val ANIMATION_DURATION = 2000L
    }

    override fun bind(element: DynamicPostUiModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        bindTitle(element.feedXCard)
        bindHeader(element.feedXCard)
        bindCaption(element.feedXCard, element.trackingPostModel)
        bindContentList(element.feedXCard.id.toIntOrZero(), element.feedXCard)
        bindPostTag(element.feedXCard.id.toIntOrZero(), element.feedXCard)
        bindFooter(element.feedXCard)
    }

    override fun bind(element: DynamicPostUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty() || payloads[0] !is Int) {
            return
        }

        when (payloads[0] as Int) {
            PAYLOAD_LIKE -> bindLike(element.feedXCard)
            PAYLOAD_COMMENT -> bindComment(element.feedXCard.comments)
            PAYLOAD_FOLLOW -> bindFollow(element.feedXCard)
            PAYLOAD_ANIMATE_FOOTER -> animateFooter()
            PAYLOAD_PLAY_VIDEO -> bindContentList(element.feedXCard.id.toIntOrZero(), element.feedXCard)
            PAYLOAD_PLAY_VOD -> bindContentList(element.feedXCard.id.toIntOrZero(), element.feedXCard)
            else -> bind(element)
        }
    }

    override fun onViewRecycled() {
        itemView.authorImage.clearImage()
    }

    private fun bindTitle(feedXCard: FeedXCard) {
        val action = Action("",action = "", appLink = feedXCard.appLink, feedXCard.webLink)
        val title = Title(feedXCard.text,"",isIsClicked = false, ctaLink = CtaLink(),action = action )
        val template = TemplateTitle(text = true,textBadge = false, false )
        itemView.cardTitle.shouldShowWithAction(shouldShowTitle(template)) {
            itemView.cardTitle.bind(title, template, adapterPosition)
        }
        itemView.cardTitle.listener = cardTitleListener
    }

    private fun shouldShowTitle(template: TemplateTitle): Boolean {
        return template.text || template.textBadge || template.ctaLink
    }

    private fun bindHeader(feedXCard: FeedXCard) {
        var author = feedXCard.author
        val authorType = if (author.type == 1) FollowCta.AUTHOR_USER else FollowCta.AUTHOR_SHOP
        val followCta =
                FollowCta(authorID = author.id, authorType = authorType, isFollow = feedXCard.followers.isFollowed)
                if (!TextUtils.isEmpty(author.logoURL)) {
                    itemView.authorImage.loadImageCircle(author.logoURL)
                } else {
                    itemView.authorImage.setImageDrawable(
                            MethodChecker.getDrawable(itemView.context, com.tokopedia.topads.sdk.R.drawable.error_drawable)
                    )
                }


                itemView.authorImage.setOnClickListener {
                    onAvatarClick(author.appLink,
                        feedXCard.id.toIntOrZero(),
                        "",
                        followCta,
                        author.id)
                }

            if (author.badgeURL.isNotBlank()) {
                itemView.authorBadge.show()
                itemView.authorBadge.loadImage(author.badgeURL)
                itemView.authorTitle.setMargin(itemView.getDimens(com.tokopedia.feedcomponent.R.dimen.feed_component_dp_4), 0, itemView.getDimens(com.tokopedia.feedcomponent.R.dimen.feed_component_dp_8), 0)
            } else {
                itemView.authorBadge.hide()
                itemView.authorTitle.setMargin(itemView.getDimens(com.tokopedia.feedcomponent.R.dimen.feed_component_dp_8), 0, itemView.getDimens(com.tokopedia.feedcomponent.R.dimen.feed_component_dp_8), 0)
            }

            itemView.authorTitle.shouldShowWithAction(author.name.isNotEmpty()) {
                itemView.authorTitle.text = MethodChecker.fromHtml(author.name)
                itemView.authorTitle.setOnClickListener {
                    onAvatarClick(author.appLink,
                        feedXCard.id.toIntOrZero(),
                        "",
                        followCta,
                        author.id) }
            }

            itemView.authorSubtitile.shouldShowWithAction(feedXCard.publishedAt.isNotEmpty()) {
               val avatarDate = TimeConverter.generateTime(itemView.context, feedXCard.publishedAt)
                val spannableString: SpannableString =
                        if (feedXCard.subTitle.isNotEmpty()) {
                            SpannableString(String.format(
                                    getString(R.string.feed_header_time_format),
                                    avatarDate,
                                    feedXCard.subTitle))
                        } else {
                            SpannableString(avatarDate)
                        }
                itemView.authorSubtitile.text = spannableString
                itemView.authorSubtitile.setOnClickListener {
                    onAvatarClick(author.appLink,
                            feedXCard.id.toIntOrZero(),
                            "",
                            followCta,
                            author.id)
                }
            }

            itemView.headerAction.shouldShowWithAction(feedXCard.followers.isFollowed
                    && feedXCard.author.id != userSession.userId) {
                bindFollow(feedXCard)
            }

                if (canShowMenu(feedXCard.reportable, feedXCard.deletable, feedXCard.editable)) {
                    itemView.menu.setOnClickListener {
                        listener.onMenuClick(
                            adapterPosition,
                            feedXCard.id.toIntOrZero(),
                            feedXCard.reportable,
                            feedXCard.deletable,
                            feedXCard.editable,
                            true,
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                    }
                } else{
                    itemView.menu.hide()
                }
    }

    private fun canShowMenu(reportable: Boolean, deletable: Boolean, editable: Boolean): Boolean {
        return reportable || deletable || editable
    }

    private fun onAvatarClick(
        redirectUrl: String,
        activityId: Int,
        activityName: String,
        followCta: FollowCta,
        shopId: String,
    ) {
        listener.onAvatarClick(
            adapterPosition,
            redirectUrl,
            activityId,
            activityName,
            followCta,
            shopId = shopId,
            mediaType = "",
            isCaption = false
        )
    }

    private fun bindFollow(feedXCard: FeedXCard) {
        val followers = feedXCard.followers
        if (followers.isFollowed) {
            itemView.headerAction.text = "Following"
            itemView.headerAction.buttonCompatType = ButtonCompat.SECONDARY
        } else {
            itemView.headerAction.text = getString(com.tokopedia.feedcomponent.R.string.feed_component_follow)
            itemView.headerAction.buttonCompatType = ButtonCompat.PRIMARY
        }
        val authorType = if (feedXCard.author.type == 1) FollowCta.AUTHOR_USER else FollowCta.AUTHOR_SHOP


        itemView.headerAction.setOnClickListener {
            listener.onHeaderActionClick(
                adapterPosition,
                feedXCard.author.id,
                authorType,
                followers.isFollowed,
                isVideo = false
            )
        }
    }

    private fun animateFooter() {
        Handler().postDelayed({
            itemView.footerBackground.animation = AnimationUtils.loadAnimation(itemView.context, R.anim.anim_fade_in)
            itemView.footerBackground.visibility = View.VISIBLE
        }, ANIMATION_DURATION)
    }



    open fun bindCaption(feedXCard: FeedXCard, trackingPostModel: TrackingPostModel) {
        val tagConverter = TagConverter()
        itemView.caption.shouldShowWithAction(feedXCard.text.isNotEmpty()) {
            if (feedXCard.text.isEmpty()) {
                itemView.caption.visibility = View.GONE
            } else if (feedXCard.text.length > MAX_CHAR ||
                   hasSecondLine(feedXCard.text)) {
                itemView.caption.visibility = View.VISIBLE
                val captionEnd = if (findSubstringSecondLine(feedXCard.text) < CAPTION_END)
                    findSubstringSecondLine(feedXCard.text)
                else
                    CAPTION_END
                val captionText = feedXCard.text.substring(0, captionEnd)
                        .replace("\n","<br/>")
                        .replace(NEWLINE, "<br/>")
                        .plus("... ")
                        .plus("<font color='${ColorUtil.getColorFromResToString(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G400)}'>" + "<b>")
                        .plus(getString(R.string.feed_component_read_more_button))
                        .plus("</b></font>")

                itemView.caption.text = tagConverter.convertToLinkifyHashtag(
                        SpannableString(MethodChecker.fromHtml(captionText)), colorLinkHashtag) { hashtag -> onHashtagClicked(hashtag, trackingPostModel) }
                itemView.caption.setOnClickListener {
                        if (itemView.caption.text.endsWith(getString(R.string.feed_component_read_more_button))) listener.onReadMoreClicked(trackingPostModel)

                        itemView.caption.text = tagConverter.convertToLinkifyHashtag(SpannableString(feedXCard.text),
                                colorLinkHashtag) { hashtag -> onHashtagClicked(hashtag, trackingPostModel) }

                }
                itemView.caption.movementMethod = LinkMovementMethod.getInstance()
            } else {
                itemView.caption.text = tagConverter
                        .convertToLinkifyHashtag(SpannableString(feedXCard.text.replace(NEWLINE, " ")),
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

    private fun hasSecondLine(caption: String): Boolean {
        val firstIndex = caption.indexOf("\n", 0)
        return caption.indexOf("\n", firstIndex + 1) != -1
    }

    private fun findSubstringSecondLine(caption: String): Int {
        val firstIndex = caption.indexOf("\n", 0)
        return if (hasSecondLine(caption)) caption.indexOf("\n",
                firstIndex + 1) else caption.length
    }

    private fun bindContentList(postId: Int,
                                feedXCard: FeedXCard) {
        var contentList = mapPostContent(feedXCard)
        itemView.contentLayout.shouldShowWithAction(contentList.size !=0) {
            contentList.forEach { it.postId = postId }
            contentList.forEach { it.positionInFeed = adapterPosition }

            adapter = PostPagerAdapter(imagePostListener,
                    youtubePostListener,
                    pollOptionListener,
                    gridItemListener,
                    videoViewListener,
                    feedMultipleImageViewListener,
                    "")
            adapter.setList(contentList)
            itemView.contentViewPager.adapter = adapter
            itemView.contentViewPager.offscreenPageLimit = adapter.count
            itemView.tabLayout.setupWithViewPager(itemView.contentViewPager)
            //Visibility gone
            itemView.tabLayout.visibility =  View.GONE
        }
    }
    private fun mapPostContent(feedXCard: FeedXCard): MutableList<BasePostViewModel> {
        val list: MutableList<BasePostViewModel> = ArrayList()
        for (media in feedXCard.media) {
            when (media.type) {
                CONTENT_IMAGE -> list.add(mapPostImage(media))
                CONTENT_LONG_VIDEO  ->if (media.coverUrl.isNotBlank()
                        && media.mediaUrl.isNotBlank()) {
                    list.add(mapPostVideo(media))
                }
                CONTENT_VIDEO -> {
                    if (media.coverUrl.isNotBlank()
                            && media.mediaUrl.isNotBlank()) {
                        list.add(mapPostVideo(media))
                    }
                }
            }
        }

        return list
    }
    private fun mapPostImage(media: FeedXMedia): ImagePostViewModel {
        return ImagePostViewModel(
               image= media.mediaUrl,
                redirectLink = media.webLink
        )
    }

    private fun mapPostVideo(media: FeedXMedia): VideoViewModel {
        return VideoViewModel(
                media.id,
                media.coverUrl,
                media.mediaUrl,
                redirectLink = media.appLink
        )
    }


    private fun bindFooter(feedXCard: FeedXCard) {
            itemView.footerBackground.visibility = View.GONE

            if (feedXCard.like.count > 0) {
                itemView.likeGroup.show()
                itemView.likeIcon.setOnClickListener {
                    listener.onLikeClick(
                        adapterPosition,
                        feedXCard.id.toIntOrZero(),
                        feedXCard.like.isLiked,
                        "",
                        false,
                        false, "", ""
                    )
                }
                itemView.likeText.setOnClickListener {
                    listener.onLikeClick(
                        adapterPosition,
                        feedXCard.id.toIntOrZero(),
                        feedXCard.like.isLiked,
                        "",
                        isFollowed = false, type = false, "", ""
                    )
                }
                bindLike(feedXCard)
            } else {
                itemView.likeGroup.hide()
            }

            if (feedXCard.comments.count > 0) {
                itemView.commentGroup.show()
                itemView.commentIcon.setOnClickListener {
                    listener.onCommentClick(
                        adapterPosition,
                        feedXCard.id.toIntOrZero(),
                        "",
                        "",
                        isFollowed = false,
                        ""
                    )
                }
                itemView.commentText.setOnClickListener {
                    listener.onCommentClick(
                        adapterPosition,
                        feedXCard.id.toIntOrZero(), "",
                        "",
                        isFollowed = false,
                        ""
                    )
                }
                bindComment(feedXCard.comments)
            } else {
                itemView.commentGroup.hide()
            }

                itemView.shareGroup.show()
                val desc = getString(R.string.feed_share_detail_format_text)

                itemView.shareIcon.setOnClickListener {
                    listener.onShareClick(
                        adapterPosition,
                            feedXCard.id.toIntOrZero(),
                            feedXCard.author.name + " `post",
                            desc.replace("%s", feedXCard.webLink),
                            feedXCard.webLink,
                            feedXCard.media.firstOrNull()?.mediaUrl ?: "",
                            false,
                            feedXCard.typename,
                            feedXCard.followers.isFollowed,
                            feedXCard.author.id,
                            feedXCard.media.firstOrNull()?.type?:""
                    )
                }
                itemView.shareText.setOnClickListener {
                    listener.onShareClick(
                            adapterPosition,
                            feedXCard.id.toIntOrZero(),
                            feedXCard.author.name + " `post",
                            desc.replace("%s", feedXCard.webLink),
                            feedXCard.webLink,
                            feedXCard.author.logoURL,
                            false,
                            feedXCard.typename,
                            feedXCard.followers.isFollowed,
                            feedXCard.author.id,
                           feedXCard.media.firstOrNull()?.type?:""
                    )
                }

                itemView.statsIcon.hide()

    }
    private fun isVideo(media: FeedXMedia?): Boolean {
        return media?.type != TYPE_IMAGE
    }
    private fun shouldShowFooter(template: TemplateFooter): Boolean {
        return template.comment || template.ctaLink || template.like || template.share || template.stats
    }

    private fun bindLike(feedXCard: FeedXCard) {
        val like = feedXCard.like
        when {
            like.isLiked -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_green)
                val likeCount = if (like.countFmt.isEmpty()) like.count.toString() else like.countFmt
                itemView.likeText.text = likeCount
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeText.context, com.tokopedia.unifyprinciples.R.color.Unify_G400)
                )
            }
            like.count > 0 -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_feed_thumb)
                itemView.likeText.text = like.countFmt
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeText.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
                )
            }
            else -> {
                itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_feed_thumb)
                val text : String  = if (like.countFmt.isNotEmpty() && !like.countFmt.equals("0")) like.countFmt else getString(R.string.kol_action_like)
                itemView.likeText.text = text
                itemView.likeText.setTextColor(
                        MethodChecker.getColor(itemView.likeIcon.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
                )
            }
        }
    }

    private fun bindComment(comments: FeedXComments) {
        itemView.commentText.text  =
                if (comments.count == 0) getString(com.tokopedia.feedcomponent.R.string.kol_action_comment)
                else comments.countFmt
    }


    private fun bindPostTag(postId: Int, feedXCard: FeedXCard) {
            if (feedXCard.tags.isNotEmpty()) {
                itemView.rvPosttag.show()
                itemView.rvPosttag.setHasFixedSize(true)
                val layoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                val authorType = if (feedXCard.author.type == 1) FollowCta.AUTHOR_USER else FollowCta.AUTHOR_SHOP

                itemView.rvPosttag.isNestedScrollingEnabled = false
                itemView.rvPosttag.layoutManager = layoutManager
                itemView.rvPosttag.adapter = PostTagAdapter(mapPostTag(feedXCard.tags, "", postId, adapterPosition, authorType),
                        PostTagTypeFactoryImpl(listener,  DeviceScreenInfo.getScreenWidth(itemView.context)))
                (itemView.rvPosttag.adapter as PostTagAdapter).notifyDataSetChanged()
            } else {
                itemView.rvPosttag.hide()
            }
    }


    private fun isPostTagAvailable(postTag: PostTag): Boolean {
        return postTag.totalItems != 0 || postTag.items.isNotEmpty()
    }

    private fun mapPostTag(postTagItemList: List<FeedXProduct>, feedType: String, postId: Int, positionInFeed: Int, authorType: String): MutableList<BasePostTagViewModel> {
        val needToRezise = postTagItemList.size > 1
        val itemList: MutableList<BasePostTagViewModel> = ArrayList()
        for (postTagItem in postTagItemList) {
                    val item = ProductPostTagViewModel(
                            postTagItem.id,
                            postTagItem.name,
                            postTagItem.priceFmt,
                            "",
                            postTagItem.appLink,
                            postTagItem.webLink,
                            postTagItem.coverURL,
                            postTagItem.discount.toString(),
                            false,
                            mutableListOf(),
                            isWishlisted = postTagItem.isWishlisted,
                            mutableListOf(),
                            mapPostTagItem(postTagItem, positionInFeed),
                            rating = postTagItem.star,
                            needToRezise,
                            authorType
                    )
                    item.feedType = feedType
                    item.postId = postId
                    item.positionInFeed = positionInFeed
                    itemList.add(item)
        }
        return itemList
    }
    private fun mapPostTagItem(feedXProduct: FeedXProduct, positionInFeed: Int): PostTagItem {
        return PostTagItem(
                id = feedXProduct.id,
                text = feedXProduct.name,
                price = feedXProduct.priceFmt,
                priceOriginal = feedXProduct.priceOriginalFmt,
                type = "",
                applink = feedXProduct.appLink,
                weblink = feedXProduct.webLink,
                thumbnail = feedXProduct.coverURL,
                percentage = feedXProduct.discountFmt,
                false,
                rating = feedXProduct.star)

    }

    private fun bindTracking(impressHolder: ImpressHolder, trackList: MutableList<TrackingViewModel>) {
        itemView.addOnImpressionListener(impressHolder) {
            listener.onAffiliateTrackClicked(trackList, false)
        }
    }

}
