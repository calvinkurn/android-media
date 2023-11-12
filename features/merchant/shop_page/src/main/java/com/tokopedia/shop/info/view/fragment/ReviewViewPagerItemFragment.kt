package com.tokopedia.shop.info.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.FragmentReviewViewpagerItemBinding
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.unifyprinciples.R as unifyprinciplesR


class ReviewViewPagerItemFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_REVIEW = "review"
        private const val MAX_ATTACHMENT = 5
        private const val MARGIN_START_REVIEW_IMAGE = 4
        private const val MARGIN_START_REVIEW_IMAGE_NO_MARGIN = 0
        private const val MARGIN_PARENT_START = 32
        private const val MARGIN_PARENT_END = 32
        private const val REVIEW_TEXT_MAX_LINES = 3

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
    private var isReviewTextAlreadyExpanded = false
    
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
    
    override fun onResume() {
        super.onResume()
        renderReview(review)
    }

    private fun renderReview(review: ShopReview.Review?) {
        review?.let {
            binding?.root?.setOnClickListener { onAttachmentImageClick(review) }
            binding?.imgAvatar?.loadImage(review.avatar)
            binding?.tpgReviewerName?.text = review.reviewerName

            val hasReviewerLabel = review.reviewerLabel.isNotEmpty()
            if (hasReviewerLabel) {
                binding?.tpgBullet?.visible()
                binding?.tpgReviewerLabel?.text = review.reviewerLabel
            }

            if (isReviewTextAlreadyExpanded) {
                binding?.tpgReviewText?.text = review.reviewText
            } else {
                renderReviewText(review)    
            }
            
            renderCompletedReview(review.likeDislike.likeStatus)
            renderLikeCount(review.likeDislike.likeStatus, review.likeDislike.totalLike)
            renderReviewImages(review.attachments)
        }
    }

    
    private fun renderReviewText(review: ShopReview.Review) {
        binding?.tpgReviewText?.text = review.reviewText
        
        binding?.tpgReviewText?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                
                binding?.tpgReviewText?.viewTreeObserver?.removeOnPreDrawListener(this)

                val reviewTextView = binding?.tpgReviewText ?: return true

                try {
                    val lineCount = reviewTextView.lineCount
                    if (lineCount >= REVIEW_TEXT_MAX_LINES) {
                        handleMaxLines(reviewTextView, review.reviewText)
                    }
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }

                return true
            }

        })

    }
    
    private fun handleMaxLines(reviewTextView: Typography, reviewText: String) {
        val ctaText = context?.getString(R.string.shop_info_more).orEmpty()
        val ctaTextLength = ctaText.length.orZero()

        val ellipsisText = "... "
        val ellipsisTextLength = ellipsisText.length

        val layout = reviewTextView.layout
        val start = layout.getLineStart(0)
        val end = layout.getLineEnd(2)

        val endIndex = end - ellipsisTextLength - ctaTextLength

        val reviewTextSubstring = reviewText.substring(start, endIndex)
        val reviewTextWithCta = "$reviewTextSubstring$ellipsisText$ctaText"

        val spannableString = SpannableString(reviewTextWithCta)
        val boldSpan = StyleSpan(Typeface.BOLD)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                reviewTextView.text = reviewText
                isReviewTextAlreadyExpanded = true
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(context ?: return, unifyprinciplesR.color.Unify_GN500)
                ds.isUnderlineText = false
            }
        }

        val indexOfCtaText = reviewTextWithCta.indexOf(ctaText)

        spannableString.setSpan(
            boldSpan,
            indexOfCtaText,
            reviewTextWithCta.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            clickableSpan,
            indexOfCtaText,
            reviewTextWithCta.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        reviewTextView.movementMethod = LinkMovementMethod.getInstance()
        reviewTextView.text = spannableString
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

        binding?.layoutImagesContainer?.removeAllViews()
        
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
}
