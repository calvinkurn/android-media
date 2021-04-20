package com.tokopedia.feedcomponent.view.adapter.viewholder.highlight

import androidx.recyclerview.widget.RecyclerView
import android.view.*
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Comment
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Like
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import kotlinx.android.synthetic.main.item_highlight_card.view.*

/**
 * @author by yoasfs on 2019-08-06
 */

class HighlightAdapter(val list: MutableList<HighlightCardViewModel>,
                       val highlightListener: HighlightListener): RecyclerView.Adapter<HighlightAdapter.Holder>() {

    companion object {
        private const val VALUE_CARD_SIZE = 0.4
        val TYPE_YOUTUBE = "youtube"
        val TYPE_VIDEO = "video"
        val TYPE_MULTI = "multimedia"
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_highlight_card, p0, false), highlightListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, p1: Int) {
        holder.bind(list[p1], p1)
    }

    class Holder(v: View, private val highlightListener: HighlightListener): RecyclerView.ViewHolder(v) {

        fun bind(item: HighlightCardViewModel, positionInAdapter: Int) {
            initView(item)
            initViewListener(item, positionInAdapter)

        }

        private fun initViewListener(item: HighlightCardViewModel, positionInAdapter: Int) {
            itemView.likeIcon.setOnClickListener{
                highlightListener.onLikeClick(item.positionInFeed, positionInAdapter, item.postId, item.footer.like.isChecked)
            }
            itemView.likeText.setOnClickListener{
                highlightListener.onLikeClick(positionInAdapter, positionInAdapter, item.postId, item.footer.like.isChecked)
            }
            itemView.commentIcon.setOnClickListener{
                highlightListener.onCommentClick(item.positionInFeed, positionInAdapter, item.postId)
            }
            itemView.commentText.setOnClickListener{
                highlightListener.onCommentClick(item.positionInFeed, positionInAdapter, item.postId)
            }
            itemView.userImage.setOnClickListener {
                highlightListener.onAvatarClick(item.positionInFeed, item.header.avatarApplink, 0 , "", FollowCta())
            }
            itemView.userName.setOnClickListener {
                highlightListener.onAvatarClick(item.positionInFeed, item.header.avatarApplink ,0, "", FollowCta())
            }
            itemView.productImage.setOnClickListener {
                highlightListener.onHighlightItemClicked(item.positionInFeed, item)
                highlightListener.onAffiliateTrackClicked(item.tracking, true)
            }
            itemView.addOnImpressionListener(item.impressHolder) {
                highlightListener.onAffiliateTrackClicked(item.tracking, false)
            }
        }

        private fun initView(item: HighlightCardViewModel) {
            ImageHandler.loadImageFit2(itemView.context, itemView.productImage, item.thumbnail)
            if (getBadgeId(item) != 0) {
                ImageHandler.loadImageWithId(itemView.badge, getBadgeId(item))
            }
            ImageHandler.loadImageCircle2(itemView.context, itemView.userImage, item.header.avatar)
            itemView.userName.text = item.header.avatarTitle
            bindLike(item.footer.like)
            bindComment(item.footer.comment)
            if (item.type.equals(TYPE_VIDEO)) {
                itemView.layout_video_duration.visibility = View.VISIBLE
                itemView.text_video_duration.text = item.videoDuration
            }
        }

        private fun getBadgeId(item: HighlightCardViewModel): Int {
            when (item.type) {
                TYPE_YOUTUBE -> return R.drawable.ic_affiliate_video
                TYPE_MULTI -> return R.drawable.ic_affiliate_multi
                else -> return 0
            }
        }

        private fun bindLike(like: Like) {
            when {
                like.isChecked -> {
                    itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_green)
                    itemView.likeText.text = like.fmt
                    itemView.likeText.setTextColor(
                            MethodChecker.getColor(itemView.likeText.context, com.tokopedia.unifyprinciples.R.color.Unify_G400)
                    )
                }
                like.value > 0 -> {
                    itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb)
                    itemView.likeText.text = like.fmt
                    itemView.likeText.setTextColor(
                            MethodChecker.getColor(itemView.likeText.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
                    )
                }
                else -> {
                    itemView.likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb)
                    val likeText = if (like.fmt.isNotEmpty()) like.fmt else ""
                    itemView.likeText.text = likeText
                    itemView.likeText.setTextColor(
                            MethodChecker.getColor(itemView.likeIcon.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
                    )
                }
            }
        }

        private fun bindComment(comment: Comment) {
            val commentText = if (comment.fmt.isNotEmpty()) comment.fmt else ""
            itemView.commentText.text = commentText
        }
    }

    interface HighlightListener {
        fun onAvatarClick(positionInFeed: Int, redirectUrl: String, activityId: Int, activityName: String, followCta: FollowCta)

        fun onLikeClick(positionInFeed: Int, columnNumber: Int, id: Int, isLiked: Boolean)

        fun onCommentClick(positionInFeed: Int, columnNumber: Int, id: Int)

        fun onFooterActionClick(positionInFeed: Int, redirectUrl: String)

        fun onAffiliateTrackClicked(trackList: List<TrackingViewModel>, isClick: Boolean)

        fun onHighlightItemClicked(positionInFeed: Int, item: HighlightCardViewModel)
    }

}