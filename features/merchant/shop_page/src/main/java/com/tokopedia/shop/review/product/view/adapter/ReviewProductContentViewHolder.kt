package com.tokopedia.shop.review.product.view.adapter

import android.content.Context
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.review.shop.view.uimodel.ImageAttachmentUiModel
import com.tokopedia.shop.review.shop.view.uimodel.ImageUpload
import com.tokopedia.shop.review.util.TimeUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import java.util.*

/**
 * Created by zulfikarrahman on 1/16/18.
 */
open class ReviewProductContentViewHolder(itemView: View, private val viewListener: ListenerReviewHolder) : AbstractViewHolder<ReviewProductModelContent>(itemView) {
    private val reviewerName: TextView
    private val reviewTime: TextView
    private val reviewAttachment: RecyclerView
    private val reviewOverflow: ImageView
    private val review: TextView
    private val reviewStar: RatingBar
    private val adapter: ImageUploadAdapter
    private val replyReviewLayout: View
    private val seeReplyText: TextView
    private val replyArrow: ImageView
    private val sellerName: TextView
    private val sellerReplyTime: TextView
    private val sellerReply: TextView
    private val replyOverflow: ImageView
    private val iconLike: ImageView
    private val counterLike: TextView
    private val containerReplyView: View
    private val containerLike: View
    private val context: Context
    override fun bind(element: ReviewProductModelContent) {
        adapter.setListener(onImageClicked(element))
        reviewerName.text = getReviewerNameText(element)
        reviewerName.setOnClickListener {
            if (!element.isReviewIsAnonymous || element.isSellerRepliedOwner) {
                viewListener.onGoToProfile(element.reviewerId, adapterPosition)
            }
        }
        containerReplyView.setOnClickListener {
            viewListener.onSeeReplied(adapterPosition)
            toggleReply(element)
        }
        reviewTime.text = TimeUtil.generateTimeYearly(element.reviewTime?.replace(WIB, "").orEmpty())
        reviewStar.rating = element.reviewStar
        review.text = getReview(element.reviewMessage)
        review.setOnClickListener {
            if (review.text.toString().endsWith(context.getString(R.string.more_to_complete))) {
                review.text = MethodChecker.fromHtml(element.reviewMessage)
            }
        }
        if (element.isReviewCanReported) {
            reviewOverflow.visibility = View.VISIBLE
            reviewOverflow.setOnClickListener(onReviewOverflowClicked(element))
        } else {
            reviewOverflow.visibility = View.GONE
        }
        initReplyViewState(element)
        if (element.isReviewHasReplied) {
            setSellerReply(element)
        } else {
            replyReviewLayout.visibility = View.GONE
            seeReplyText.visibility = View.GONE
            replyArrow.visibility = View.GONE
        }
        containerLike.setOnClickListener {
            viewListener.onLikeDislikePressed(element.reviewId, if (element.isLikeStatus) UNLIKE_STATUS else LIKE_STATUS_ACTIVE, element.productId,
                    element.isLikeStatus, adapterPosition)
            element.isLikeStatus = !element.isLikeStatus
            element.totalLike = (if (element.isLikeStatus) element.totalLike + 1 else element.totalLike - 1)
            setLikeStatus(element)
        }
        setLikeStatus(element)
        if (element.reviewAttachment != null && element.reviewAttachment!!.isNotEmpty()) {
            reviewAttachment.visibility = View.VISIBLE
        } else {
            reviewAttachment.visibility = View.GONE
        }
        adapter.addList(convertToAdapterViewModel(element.reviewAttachment))
        adapter.notifyDataSetChanged()

        val cvReviewContent = itemView.findViewById<CardView>(R.id.cv_review_content)
        if (context.isDarkMode()) {
            cvReviewContent.setBackgroundColor(
                    ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N50
                    )
            )
        } else {
            cvReviewContent.setBackgroundColor(
                    ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
            )
        }
    }

    fun setLikeStatus(element: ReviewProductModelContent) {
        if (element.isLikeStatus) {
            iconLike.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_like_pressed))
        } else {
            iconLike.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_like_normal))
        }
        if (element.isLogin) {
            if (element.isLikeStatus && element.totalLike > 1) {
                counterLike.text = itemView.context.getString(R.string.product_review_label_counter_like_1_formatted, element.totalLike - 1)
            } else if (element.isLikeStatus && element.totalLike == 1) {
                counterLike.setText(R.string.product_review_label_counter_like_2_formatted)
            } else if (!element.isLikeStatus && element.totalLike < 1) {
                counterLike.setText(R.string.product_review_label_counter_like_3_formatted)
            } else {
                counterLike.text = itemView.context.getString(R.string.product_review_label_counter_like_4_formatted, element.totalLike)
            }
        } else {
            counterLike.text = itemView.context.getString(R.string.product_review_label_counter_like_4_formatted, element.totalLike)
        }
    }

    private fun convertToAdapterViewModel(reviewAttachment: List<ImageAttachmentUiModel?>?): ArrayList<ImageUpload> {
        val list = ArrayList<ImageUpload>()
        for (vm in reviewAttachment!!) {
            list.add(ImageUpload(
                    vm?.uriThumbnail,
                    vm?.uriLarge,
                    vm?.description, vm?.attachmentId.toString()))
        }
        return list
    }

    private fun onImageClicked(element: ReviewProductModelContent): ImageUploadAdapter.ProductImageListener {
        return object : ImageUploadAdapter.ProductImageListener {
            override fun onUploadClicked(position: Int): View.OnClickListener {
                return View.OnClickListener { }
            }

            override fun onImageClicked(position: Int, imageUpload: ImageUpload?): View.OnClickListener {
                return View.OnClickListener { viewListener.goToPreviewImage(position, adapter.list, element) }
            }
        }
    }

    private fun setSellerReply(element: ReviewProductModelContent) {
        seeReplyText.visibility = View.VISIBLE
        replyArrow.visibility = View.VISIBLE
        sellerName.text = element.sellerName
        sellerName.setOnClickListener { viewListener.onGoToShopInfo(element.shopId) }
        sellerReplyTime.text = TimeUtil.generateTimeYearly(element.responseCreateTime?.replace(WIB, "").orEmpty())
        sellerReply.text = MethodChecker.fromHtml(element.responseMessage)
        if (element.isSellerRepliedOwner) {
            replyOverflow.visibility = View.VISIBLE
            replyOverflow.setOnClickListener { v ->
                val popup = PopupMenu(itemView.context, v)
                popup.menu.add(1, MENU_DELETE, 1,
                        context
                                .getString(R.string.menu_delete))
                popup.setOnMenuItemClickListener { item ->
                    if (item.itemId == MENU_DELETE) {
                        viewListener.onDeleteReviewResponse(element, adapterPosition)
                        true
                    } else {
                        false
                    }
                }
                popup.show()
            }
        } else {
            replyOverflow.visibility = View.GONE
        }
    }

    private fun toggleReply(element: ReviewProductModelContent) {
        element.isReplyOpened = !element.isReplyOpened
        initReplyViewState(element)
    }

    private fun initReplyViewState(element: ReviewProductModelContent) {
        if (element.isReplyOpened) {
            seeReplyText.text = context.getText(R.string.close_reply)
            replyArrow.rotation = 180f
            replyReviewLayout.visibility = View.VISIBLE
            viewListener.onSmoothScrollToReplyView(adapterPosition)
        } else {
            seeReplyText.text = context.getText(R.string.see_reply)
            replyArrow.rotation = 0f
            replyReviewLayout.visibility = View.GONE
        }
    }

    private fun getReview(review: String?): Spanned {
        return if (MethodChecker.fromHtml(review).length > MAX_CHAR) {
            val moreDescription = "<font color='#${Integer.toHexString(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400) + 0x00ffffff)}'>Selengkapnya</font>"
            val subDescription = MethodChecker.fromHtml(review).toString().substring(0, MAX_CHAR)
            MethodChecker
                    .fromHtml(subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                            + moreDescription)
        } else {
            MethodChecker.fromHtml(review)
        }
    }

    private fun getReviewerNameText(element: ReviewProductModelContent): String? {
        return if (element.isReviewIsAnonymous && !element.isSellerRepliedOwner) {
            getAnonymousName(element.reviewerName)
        } else {
            element.reviewerName
        }
    }

    private fun getAnonymousName(name: String?): String {
        val first = name!!.substring(0, 1)
        val last = name.substring(name.length - 1)
        return "$first***$last"
    }

    private fun onReviewOverflowClicked(element: ReviewProductModelContent): View.OnClickListener {
        return View.OnClickListener { v ->
            viewListener.onMenuClicked(adapterPosition)
            val popup = PopupMenu(itemView.context, v)
            popup.menu.add(1, MENU_REPORT, 2, v.context.getString(R.string.menu_report))
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                if (item.itemId == MENU_REPORT) {
                    viewListener.onGoToReportReview(
                            element.shopId,
                            element.reviewId,
                            adapterPosition
                    )
                    return@OnMenuItemClickListener true
                }
                false
            })
            popup.show()
        }
    }

    interface ListenerReviewHolder {
        fun onGoToProfile(reviewerId: String?, adapterPosition: Int)
        fun goToPreviewImage(position: Int, list: ArrayList<ImageUpload>, element: ReviewProductModelContent?)
        fun onGoToShopInfo(shopId: String?)
        fun onDeleteReviewResponse(element: ReviewProductModelContent, adapterPosition: Int)
        fun onSmoothScrollToReplyView(adapterPosition: Int)
        fun onGoToReportReview(shopId: String?, reviewId: String?, adapterPosition: Int)
        fun onLikeDislikePressed(reviewId: String?, likeStatus: Int, productId: String?, status: Boolean, adapterPosition: Int)
        fun onMenuClicked(adapterPosition: Int)
        fun onSeeReplied(adapterPosition: Int)
    }

    companion object {
        val LAYOUT = R.layout.item_product_review_shop_page
        private const val MAX_CHAR = 50
        const val UNLIKE_STATUS = 3
        const val LIKE_STATUS_ACTIVE = 1
        const val WIB = "WIB"
        private const val MENU_REPORT = 102
        private const val MENU_DELETE = 103
    }

    init {
        context = itemView.context
        reviewerName = itemView.findViewById<View>(R.id.reviewer_name) as TextView
        reviewTime = itemView.findViewById<View>(R.id.review_time) as TextView
        reviewAttachment = itemView.findViewById<View>(R.id.product_review_image) as RecyclerView
        reviewOverflow = itemView.findViewById<View>(R.id.review_overflow) as ImageView
        review = itemView.findViewById<View>(R.id.review) as TextView
        reviewStar = itemView.findViewById<View>(R.id.product_rating) as RatingBar
        adapter = ImageUploadAdapter.Companion.createAdapter(itemView.context)
        adapter.setReviewImage(true)
        adapter.setCanUpload(false)
        reviewAttachment.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.HORIZONTAL, false)
        reviewAttachment.adapter = adapter
        seeReplyText = itemView.findViewById<View>(R.id.see_reply_button) as TextView
        replyArrow = itemView.findViewById<View>(R.id.reply_chevron) as ImageView
        replyReviewLayout = itemView.findViewById(R.id.reply_review_layout)
        sellerName = itemView.findViewById<View>(R.id.seller_reply_name) as TextView
        sellerReplyTime = itemView.findViewById<View>(R.id.seller_reply_time) as TextView
        sellerReply = itemView.findViewById<View>(R.id.seller_reply) as TextView
        replyOverflow = itemView.findViewById<View>(R.id.reply_overflow) as ImageView
        iconLike = itemView.findViewById(R.id.icon_like)
        counterLike = itemView.findViewById(R.id.text_counter_like)
        containerReplyView = itemView.findViewById(R.id.container_reply_view)
        containerLike = itemView.findViewById(R.id.container_like)
    }
}