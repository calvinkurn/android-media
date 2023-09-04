package com.tokopedia.feedcomponent.view.adapter.viewholder.post

import android.annotation.SuppressLint
import android.os.Handler
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Caption
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Comment
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Footer
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Header
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Like
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTag
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateBody
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateHeader
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.util.caption.FeedCaption
import com.tokopedia.feedcomponent.view.adapter.post.PostPagerAdapter
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagAdapter
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagTypeFactoryImpl
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.BasePostTagModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.CtaPostTagModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feedcomponent.view.widget.WrapContentViewPager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.URLEncoder
import com.tokopedia.content.common.R as contentcommonR

/**
 * @author by milhamj on 28/11/18.
 */
open class DynamicPostViewHolder(
    v: View,
    private val listener: DynamicPostListener,
    private val cardTitleListener: CardTitleView.CardTitleListener,
    private val imagePostListener: ImagePostViewHolder.ImagePostListener,
    private val youtubePostListener: YoutubeViewHolder.YoutubePostListener,
    private val pollOptionListener: PollAdapter.PollOptionListener,
    private val gridItemListener: GridPostAdapter.GridItemListener,
    private val videoViewListener: VideoViewHolder.VideoViewListener,
    private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener,
    private val userSession: UserSessionInterface
) : AbstractViewHolder<DynamicPostModel>(v) {

    private val viewItemPostTag: View = itemView.findViewById(R.id.layoutPostTag)
    private val authorTitle: Typography = itemView.findViewById(R.id.authorTitle)
    private val authorSubtitle: Typography = itemView.findViewById(R.id.authorSubtitile)
    private val footerBackground: View = itemView.findViewById(R.id.footerBackground)
    private val llFooter: LinearLayout = itemView.findViewById(R.id.footer)
    private val layoutFooterAction: FrameLayout = itemView.findViewById(R.id.layoutFooterAction)
    private val footerAction: Typography = itemView.findViewById(R.id.footerAction)
    private val likeGroup: Group = itemView.findViewById(R.id.likeGroup)
    private val likeText: Typography = itemView.findViewById(R.id.likeText)
    private val headerAction: ButtonCompat = itemView.findViewById(R.id.headerAction)
    private val ivMenu: ImageView = itemView.findViewById(R.id.menu)
    private val authorImage: ImageView = itemView.findViewById(R.id.authorImage)
    private val authorBadge: ImageView = itemView.findViewById(R.id.authorBadge)
    private val cardTitle: CardTitleView = itemView.findViewById(R.id.cardTitle)
    private val clHeader: ConstraintLayout = itemView.findViewById(R.id.header)
    private val likeIcon: IconUnify = itemView.findViewById(R.id.likeIcon)
    private val commentText: Typography = itemView.findViewById(R.id.commentText)
    private val tvCaption: Typography = itemView.findViewById(R.id.caption)
    private val contentLayout: RelativeLayout = itemView.findViewById(R.id.contentLayout)
    private val contentViewPager: WrapContentViewPager = itemView.findViewById(R.id.contentViewPager)
    private val tabLayout: TabLayout = itemView.findViewById(R.id.tabLayout)
    private val commentGroup: Group = itemView.findViewById(R.id.commentGroup)
    private val commentIcon: AppCompatImageView = itemView.findViewById(R.id.commentIcon)
    private val shareGroup: Group = itemView.findViewById(R.id.shareGroup)
    private val shareText: Typography = itemView.findViewById(R.id.shareText)
    private val shareIcon: AppCompatImageView = itemView.findViewById(R.id.shareIcon)
    private val statsIcon: AppCompatImageView = itemView.findViewById(R.id.statsIcon)

    private val cardTitlePostTag: Typography = viewItemPostTag.findViewById(R.id.cardTitlePostTag)
    private val rvPostTag: RecyclerView = viewItemPostTag.findViewById(R.id.rvPosttag)

    lateinit var adapter: PostPagerAdapter

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dynamic_post

        const val PAYLOAD_LIKE = 13
        const val PAYLOAD_COMMENT = 14
        const val PAYLOAD_FOLLOW = 15
        const val PAYLOAD_ANIMATE_FOOTER = 16
        const val PAYLOAD_PLAY_VIDEO = 17
        const val PAYLOAD_PLAY_VOD = 18
        const val MAX_CHAR = 140
        const val NEWLINE = "(\r\n|\n)"
        const val SOURCE_DETAIL = "detail"

        const val POSTTAG_PRODUCT = "product"
        const val POSTTAG_BUTTONCTA = "buttoncta"
        const val ANIMATION_DURATION = 2000L
    }

    override fun bind(element: DynamicPostModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        bindTitle(element.title, element.template.cardpost.title)
        bindHeader(
            element.id,
            element.header,
            element.template.cardpost.header,
            element.activityName,
            "",
            element.caption.text
        )
        bindCaption(element.caption, element.template.cardpost.body, element.trackingPostModel)
        bindContentList(
            element.id,
            element.contentList,
            element.template.cardpost.body,
            element.feedType
        )
        bindPostTag(
            element.id,
            element.postTag,
            element.template.cardpost.body,
            element.feedType,
            element.header.followCta.authorType
        )
        bindFooter(
            element.id,
            element.footer,
            element.template.cardpost.footer,
            isPostTagAvailable(element.postTag),
            element.header.followCta.authorType
        )
        bindTracking(element.impressHolder, element.tracking)
    }

    override fun bind(element: DynamicPostModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty() || payloads[0] !is Int) {
            return
        }

        when (payloads[0] as Int) {
            PAYLOAD_LIKE -> bindLike(element.footer.like)
            PAYLOAD_COMMENT -> bindComment(element.footer.comment)
            PAYLOAD_FOLLOW -> bindFollow(element.header.followCta)
            PAYLOAD_ANIMATE_FOOTER -> animateFooter()
            PAYLOAD_PLAY_VIDEO -> bindContentList(
                element.id,
                element.contentList,
                element.template.cardpost.body,
                element.feedType
            )
            PAYLOAD_PLAY_VOD -> bindContentList(
                element.id,
                element.contentList,
                element.template.cardpost.body,
                element.feedType
            )
            else -> bind(element)
        }
    }

    override fun onViewRecycled() {
        authorImage.clearImage()
    }

    private fun bindTitle(title: Title, template: TemplateTitle) {
        cardTitle.shouldShowWithAction(shouldShowTitle(template)) {
            cardTitle.bind(title, template, adapterPosition)
        }
        cardTitle.listener = cardTitleListener
    }

    private fun shouldShowTitle(template: TemplateTitle): Boolean {
        return template.text || template.textBadge || template.ctaLink
    }

    private fun bindHeader(
        postId: String,
        header: Header,
        template: TemplateHeader,
        activityName: String,
        shopId: String,
        caption: String
    ) {
        clHeader.shouldShowWithAction(shouldShowHeader(template)) {
            authorImage.shouldShowWithAction(template.avatar) {
                if (!TextUtils.isEmpty(header.avatar)) {
                    authorImage.loadImageCircle(header.avatar)
                } else {
                    authorImage.setImageDrawable(
                        MethodChecker.getDrawable(
                            itemView.context,
                            com.tokopedia.topads.sdk.R.drawable.error_drawable
                        )
                    )
                }
                authorImage.setOnClickListener {
                    onAvatarClick(
                        header.avatarApplink,
                        postId,
                        activityName,
                        header.followCta,
                        shopId
                    )
                }
            }

            if (template.avatarBadge && header.avatarBadgeImage.isNotBlank()) {
                authorBadge.show()
                authorBadge.loadImage(header.avatarBadgeImage)
                authorTitle.setMargin(
                    itemView.getDimens(contentcommonR.dimen.content_common_dp_4),
                    0,
                    itemView.getDimens(contentcommonR.dimen.content_common_space_8),
                    0
                )
            } else {
                authorBadge.hide()
                authorTitle.setMargin(
                    itemView.getDimens(contentcommonR.dimen.content_common_space_8),
                    0,
                    itemView.getDimens(contentcommonR.dimen.content_common_space_8),
                    0
                )
            }

            authorTitle.shouldShowWithAction(template.avatarTitle) {
                authorTitle.text = MethodChecker.fromHtml(header.avatarTitle)
                authorTitle.setOnClickListener {
                    onAvatarClick(
                        header.avatarApplink,
                        postId,
                        activityName,
                        header.followCta,
                        shopId
                    )
                }
            }

            authorSubtitle.shouldShowWithAction(template.avatarDate) {
                header.avatarDate = TimeConverter.generateTime(itemView.context, header.avatarDate)
                val spannableString: SpannableString =
                    if (header.cardSummary.isNotEmpty()) {
                        SpannableString(
                            String.format(
                                getString(contentcommonR.string.feed_header_time_format),
                                header.avatarDate,
                                header.cardSummary
                            )
                        )
                    } else {
                        SpannableString(header.avatarDate)
                    }
                authorSubtitle.text = spannableString
                authorSubtitle.setOnClickListener {
                    onAvatarClick(
                        header.avatarApplink,
                        postId,
                        activityName,
                        header.followCta,
                        shopId
                    )
                }
            }

            headerAction.shouldShowWithAction(
                template.followCta
                    && header.followCta.authorID != userSession.userId
            ) {
                bindFollow(header.followCta)
            }

            ivMenu.shouldShowWithAction(template.report) {
                if (canShowMenu(header.reportable, header.deletable, header.editable)) {
                    ivMenu.setOnClickListener {
                        listener.onMenuClick(
                            adapterPosition,
                            postId,
                            header.reportable,
                            header.deletable,
                            header.editable,
                            true,
                            header.followCta.authorID,
                            header.followCta.authorType,
                            "",
                            "",
                            caption
                        )
                    }
                } else {
                    ivMenu.hide()
                }
            }
        }
    }

    private fun canShowMenu(reportable: Boolean, deletable: Boolean, editable: Boolean): Boolean {
        return reportable || deletable || editable
    }

    private fun onAvatarClick(
        redirectUrl: String,
        activityId: String,
        activityName: String,
        followCta: FollowCta,
        shopId: String
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

    private fun bindFollow(followCta: FollowCta) {
        if (followCta.isFollow) {
            headerAction.text = followCta.textTrue
            headerAction.buttonCompatType = ButtonCompat.SECONDARY
        } else {
            headerAction.text = followCta.textFalse
            headerAction.buttonCompatType = ButtonCompat.PRIMARY
        }

        headerAction.setOnClickListener {
            listener.onHeaderActionClick(
                adapterPosition,
                followCta.authorID,
                followCta.authorType,
                followCta.isFollow,
                isVideo = false
            )
        }
    }

    private fun animateFooter() {
        Handler().postDelayed({
            footerBackground.animation =
                AnimationUtils.loadAnimation(itemView.context, contentcommonR.anim.anim_fade_in)
            footerBackground.visibility = View.VISIBLE
        }, ANIMATION_DURATION)
    }

    private fun shouldShowHeader(template: TemplateHeader): Boolean {
        return template.avatar || template.avatarBadge || template.avatarDate ||
            template.avatarTitle || template.followCta || template.report
    }

    @SuppressLint("PII Data Exposure")
    open fun bindCaption(
        caption: Caption,
        template: TemplateBody,
        trackingPostModel: TrackingPostModel
    ) {
        tvCaption.shouldShowWithAction(template.caption) {
            val tagCaption = FeedCaption.Tag(
                colorRes = MethodChecker.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                ),
                clickListener = {
                    onHashtagClicked(it, trackingPostModel)
                }
            )

            val captionBody = FeedCaption.Builder(caption.text)
                .withTag(tagCaption)
                .build()

            val readMoreCaption = FeedCaption.ReadMore(
                maxTrimChar = MAX_CHAR,
                label = caption.buttonName,
                colorRes = MethodChecker.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                ),
                clickListener = {
                    tvCaption.setText(captionBody, TextView.BufferType.SPANNABLE)
                }
            )
            val trimmedCaption = FeedCaption.Builder(caption.text)
                .withTag(tagCaption)
                .trimCaption(readMoreCaption)
                .build()

            tvCaption.setText(trimmedCaption, TextView.BufferType.SPANNABLE)
            tvCaption.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun onHashtagClicked(hashtag: String, trackingPostModel: TrackingPostModel) {
        val encodeHashtag = URLEncoder.encode(hashtag)
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalContent.HASHTAG_PAGE,
            encodeHashtag
        )
        listener.onHashtagClicked(hashtag, trackingPostModel)
    }

    private fun bindContentList(postId: String,
                                contentList: MutableList<BasePostModel>,
                                template: TemplateBody,
                                feedType: String) {
        contentLayout.shouldShowWithAction(template.media && contentList.size !=0) {
            contentList.forEach { it.postId = postId }
            contentList.forEach { it.positionInFeed = adapterPosition }

            adapter = PostPagerAdapter(
                imagePostListener,
                youtubePostListener,
                pollOptionListener,
                gridItemListener,
                videoViewListener,
                feedMultipleImageViewListener,
                feedType
            )
            adapter.setList(contentList)
            contentViewPager.adapter = adapter
            contentViewPager.offscreenPageLimit = adapter.count
            tabLayout.setupWithViewPager(contentViewPager)
            tabLayout.visibility = if (adapter.count > 1) View.VISIBLE else View.GONE
        }
    }

    private fun bindFooter(
        id: String,
        footer: Footer,
        template: TemplateFooter,
        isPostTagAvailable: Boolean,
        authorType: String
    ) {
        llFooter.shouldShowWithAction(shouldShowFooter(template)) {
            footerBackground.visibility = View.GONE
            if (template.ctaLink && !TextUtils.isEmpty(footer.buttonCta.text) && !isPostTagAvailable) {
                layoutFooterAction.show()
                footerAction.text = footer.buttonCta.text
                footerAction.setOnClickListener {
                    listener.onFooterActionClick(
                        adapterPosition,
                        footer.buttonCta.appLink
                    )
                }
            } else {
                layoutFooterAction.hide()
            }

            if (template.like) {
                likeGroup.show()
                likeIcon.setOnClickListener {
                    listener.onLikeClick(
                        adapterPosition,
                        id.toLongOrZero(),
                        footer.like.isChecked,
                        "",
                        false,
                        false, "", "",
                        authorType = authorType
                    )
                }
                likeText.setOnClickListener {
                    listener.onLikeClick(
                        adapterPosition,
                        id.toLongOrZero(),
                        footer.like.isChecked,
                        "",
                        isFollowed = false, type = false, "", "",
                        authorType = authorType
                    )
                }
                bindLike(footer.like)
            } else {
                likeGroup.hide()
            }

            if (template.comment) {
                commentGroup.show()
                commentIcon.setOnClickListener {
                    listener.onCommentClick(
                        adapterPosition,
                        id,
                        "",
                        "",
                        isFollowed = false,
                        ""
                    )
                }
                commentText.setOnClickListener {
                    listener.onCommentClick(
                        adapterPosition,
                        id,
                        "",
                        "",
                        isFollowed = false,
                        mediaType = ""
                    )
                }
                bindComment(footer.comment)
            } else {
                commentGroup.hide()
            }

            if (template.share) {
                shareGroup.show()
                shareText.text = footer.share.text
                shareIcon.setOnClickListener {
                    listener.onShareClick(
                        adapterPosition,
                        id,
                        footer.share.title,
                        footer.share.description,
                        footer.share.url,
                        footer.share.imageUrl,
                        mediaType = ""
                    )
                }
                shareText.setOnClickListener {
                    listener.onShareClick(
                        adapterPosition,
                        id,
                        footer.share.title,
                        footer.share.description,
                        footer.share.url,
                        footer.share.imageUrl,
                        mediaType = ""
                    )
                }
            } else {
                shareGroup.hide()
            }

            if (template.stats) {
                statsIcon.shouldShowWithAction(true) {
                    statsIcon.setOnClickListener {
                        listener.onStatsClick(
                            footer.stats.text,
                            id.toString(),
                            footer.stats.productIDs,
                            footer.like.value,
                            footer.comment.value
                        )
                    }
                }
            } else statsIcon.hide()
        }
    }

    private fun shouldShowFooter(template: TemplateFooter): Boolean {
        return template.comment || template.ctaLink || template.like || template.share || template.stats
    }

    private fun bindLike(like: Like) {
        when {
            like.isChecked -> {
                updateLikeButton(true)
                val likeCount = if (like.fmt.isEmpty()) like.value.toString() else like.fmt
                likeText.text = likeCount
                likeText.setTextColor(
                    MethodChecker.getColor(
                        likeText.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            }
            like.value > 0 -> {
                updateLikeButton(false)
                likeText.text = like.fmt
                likeText.setTextColor(
                    MethodChecker.getColor(
                        likeText.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_44
                    )
                )
            }
            else -> {
                updateLikeButton(false)
                val text: String =
                    if (like.fmt.isNotEmpty() && !like.fmt.equals("0")) {
                        like.fmt
                    } else {
                        getString(contentcommonR.string.kol_action_like)
                    }
                likeText.text = text
                likeText.setTextColor(
                    MethodChecker.getColor(
                        likeIcon.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_44
                    )
                )
            }
        }
    }

    private fun updateLikeButton(isLiked: Boolean) {
        val likedColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        val dislikeColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN300)
        if (isLiked) {
            likeIcon.setImage(
                newIconId = IconUnify.THUMB_FILLED,
                newLightEnable = likedColor,
                newLightDisable = dislikeColor,
                newDarkEnable = likedColor,
                newDarkDisable = dislikeColor
            )
        } else {
            likeIcon.setImage(
                newIconId = IconUnify.THUMB_FILLED,
                newLightEnable = dislikeColor,
                newLightDisable = likedColor,
                newDarkEnable = dislikeColor,
                newDarkDisable = likedColor
            )
        }
    }

    private fun bindComment(comment: Comment) {
        commentText.text =
            if (comment.value == 0) {
                if (comment.fmt.isNotEmpty()) {
                    comment.fmt
                } else {
                    getString(contentcommonR.string.kol_action_comment)
                }
            } else {
                if (comment.fmt.isNotEmpty()) comment.fmt else comment.value.toString()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindPostTag(
        postId: String,
        postTag: PostTag,
        template: TemplateBody,
        feedType: String,
        authorType: String
    ) {
        viewItemPostTag.rootView.shouldShowWithAction(shouldShowPostTag(postTag, template)) {
            if (postTag.text.isNotEmpty()) {
                cardTitlePostTag.text = postTag.text
                cardTitlePostTag.show()
            } else {
                cardTitlePostTag.hide()
            }
            if (postTag.totalItems > 0) {
                rvPostTag.show()
                rvPostTag.setHasFixedSize(true)
                val layoutManager: RecyclerView.LayoutManager = when (feedType) {
                    SOURCE_DETAIL -> LinearLayoutManager(itemView.context)
                    else -> LinearLayoutManager(
                        itemView.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                }
                rvPostTag.isNestedScrollingEnabled = false
                rvPostTag.layoutManager = layoutManager
                rvPostTag.adapter = PostTagAdapter(
                    mapPostTag(postTag.items, feedType, postId, adapterPosition, authorType),
                    PostTagTypeFactoryImpl(
                        listener,
                        DeviceScreenInfo.getScreenWidth(itemView.context)
                    )
                )
                (rvPostTag.adapter as PostTagAdapter).notifyDataSetChanged()
            } else {
                rvPostTag.hide()
            }
        }
    }

    private fun shouldShowPostTag(postTag: PostTag, template: TemplateBody): Boolean {
        return template.postTag || isPostTagAvailable(postTag)
    }

    private fun isPostTagAvailable(postTag: PostTag): Boolean {
        return postTag.totalItems != 0 || postTag.items.isNotEmpty()
    }

    private fun mapPostTag(
        postTagItemList: List<PostTagItem>,
        feedType: String,
        postId: String,
        positionInFeed: Int,
        authorType: String
    ): MutableList<BasePostTagModel> {
        val needToRezise = postTagItemList.size > 1
        val itemList: MutableList<BasePostTagModel> = ArrayList()
        for (postTagItem in postTagItemList) {
            when (postTagItem.type) {
                POSTTAG_PRODUCT -> {
                    val shouldHideActionButton = postTagItem.shop.firstOrNull()?.shopId == userSession.shopId
                    val item = ProductPostTagModel(
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
                        authorType,
                        shouldHideActionButton = shouldHideActionButton
                    )
                    item.feedType = feedType
                    item.postId = postId
                    item.positionInFeed = positionInFeed
                    itemList.add(item)
                }
                POSTTAG_BUTTONCTA -> {
                    val item = CtaPostTagModel(
                        postTagItem.id,
                        postTagItem.text,
                        postTagItem.type,
                        postTagItem.applink,
                        postTagItem.weblink,
                        postTagItem.position,
                        postTagItem
                    )
                    item.feedType = feedType
                    item.postId = postId
                    item.positionInFeed = positionInFeed
                    itemList.add(item)
                }
            }
        }
        return itemList
    }

    private fun bindTracking(impressHolder: ImpressHolder, trackList: MutableList<TrackingModel>) {
        itemView.addOnImpressionListener(impressHolder) {
            listener.onAffiliateTrackClicked(trackList, false)
        }
    }

    interface DynamicPostListener {
        fun onAvatarClick(
            positionInFeed: Int,
            redirectUrl: String,
            activityId: String,
            activityName: String,
            followCta: FollowCta,
            type: String = "",
            isFollowed: Boolean = false,
            shopId: String,
            mediaType: String,
            isCaption: Boolean
        )

        fun onHeaderActionClick(
            positionInFeed: Int,
            id: String,
            type: String,
            isFollow: Boolean,
            postType: String = "",
            isVideo: Boolean,
            isBottomSheetMenuOnFeed: Boolean = false,
            isFollowedFromFollowRestrictionBottomSheet: Boolean = false
        )

        fun onMenuClick(
            positionInFeed: Int,
            postId: String,
            reportable: Boolean,
            deletable: Boolean,
            editable: Boolean,
            isFollowed: Boolean,
            authorId: String,
            authorType: String,
            postType: String = "",
            mediaType: String = "",
            caption: String,
            playChannelId: String = ""
        )

        fun onCaptionClick(positionInFeed: Int, redirectUrl: String)

        fun onLikeClick(
            positionInFeed: Int,
            id: Long,
            isLiked: Boolean,
            postType: String = "",
            isFollowed: Boolean = false,
            type: Boolean = false,
            shopId: String = "",
            mediaType: String? = "",
            playChannelId: String = "",
            authorType: String = ""
        )

        fun onCommentClick(
            positionInFeed: Int,
            id: String,
            authorType: String,
            type: String,
            isFollowed: Boolean = false,
            mediaType: String = "",
            shopId: String = "",
            playChannelId: String = "",
            isClickIcon: Boolean = true
        )

        fun onStatsClick(
            title: String,
            activityId: String,
            productIds: List<String>,
            likeCount: Int,
            commentCount: Int
        )

        fun onShareClick(
            positionInFeed: Int,
            id: String,
            title: String,
            description: String,
            url: String,
            imageUrl: String,
            postTypeASGC: Boolean = false,
            type: String = "",
            isFollowed: Boolean = false,
            shopId: String = "",
            mediaType: String = "",
            isTopads: Boolean = false,
            playChannelId: String = "",
            weblink: String = ""
        )

        fun onFooterActionClick(positionInFeed: Int, redirectUrl: String)

        fun onPostTagItemClick(
            positionInFeed: Int,
            redirectUrl: String,
            postTagItem: PostTagItem,
            itemPosition: Int
        )

        fun onFullScreenCLick(
            feedXCard: FeedXCard,
            positionInFeed: Int,
            redirectUrl: String,
            currentTime: Long,
            shouldTrack: Boolean,
            isFullScreenButton: Boolean
        )

        fun addVODView(
            feedXCard: FeedXCard,
            playChannelId: String,
            rowNumber: Int,
            time: Long,
            hitTrackerApi: Boolean
        )

        fun sendWatchVODTracker(
            feedXCard: FeedXCard,
            playChannelId: String,
            rowNumber: Int,
            time: Long
        )

        fun onPostTagBubbleClick(
            positionInFeed: Int,
            redirectUrl: String,
            postTagItem: FeedXProduct,
            adClickUrl: String
        )

        fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean)

        fun onPostTagItemBuyClicked(
            positionInFeed: Int,
            postTagItem: PostTagItem,
            authorType: String
        )

        fun onHashtagClicked(hashtagText: String, trackingPostModel: TrackingPostModel)

        fun onReadMoreClicked(trackingPostModel: TrackingPostModel)

        fun onImageClicked(card: FeedXCard)

        fun onTagClicked(
            card: FeedXCard,
            products: List<FeedXProduct>,
            listener: DynamicPostListener,
            mediaType: String = "",
            positionInFeed: Int
        )

        fun onIngatkanSayaBtnImpressed(card: FeedXCard, positionInFeed: Int)
        fun onIngatkanSayaBtnClicked(card: FeedXCard, positionInFeed: Int)
        fun changeUpcomingWidgetToOngoing(card: FeedXCard, positionInFeed: Int)
        fun removeOngoingCampaignSaleWidget(card: FeedXCard, positionInFeed: Int)

        fun onReadMoreClicked(
            card: FeedXCard,
            positionInFeed: Int
        )

        fun muteUnmuteVideo(
            card: FeedXCard,
            mute: Boolean,
            positionInFeed: Int,
            mediaType: String
        )

        fun onImpressionTracking(feedXCard: FeedXCard, positionInFeed: Int)

        fun onHashtagClickedFeed(hashtagText: String, feedXCard: FeedXCard)

        fun onFollowClickAds(positionInFeed: Int, shopId: String, adId: String)

        fun onClickSekSekarang(
            postId: String,
            shopId: String,
            type: String,
            isFollowed: Boolean,
            hasVoucher: Boolean,
            positionInFeed: Int,
            feedXCard: FeedXCard
        )
    }
}
