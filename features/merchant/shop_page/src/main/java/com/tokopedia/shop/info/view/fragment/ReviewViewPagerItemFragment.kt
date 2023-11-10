package com.tokopedia.shop.info.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.FragmentReviewViewpagerItemBinding
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ReviewViewPagerItemFragment : BaseDaggerFragment() {

    companion object {
        private const val IMAGE_REVIEW_SIZE = 56
        private const val BUNDLE_KEY_REVIEW = "review"
        private const val MAX_ATTACHMENT = 5
        private const val LEFT_MARGIN = 4
        private const val ATTACHMENT_CORNER_RADIUS = 12

        fun newInstance(review: ShopReview.Review): ReviewViewPagerItemFragment {
            return ReviewViewPagerItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_REVIEW, review)
                }
            }
        }
    }

    private var binding by autoClearedNullable<FragmentReviewViewpagerItemBinding>()
    private val review by lazy { arguments?.getParcelable<ShopReview.Review>(BUNDLE_KEY_REVIEW) }
    private var onAttachmentImageClick: (ShopReview.Review) -> Unit = {}
    private var onAttachmentImageViewAllClick: (ShopReview.Review) -> Unit = {}

    override fun getScreenName(): String = ReviewViewPagerItemFragment::class.java.simpleName

    override fun initInjector() {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewViewpagerItemBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderReview(review)
    }

    private fun renderReview(review: ShopReview.Review?) {
        review?.let {
            binding?.tpgReviewText?.text = MethodChecker.fromHtml(review.reviewText)
            binding?.imgAvatar?.loadImage(review.avatar)
            binding?.tpgReviewerName?.text = review.reviewerName

            val hasReviewerLabel = review.reviewerLabel.isNotEmpty()
            if (hasReviewerLabel) {
                binding?.tpgBullet?.visible()
                binding?.tpgReviewerLabel?.text = review.reviewerLabel
            }

            renderCompletedReview(review.likeDislike.likeStatus)
            renderLikeCount(review.likeDislike.likeStatus, review.likeDislike.totalLike)

            renderAttachments(review.attachments)
        }
    }

    private fun renderCompletedReview(completedReviewCount: Int) {
        val showCompletedReview = completedReviewCount.isMoreThanZero()

        if (showCompletedReview) {
            binding?.tpgCompletedReview?.text =
                getString(R.string.shop_info_placeholder_complete_review, completedReviewCount)
        }
    }

    private fun renderLikeCount(completedReviewCount: Int, totalLike: Int) {
        val showTotalLike = totalLike.isMoreThanZero()
        val showCompletedReview = completedReviewCount.isMoreThanZero()
        val showBullet = showCompletedReview && showTotalLike

        binding?.tpgReviewLikeCount?.isVisible = showTotalLike
        binding?.tpgBulletReview?.isVisible = showBullet

        if (showTotalLike) {
            binding?.tpgReviewLikeCount?.text =
                getString(R.string.shop_info_placeholder_useful_review, totalLike)
        }
    }

    private fun renderAttachments(attachments: List<ShopReview.Review.Attachment>) {
        attachments
            .take(MAX_ATTACHMENT)
            .forEachIndexed { index, attachment ->
                val isFirstItem = index == Int.ZERO
                val isLastItem = index == MAX_ATTACHMENT - Int.ONE
                val context = binding?.layoutImagesContainer?.context ?: return

                val attachmentImageView = if (isLastItem) {
                    createAttachmentImageWithCta(review ?: return, attachment, attachments.size)
                } else {
                    val marginStart = if (isFirstItem) Int.ZERO else LEFT_MARGIN.toPx()
                    createAttachmentImage(review ?: return, context, attachment.thumbnailURL, marginStart)
                }

                binding?.layoutImagesContainer?.addView(attachmentImageView)
            }
    }
    private fun createAttachmentImage(
        attachment: ShopReview.Review,
        context: Context,
        imageUrl: String,
        marginStartPx: Int
    ): ImageUnify {
        val imageUnify = ImageUnify(context = context).apply {
            cornerRadius = ATTACHMENT_CORNER_RADIUS
            type = ImageUnify.TYPE_RECT
        }

        val params = LayoutParams(IMAGE_REVIEW_SIZE.toPx(), IMAGE_REVIEW_SIZE.toPx())
        params.marginStart = marginStartPx

        imageUnify.layoutParams = params

        imageUnify.loadImage(imageUrl)
        imageUnify.setOnClickListener { onAttachmentImageClick(review ?: return@setOnClickListener) }

        return imageUnify
    }

    fun setOnAttachmentImageClick(onAttachmentImageClick: (ShopReview.Review) -> Unit) {
        this.onAttachmentImageClick = onAttachmentImageClick
    }

    fun setOnAttachmentImageViewAllClick(onAttachmentImageViewAllClick: (ShopReview.Review) -> Unit) {
        this.onAttachmentImageViewAllClick = onAttachmentImageViewAllClick
    }

    private fun createAttachmentImageWithCta(
        review: ShopReview.Review,
        attachment: ShopReview.Review.Attachment,
        totalAttachment: Int
    ): View {
        val attachmentView = layoutInflater.inflate(R.layout.item_shop_info_attachment, null, false)
        val params = LayoutParams(IMAGE_REVIEW_SIZE.toPx(), IMAGE_REVIEW_SIZE.toPx())
        params.marginStart = LEFT_MARGIN.toPx()

        attachmentView.layoutParams = params

        val imgAttachmentImage = attachmentView.findViewById<ImageUnify>(R.id.imgAttachmentImage)
        imgAttachmentImage.loadImage(attachment.thumbnailURL) val tpgCollapsedAttachmentCount = attachmentView.findViewById<Typography>(R.id.tpgCollapsedAttachmentCount)
        val collapsedAttachmentCount = totalAttachment - MAX_ATTACHMENT
        tpgCollapsedAttachmentCount.text = getString(
            R.string.shop_info_placeholder_attachment_text,
            collapsedAttachmentCount.toString()
        )

        imgAttachmentImage.setOnClickListener { onAttachmentImageViewAllClick(review) }
        return attachmentView
    }

    private fun expandReviewText() {
        binding?.tpgReviewText?.maxLines = Int.MAX_VALUE
    }
}
