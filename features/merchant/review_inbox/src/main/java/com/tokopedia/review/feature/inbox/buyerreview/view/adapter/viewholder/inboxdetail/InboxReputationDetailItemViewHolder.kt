package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageRounded
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

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.inbox_reputation_detail_item
        private const val MAX_CHAR = 50
        private const val BY = "Oleh"
        private const val MENU_REPORT = 102
        private const val MENU_DELETE = 103
        private const val MENU_SHARE = 104
        const val ROUNDED_FIVE = 5.0f
        const val ROUNDED_FIFTEEN = 15.0f
        const val ROTATION_180 = 180f
        const val MENU_1 = 1
        const val MENU_2 = 2
        const val MENU_3 = 3
    }

    var isReplyOpened = false
    private val productName: Typography? = itemView.findViewById(R.id.product_name)
    private val productAvatar: ImageView? = itemView.findViewById(R.id.product_image)
    private val emptyReviewText: Typography? = itemView.findViewById(R.id.empty_review_text)
    private val viewReview: View? = itemView.findViewById(R.id.review_layout)
    private val reviewerName: Typography? = itemView.findViewById(R.id.reviewer_name)
    private val reviewTime: Typography? = itemView.findViewById(R.id.review_time)
    private val reviewAttachment: RecyclerView? = itemView.findViewById(R.id.product_review_image)
    private val reviewOverflow: ImageView? = itemView.findViewById(R.id.review_overflow)
    private val review: Typography? = itemView.findViewById(R.id.review)
    private val reviewStar: RatingBar? = itemView.findViewById(R.id.product_rating)
    private val giveReview: View? = itemView.findViewById(R.id.add_review_layout)
    private val adapter: ImageUploadAdapter = ImageUploadAdapter.createAdapter(itemView.context)
    private val replyReviewLayout: View? = itemView.findViewById(R.id.reply_review_layout)
    private val seeReplyLayout: View? = itemView.findViewById(R.id.see_reply_layout)
    private val seeReplyText: Typography? = seeReplyLayout?.findViewById(R.id.see_reply_button)
    private val replyArrow: ImageView? = seeReplyLayout?.findViewById(R.id.reply_chevron)
    private val sellerReplyLayout: View? = itemView.findViewById(R.id.seller_reply_layout)
    private val sellerName: Typography? = itemView.findViewById(R.id.seller_reply_name)
    private val sellerReplyTime: Typography?
    private val sellerReply: Typography?
    private val replyOverflow: ImageView?
    private val sellerAddReplyLayout: View?
    private val sellerAddReplyEditText: EditText?
    private val sendReplyButton: ImageView?

    init {
        adapter.setCanUpload(false)
        adapter.setListener(onImageClicked())
        reviewAttachment?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL, false
        )
        reviewAttachment?.adapter = adapter
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
        when {
            element.isProductDeleted -> setDeletedProduct()
            element.isProductBanned -> setBannedProduct()
            else -> setRegularProduct(element)
        }
        if (!element.isReviewHasReviewed) {
            setNoReviewYet()
        } else if (element.isReviewHasReviewed && element.isReviewSkipped) {
            setSkipped()
        } else {
            emptyReviewText?.hide()
            viewReview?.show()
            giveReview?.hide()
            reviewerName?.text = MethodChecker.fromHtml(getReviewerNameText(element))
            reviewerName?.setOnClickListener { viewListener.onGoToProfile(element.reviewerId) }
            val time: String = if (element.isReviewIsEdited) {
                getFormattedTime(element.reviewTime) +
                        itemView.context.getString(R.string.edited)
            } else {
                getFormattedTime(element.reviewTime)
            }
            reviewTime?.text = time
            reviewStar?.rating = element.reviewStar.toFloat()
            review?.text = getReview(element.review)
            review?.setOnClickListener {
                if (review.text.toString()
                        .endsWith(itemView.context.getString(R.string.more_to_complete))
                ) {
                    review.text = element.review
                }
            }
            setChevronDownImage()
            if (canShowOverflow(element)) {
                reviewOverflow?.show()
                reviewOverflow?.setOnClickListener(onReviewOverflowClicked(element))
            } else {
                reviewOverflow?.hide()
            }
            if (!TextUtils.isEmpty(element.reviewResponseUiModel.responseMessage)) {
                setSellerReply(element)
            } else {
                seeReplyText?.hide()
                replyArrow?.hide()
                sellerReplyLayout?.hide()
                if (element.tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
                    sellerAddReplyLayout?.show()
                } else {
                    sellerAddReplyLayout?.hide()
                }
            }
        }
        showOrHideGiveReviewLayout(element)
        adapter.addList(convertToAdapterViewModel(element.reviewAttachment))
        adapter.notifyDataSetChanged()
        sendReplyButton?.setOnClickListener {
            viewListener.onSendReplyReview(
                element,
                sellerAddReplyEditText?.text.toString()
            )
        }
    }

    private fun setDeletedProduct() {
        productName?.text = itemView.context.getString(R.string.product_is_deleted)
        productAvatar?.loadImageRounded(
            R.drawable.ic_product_deleted,
            ROUNDED_FIVE
        )
    }

    private fun setBannedProduct() {
        productName?.text = itemView.context.getString(R.string.product_is_banned)
        productAvatar?.loadImageRounded(
            R.drawable.ic_product_deleted,
            ROUNDED_FIVE
        )
    }

    private fun setRegularProduct(element: InboxReputationDetailItemUiModel) {
        productName?.text = MethodChecker.fromHtml(element.productName)
        productName?.setOnClickListener {
            viewListener.onGoToProductDetail(
                element.productId, element
                    .productAvatar, element.productName
            )
        }
        productAvatar?.loadImageRounded(
            element.productAvatar,
            ROUNDED_FIFTEEN
        )
        productAvatar?.setOnClickListener {
            viewListener.onGoToProductDetail(
                element.productId,
                element.productAvatar, element.productName
            )
        }
    }

    private fun setNoReviewYet() {
        viewReview?.hide()
        seeReplyLayout?.hide()
        emptyReviewText?.show()
        emptyReviewText?.setText(R.string.not_reviewed)
    }

    private fun setSkipped() {
        emptyReviewText?.show()
        viewReview?.hide()
        seeReplyLayout?.hide()
        emptyReviewText?.show()
        emptyReviewText?.setText(R.string.review_is_skipped)
    }

    private fun showOrHideGiveReviewLayout(element: InboxReputationDetailItemUiModel) {
        giveReview?.showWithCondition(!shouldHideGiveReview(element))
    }

    private fun shouldHideGiveReview(element: InboxReputationDetailItemUiModel): Boolean {
        return element.tab == ReviewInboxConstants.TAB_BUYER_REVIEW || element.isReviewSkipped
                || isOwnProduct(element)
                || element.isReviewHasReviewed
    }

    private fun setSellerReply(element: InboxReputationDetailItemUiModel) {
        sellerAddReplyLayout?.hide()
        sellerReplyLayout?.show()
        seeReplyLayout?.show()
        seeReplyText?.show()
        replyArrow?.show()
        seeReplyText?.setOnClickListener {
            toggleReply()
            viewListener.onClickToggleReply(element, adapterPosition)
        }
        replyArrow?.setOnClickListener { toggleReply() }
        val reviewResponseUiModel = element.reviewResponseUiModel
        sellerName?.text = MethodChecker.fromHtml(
            getFormattedReplyName(
                reviewResponseUiModel
                    .responseBy
            )
        )
        sellerName?.setOnClickListener { viewListener.onGoToShopInfo(element.shopId) }
        sellerReplyTime?.text = getFormattedTime(reviewResponseUiModel.responseCreateTime)
        sellerReply?.text = MethodChecker.fromHtml(reviewResponseUiModel.responseMessage)
        sellerAddReplyEditText?.setText("")
        if (element.tab == ReviewInboxConstants.TAB_BUYER_REVIEW) {
            seeReplyLayout?.show()
            replyOverflow?.show()
            replyOverflow?.setOnClickListener { v ->
                val popup = PopupMenu(itemView.context, v)
                popup.menu.add(
                    1, MENU_DELETE, 1,
                    itemView.context
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
        } else replyOverflow?.hide()
    }

    private fun toggleReply() {
        isReplyOpened = !isReplyOpened
        if (isReplyOpened) {
            seeReplyText?.text = itemView.context.getText(R.string.close_reply)
            replyArrow?.rotation = ROTATION_180
            replyReviewLayout?.show()
            viewListener.onSmoothScrollToReplyView(adapterPosition)
        } else {
            seeReplyText?.text = itemView.context.getText(R.string.see_reply)
            replyArrow?.rotation = 0f
            replyReviewLayout?.hide()
        }
    }

    private fun getFormattedReplyName(responseBy: String?): String {
        return "$BY <b>$responseBy</b>"
    }

    private fun getFormattedTime(reviewTime: String): String {
        return TimeConverter.generateTimeYearly(reviewTime.replace("WIB", ""))
    }

    private fun getReview(review: String?): CharSequence? {
        return if (MethodChecker.fromHtml(review).length > MAX_CHAR) {
            val subDescription =
                MethodChecker.fromHtml(review).toString()
                    .substring(0, MAX_CHAR)
            HtmlLinkHelper(
                itemView.context, subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                        + itemView.context.getString(R.string.review_expand)
            ).spannedString
        } else {
            MethodChecker.fromHtml(review)
        }
    }

    private fun convertToAdapterViewModel(reviewAttachment: List<ImageAttachmentUiModel>): ArrayList<ImageUpload> {
        val list = ArrayList<ImageUpload>()
        reviewAttachment.forEach {
            list.add(
                ImageUpload(
                    it.uriThumbnail,
                    it.uriLarge,
                    it.description,
                    it.attachmentId.toString()
                )
            )
        }
        return list
    }

    private fun getReviewerNameText(element: InboxReputationDetailItemUiModel): String {
        return if (element.isReviewIsAnonymous
            && element.tab != ReviewInboxConstants.TAB_BUYER_REVIEW
        ) {
            getAnonymousName(element.reviewerName)
        } else {
            element.reviewerName
        }
    }

    private fun getAnonymousName(name: String): String {
        val first = name.substring(0, 1)
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
        return (viewListener.getShopId() == element.shopId.toString())
    }

    private fun onReviewOverflowClicked(element: InboxReputationDetailItemUiModel): View.OnClickListener {
        return View.OnClickListener { v ->
            viewListener.onClickReviewOverflowMenu(element, adapterPosition)
            val popup = PopupMenu(itemView.context, v)
            if (element.tab == ReviewInboxConstants.TAB_BUYER_REVIEW) popup.menu.add(
                MENU_1, MENU_REPORT, MENU_2, itemView.context
                    .getString(R.string.menu_report)
            )
            if (!TextUtils.isEmpty(element.productName)) popup.menu.add(
                MENU_1, MENU_SHARE, MENU_3, itemView.context
                    .getString(R.string.menu_share)
            )
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    MENU_REPORT -> {
                        viewListener.onGoToReportReview(element.shopId, element.reviewId)
                        true
                    }
                    MENU_SHARE -> {
                        viewListener.onShareReview(element, adapterPosition)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popup.show()
        }
    }

    private fun setChevronDownImage() {
        replyArrow?.setImageDrawable(
            getIconUnifyDrawable(
                itemView.context,
                IconUnify.CHEVRON_DOWN,
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700
                )
            )
        )
    }
}