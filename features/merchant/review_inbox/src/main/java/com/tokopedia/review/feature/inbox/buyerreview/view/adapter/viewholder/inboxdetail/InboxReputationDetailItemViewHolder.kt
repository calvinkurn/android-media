package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.util.TimeConverter
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ImageUploadAdapter
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ImageUploadAdapter.ProductImageListener
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageAttachmentUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageUpload
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailItemViewHolder(
    itemView: View,
    private val viewListener: InboxReputationDetail.View
) : AbstractViewHolder<InboxReputationDetailItemUiModel>(itemView) {


    var isReplyOpened = false
    var productName: Typography
    var productAvatar: ImageView
    var emptyReviewText: Typography
    var viewReview: View
    var reviewerName: Typography
    var reviewTime: Typography
    var reviewAttachment: RecyclerView
    var reviewOverflow: ImageView
    var review: Typography
    var reviewStar: RatingBar
    var giveReview: View
    var context: Context
    var adapter: ImageUploadAdapter
    var replyReviewLayout: View
    var seeReplyLayout: View
    var seeReplyText: Typography
    var replyArrow: ImageView
    var sellerReplyLayout: View
    var sellerName: Typography
    var sellerReplyTime: Typography
    var sellerReply: Typography
    var replyOverflow: ImageView
    var sellerAddReplyLayout: View
    var sellerAddReplyEditText: EditText
    var sendReplyButton: ImageView
    private fun onImageClicked(): ProductImageListener {
        return object : ProductImageListener {
            override fun onUploadClicked(position: Int): View.OnClickListener {
                return View.OnClickListener { }
            }

            override fun onImageClicked(
                position: Int,
                imageUpload: ImageUpload?
            ): View.OnClickListener {
                return View.OnClickListener {
                    viewListener.goToPreviewImage(
                        position,
                        adapter.list
                    )
                }
            }
        }
    }

    override fun bind(element: InboxReputationDetailItemUiModel) {
        if (element.isProductDeleted) {
            productName.text = context.getString(R.string.product_is_deleted)
            ImageHandler.loadImageRounded2(
                productAvatar.context,
                productAvatar,
                R.drawable.ic_product_deleted,
                5.0f
            )
        } else if (element.isProductBanned) {
            productName.text = context.getString(R.string.product_is_banned)
            ImageHandler.loadImageRounded2(
                productAvatar.context,
                productAvatar,
                R.drawable.ic_product_deleted,
                5.0f
            )
        } else {
            productName.text = MethodChecker.fromHtml(element.productName)
            productName.setOnClickListener {
                viewListener.onGoToProductDetail(
                    element.productId, element
                        .productAvatar, element.productName
                )
            }
            ImageHandler.loadImageRounded2(
                productAvatar.context,
                productAvatar,
                element.productAvatar,
                15.0f
            )
            productAvatar.setOnClickListener {
                viewListener.onGoToProductDetail(
                    element.productId,
                    element.productAvatar, element.productName
                )
            }
        }
        if (!element.isReviewHasReviewed) {
            viewReview.visibility = View.GONE
            seeReplyLayout.visibility = View.GONE
            emptyReviewText.visibility = View.VISIBLE
            emptyReviewText.setText(R.string.not_reviewed)
        } else if (element.isReviewHasReviewed && element.isReviewSkipped) {
            emptyReviewText.visibility = View.VISIBLE
            viewReview.visibility = View.GONE
            seeReplyLayout.visibility = View.GONE
            emptyReviewText.visibility = View.VISIBLE
            emptyReviewText.setText(R.string.review_is_skipped)
        } else {
            emptyReviewText.visibility = View.GONE
            viewReview.visibility = View.VISIBLE
            giveReview.visibility = View.GONE
            reviewerName.text = MethodChecker.fromHtml(getReviewerNameText(element))
            reviewerName.setOnClickListener { viewListener.onGoToProfile(element.reviewerId) }
            val time: String
            time = if (element.isReviewIsEdited) {
                getFormattedTime(element.reviewTime) +
                        context.getString(R.string.edited)
            } else {
                getFormattedTime(element.reviewTime)
            }
            reviewTime.text = time
            reviewStar.rating = element.reviewStar.toFloat()
            review.text = getReview(element.review)
            review.setOnClickListener {
                if (review.text.toString().endsWith(context.getString(R.string.more_to_complete))) {
                    review.text = element.review
                }
            }
            setChevronDownImage()
            if (canShowOverflow(element)) {
                reviewOverflow.visibility = View.VISIBLE
                reviewOverflow.setOnClickListener(onReviewOverflowClicked(element))
            } else {
                reviewOverflow.visibility = View.GONE
            }
            if (element.reviewResponseUiModel != null
                && !TextUtils.isEmpty(element.reviewResponseUiModel.responseMessage)
            ) {
                setSellerReply(element)
            } else {
                seeReplyText.visibility = View.GONE
                replyArrow.visibility = View.GONE
                sellerReplyLayout.visibility = View.GONE
                if (element.tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
                    sellerAddReplyLayout.visibility = View.VISIBLE
                } else {
                    sellerAddReplyLayout.visibility = View.GONE
                }
            }
        }
        showOrHideGiveReviewLayout(element)
        adapter.addList(convertToAdapterViewModel(element.reviewAttachment))
        adapter.notifyDataSetChanged()
        sendReplyButton.setOnClickListener {
            viewListener.onSendReplyReview(
                element,
                sellerAddReplyEditText.text.toString()
            )
        }
    }

    private fun showOrHideGiveReviewLayout(element: InboxReputationDetailItemUiModel) {
        if (element.tab == ReviewInboxConstants.TAB_BUYER_REVIEW || element.isReviewSkipped
            || isOwnProduct(element)
            || element.isReviewHasReviewed
        ) {
            giveReview.visibility = View.GONE
        } else {
            giveReview.visibility = View.VISIBLE
        }
    }

    private fun setSellerReply(element: InboxReputationDetailItemUiModel) {
        sellerAddReplyLayout.visibility = View.GONE
        sellerReplyLayout.visibility = View.VISIBLE
        seeReplyLayout.visibility = View.VISIBLE
        seeReplyText.visibility = View.VISIBLE
        replyArrow.visibility = View.VISIBLE
        seeReplyText.setOnClickListener {
            toggleReply()
            viewListener.onClickToggleReply(element, adapterPosition)
        }
        replyArrow.setOnClickListener { toggleReply() }
        val reviewResponseUiModel = element.reviewResponseUiModel
        sellerName.text = MethodChecker.fromHtml(
            getFormattedReplyName(
                reviewResponseUiModel
                    .responseBy
            )
        )
        sellerName.setOnClickListener { viewListener.onGoToShopInfo(element.shopId) }
        sellerReplyTime.text = getFormattedTime(reviewResponseUiModel.responseCreateTime)
        sellerReply.text = MethodChecker.fromHtml(reviewResponseUiModel.responseMessage)
        sellerAddReplyEditText.setText("")
        if (element.tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
            seeReplyLayout.visibility = View.VISIBLE
            replyOverflow.visibility = View.VISIBLE
            replyOverflow.setOnClickListener { v ->
                val popup = PopupMenu(context, v)
                popup.menu.add(
                    1, MENU_DELETE, 1,
                    context
                        .getString(R.string.menu_delete)
                )
                popup.setOnMenuItemClickListener { item ->
                    if (item.itemId == MENU_DELETE) {
                        viewListener.onDeleteReviewResponse(element)
                        true
                    } else {
                        false
                    }
                }
                popup.show()
            }
        } else replyOverflow.visibility = View.GONE
    }

    private fun toggleReply() {
        isReplyOpened = !isReplyOpened
        if (isReplyOpened) {
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

    private fun getFormattedReplyName(responseBy: String?): String {
        return BY + " <b>" + responseBy + "</b>"
    }

    private fun getFormattedTime(reviewTime: String?): String {
        return TimeConverter.generateTimeYearly(reviewTime!!.replace("WIB", ""))
    }

    private fun getReview(review: String?): CharSequence? {
        return if (MethodChecker.fromHtml(review).length > MAX_CHAR) {
            val subDescription =
                MethodChecker.fromHtml(review).toString()
                    .substring(0, MAX_CHAR)
            HtmlLinkHelper(
                context, subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                        + context.getString(R.string.review_expand)
            ).spannedString
        } else {
            MethodChecker.fromHtml(review)
        }
    }

    private fun convertToAdapterViewModel(reviewAttachment: List<ImageAttachmentUiModel?>?): ArrayList<ImageUpload> {
        val list = ArrayList<ImageUpload>()
        for (vm in reviewAttachment!!) {
            list.add(
                ImageUpload(
                    vm.uriThumbnail,
                    vm.uriLarge,
                    vm.description, vm.attachmentId.toString()
                )
            )
        }
        return list
    }

    private fun getReviewerNameText(element: InboxReputationDetailItemUiModel): String? {
        return if (element.isReviewIsAnonymous
            && element.tab != ReviewInboxConstants.TAB_BUYER_REVIEW
        ) {
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

    private fun canShowOverflow(element: InboxReputationDetailItemUiModel): Boolean {
        return (element.isReviewIsEditable
                || element.tab == ReviewInboxConstants.TAB_BUYER_REVIEW || !TextUtils.isEmpty(
            element.productName
        ))
    }

    private fun isOwnProduct(element: InboxReputationDetailItemUiModel): Boolean {
        return (viewListener.userSession
            .shopId
                == element.shopId.toString())
    }

    private fun onReviewOverflowClicked(element: InboxReputationDetailItemUiModel): View.OnClickListener {
        return View.OnClickListener { v ->
            viewListener.onClickReviewOverflowMenu(element, adapterPosition)
            val popup = PopupMenu(context, v)
            if (element.tab == ReviewInboxConstants.TAB_BUYER_REVIEW) popup.menu.add(
                1, MENU_REPORT, 2, context
                    .getString(R.string.menu_report)
            )
            if (!TextUtils.isEmpty(element.productName)) popup.menu.add(
                1, MENU_SHARE, 3, context
                    .getString(R.string.menu_share)
            )
            popup.setOnMenuItemClickListener { item ->
                if (item.itemId == MENU_REPORT) {
                    viewListener.onGoToReportReview(
                        element.shopId,
                        element.reviewId
                    )
                    true
                } else if (item.itemId == MENU_SHARE) {
                    viewListener.onShareReview(element, adapterPosition)
                    true
                } else {
                    false
                }
            }
            popup.show()
        }
    }

    private fun setChevronDownImage() {
        replyArrow.setImageDrawable(
            getIconUnifyDrawable(
                context,
                IconUnify.CHEVRON_DOWN,
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
            )
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.inbox_reputation_detail_item
        private const val MAX_CHAR = 50
        private const val BY = "Oleh"
        private const val MENU_EDIT = 101
        private const val MENU_REPORT = 102
        private const val MENU_DELETE = 103
        private const val MENU_SHARE = 104
    }

    init {
        context = itemView.context
        this.viewListener = viewListener
        productName = itemView.findViewById<View>(R.id.product_name) as Typography
        productAvatar = itemView.findViewById<View>(R.id.product_image) as ImageView
        emptyReviewText = itemView.findViewById<View>(R.id.empty_review_text) as Typography
        viewReview = itemView.findViewById(R.id.review_layout)
        reviewerName = itemView.findViewById<View>(R.id.reviewer_name) as Typography
        reviewTime = itemView.findViewById<View>(R.id.review_time) as Typography
        reviewAttachment = itemView.findViewById<View>(R.id.product_review_image) as RecyclerView
        reviewOverflow = itemView.findViewById<View>(R.id.review_overflow) as ImageView
        review = itemView.findViewById<View>(R.id.review) as Typography
        reviewStar = itemView.findViewById<View>(R.id.product_rating) as RatingBar
        giveReview = itemView.findViewById(R.id.add_review_layout)
        adapter = ImageUploadAdapter.createAdapter(itemView.context)
        adapter.setCanUpload(false)
        adapter.setListener(onImageClicked())
        reviewAttachment.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL, false
        )
        reviewAttachment.adapter = adapter
        sellerReplyLayout = itemView.findViewById(R.id.seller_reply_layout)
        seeReplyLayout = itemView.findViewById(R.id.see_reply_layout)
        seeReplyText = seeReplyLayout.findViewById<View>(R.id.see_reply_button) as Typography
        replyArrow = seeReplyLayout.findViewById<View>(R.id.reply_chevron) as ImageView
        replyReviewLayout = itemView.findViewById(R.id.reply_review_layout)
        sellerName = itemView.findViewById<View>(R.id.seller_reply_name) as Typography
        sellerReplyTime = itemView.findViewById<View>(R.id.seller_reply_time) as Typography
        sellerReply = itemView.findViewById<View>(R.id.seller_reply) as Typography
        replyOverflow = itemView.findViewById<View>(R.id.reply_overflow) as ImageView
        sellerAddReplyLayout = itemView.findViewById(R.id.seller_add_reply_layout)
        sellerAddReplyEditText =
            itemView.findViewById<View>(R.id.seller_reply_edit_text) as EditText
        sendReplyButton = itemView.findViewById<View>(R.id.send_button) as ImageView
        sellerAddReplyEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (TextUtils.isEmpty(sellerAddReplyEditText.text.toString())) {
                    ImageHandler.loadImageWithIdWithoutPlaceholder(
                        sendReplyButton,
                        R.drawable.ic_send_grey_transparent
                    )
                    sendReplyButton.isEnabled = false
                } else {
                    ImageHandler.loadImageWithIdWithoutPlaceholder(
                        sendReplyButton,
                        R.drawable.ic_send_green
                    )
                    sendReplyButton.isEnabled = true
                }
            }
        })
    }
}