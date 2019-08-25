package com.tokopedia.feedcomponent.view.adapter.viewholder.highlight

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.*
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Comment
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Like
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
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
        val TYPE_MULTI = "multi"
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

    class Holder(v: View, val highlightListener: HighlightListener): RecyclerView.ViewHolder(v) {

        fun bind(item: HighlightCardViewModel, positionInAdapter: Int) {
            initView(item)
            initViewListener(item, positionInAdapter)

        }

        private fun initViewListener(item: HighlightCardViewModel, positionInAdapter: Int) {
            itemView.likeIcon.setOnClickListener{highlightListener.onLikeClick(item.positionInFeed, positionInAdapter, item.postId, item.footer.like.isChecked)}
            itemView.likeText.setOnClickListener{highlightListener.onLikeClick(positionInAdapter, positionInAdapter, item.postId, item.footer.like.isChecked)}
            itemView.commentIcon.setOnClickListener{highlightListener.onCommentClick(item.positionInFeed, positionInAdapter, item.postId)}
            itemView.commentText.setOnClickListener{highlightListener.onCommentClick(item.positionInFeed, positionInAdapter, item.postId)}
            itemView.userImage.setOnClickListener {highlightListener.onAvatarClick(item.positionInFeed, item.header.avatarApplink) }
            itemView.userName.setOnClickListener {highlightListener.onAvatarClick(item.positionInFeed, item.header.avatarApplink) }
            itemView.productImage.setOnClickListener { highlightListener.onHighlightItemClicked(item.positionInFeed, item.applink) }
        }

        private fun initView(item: HighlightCardViewModel) {
            ImageHandler.loadImageFit2(itemView.context, itemView.productImage, item.thumbnail)
            if (getBadgeId(item) != 0) {
                ImageHandler.loadImageWithId(itemView.productImage, getBadgeId(item))
            }
            ImageHandler.loadImageFit2(itemView.context, itemView.userImage, item.header.avatar)
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
            if (comment.value == 0) {
                itemView.commentText.setText(R.string.kol_action_comment)
            } else {
                itemView.commentText.text = comment.fmt
            }
        }
    }

    interface HighlightListener {
        fun onAvatarClick(positionInFeed: Int, redirectUrl: String)

        fun onLikeClick(positionInFeed: Int, columnNumber: Int, id: Int, isLiked: Boolean)

        fun onCommentClick(positionInFeed: Int, columnNumber: Int, id: Int)

        fun onFooterActionClick(positionInFeed: Int, redirectUrl: String)

        fun onAffiliateTrackClicked(trackList: MutableList<TrackingViewModel>, isClick: Boolean)

        fun onHighlightItemClicked(positionInFeed: Int, redirectUrl: String)
    }

}