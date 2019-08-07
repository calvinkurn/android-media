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
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
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

    class Holder(v: View, highlightListener: HighlightListener): RecyclerView.ViewHolder(v) {

        fun bind(item: HighlightCardViewModel, positionInAdapter: Int) {
            initView(item)
            ImageHandler.loadImageFit2(itemView.context, itemView.productImage, item.thumbnail)
        }

        private fun initView(item: HighlightCardViewModel) {
            itemView.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val viewTreeObserver = itemView.viewTreeObserver
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        @Suppress("DEPRECATION")
                        viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                    val displayMetrics = DisplayMetrics()
                    (itemView.context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.let {
                        it.defaultDisplay.getMetrics(displayMetrics)
                        itemView.layoutParams.width = (displayMetrics.widthPixels * VALUE_CARD_SIZE).toInt()
                        itemView.requestLayout()
                    }
                }
            })
            ImageHandler.loadImageFit2(itemView.context, itemView.productImage, item.thumbnail)
            if (getBadgeId(item) != 0) {
                ImageHandler.loadImageWithId(itemView.productImage, getBadgeId(item))
            }
            ImageHandler.loadImageFit2(itemView.context, itemView.userImage, item.header.avatar)
            itemView.userName.text = item.header.avatarTitle
            bindLike(item.footer.like)
            bindComment(item.footer.comment)

        }

        private fun getBadgeId(item: HighlightCardViewModel): Int {
            when (item.type) {
                TYPE_YOUTUBE, TYPE_VIDEO -> return R.drawable.ic_affiliate_video
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
                    itemView.likeText.text = "0"
                    itemView.likeText.setTextColor(
                            MethodChecker.getColor(itemView.likeIcon.context, R.color.black_54)
                    )
                }
            }
        }

        private fun bindComment(comment: Comment) {
            itemView.commentText.text =
                    if (comment.value == 0) "0"
                    else comment.fmt
        }
    }

    interface HighlightListener {
        fun onAvatarClick(positionInFeed: Int, redirectUrl: String)

        fun onLikeClick(positionInFeed: Int, id: Int, isLiked: Boolean)

        fun onCommentClick(positionInFeed: Int, id: Int)

        fun onFooterActionClick(positionInFeed: Int, redirectUrl: String)

        fun onAffiliateTrackClicked(trackList: MutableList<TrackingViewModel>, isClick: Boolean)
    }

}