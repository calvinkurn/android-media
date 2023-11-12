package com.tokopedia.shop.info.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getScreenWidth
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
        private const val BUNDLE_KEY_REVIEW = "review"
        private const val MAX_ATTACHMENT = 5
        private const val MARGIN_START_REVIEW_IMAGE = 4
        private const val MARGIN_START_REVIEW_IMAGE_NO_MARGIN = 0
        private const val MARGIN_PARENT_START = 32
        private const val MARGIN_PARENT_END = 32

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
            renderReviewImages(review.attachments)
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

    private fun renderReviewImages(reviewImages: List<ShopReview.Review.Attachment>) {
        val marginStartFromParentStart = MARGIN_PARENT_START.toPx()
        val marginEndFromParentEnd = MARGIN_PARENT_END.toPx()
        val totalItemSpacingMarginStart = (MAX_ATTACHMENT - 1) * MARGIN_START_REVIEW_IMAGE.toPx()
        val usableScreenWidth = getScreenWidth() - marginStartFromParentStart - totalItemSpacingMarginStart - marginEndFromParentEnd
        val reviewImageMaxHeight = usableScreenWidth / MAX_ATTACHMENT
        val reviewImageMaxWidth = usableScreenWidth / MAX_ATTACHMENT

        reviewImages
            .take(MAX_ATTACHMENT)
            .forEachIndexed { index, attachment ->
                val review = review ?: return
                val reviewImage = createReviewImage(
                    review,
                    index,
                    attachment,
                    reviewImages.size,
                    reviewImageMaxHeight,
                    reviewImageMaxWidth
                )

                binding?.layoutImagesContainer?.addView(reviewImage)
            }
    }

    fun setOnAttachmentImageClick(onAttachmentImageClick: (ShopReview.Review) -> Unit) {
        this.onAttachmentImageClick = onAttachmentImageClick
    }

    fun setOnAttachmentImageViewAllClick(onAttachmentImageViewAllClick: (ShopReview.Review) -> Unit) {
        this.onAttachmentImageViewAllClick = onAttachmentImageViewAllClick
    }

    private fun createReviewImage(
        review: ShopReview.Review,
        currentIndex: Int,
        attachment: ShopReview.Review.Attachment,
        reviewCount: Int,
        reviewImageMaxHeight: Int,
        reviewImageMaxWidth: Int
    ): View {
        val reviewImageView = layoutInflater.inflate(R.layout.item_shop_info_attachment, null, false)

        val imgReview = reviewImageView.findViewById<ImageUnify>(R.id.imgReview)
        val overlay = reviewImageView.findViewById<View>(R.id.overlay)

        val layoutParams = FrameLayout.LayoutParams(reviewImageMaxWidth, reviewImageMaxHeight)
        layoutParams.marginStart = if (currentIndex == Int.ZERO) {
            MARGIN_START_REVIEW_IMAGE_NO_MARGIN.toPx()
        } else {
            MARGIN_START_REVIEW_IMAGE.toPx()
        }
        imgReview.layoutParams = layoutParams
        overlay.layoutParams = layoutParams

        imgReview.loadImage(attachment.thumbnailURL)
        imgReview.setOnClickListener { onAttachmentImageViewAllClick(review) }

        val isRenderingLastReviewImage = currentIndex == MAX_ATTACHMENT - Int.ONE
        val showCtaViewAll = reviewCount > MAX_ATTACHMENT && isRenderingLastReviewImage
        val remainingReviewImageCount = reviewCount - MAX_ATTACHMENT

        overlay.isVisible = showCtaViewAll

        val tpgCtaViewAll = reviewImageView.findViewById<Typography>(R.id.tpgCtaViewAll)
        tpgCtaViewAll.isVisible = showCtaViewAll
        tpgCtaViewAll.text = getString(
            R.string.shop_info_placeholder_attachment_text,
            remainingReviewImageCount.toString()
        )

        return reviewImageView
    }

    private fun expandReviewText() {
        binding?.tpgReviewText?.maxLines = Int.MAX_VALUE
    }
}
