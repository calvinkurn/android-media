package com.tokopedia.shop.info.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.FragmentReviewViewpagerItemBinding
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ReviewViewPagerItemFragment : BaseDaggerFragment() {

    companion object {
        private const val IMAGE_REVIEW_SIZE = 56
        private const val BUNDLE_KEY_REVIEW = "review"
        private const val MAX_ATTACHMENT = 4
        
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
        render(review)
    }

    private fun render(review: ShopReview.Review?) {
        review?.let {
            binding?.tpgReviewText?.text = MethodChecker.fromHtml(review.reviewText)
            binding?.imgAvatar?.loadImage(review.avatar)
            binding?.tpgReviewerName?.text = review.reviewerName
            
            val hasReviewerLabel = review.reviewerLabel.isNotEmpty()
            if (hasReviewerLabel) {
                binding?.tpgBullet?.visible()
                binding?.tpgReviewerLabel?.text = review.reviewerLabel    
            }
            
            binding?.tpgCompletedReview?.text = getString(R.string.shop_info_placeholder_complete_review, review.likeDislike.totalLike)
            binding?.tpgReviewLikeCount?.text = getString(R.string.shop_info_placeholder_useful_review, review.likeDislike.likeStatus)
            
            renderAttachments(review.attachments)
        }
    }

    private fun renderAttachments(attachments: List<ShopReview.Review.Attachment>) {
        attachments
            .take(MAX_ATTACHMENT)
            .forEachIndexed { index, attachment ->
                val isLastItem = index == MAX_ATTACHMENT - 1
                val context = binding?.layoutImagesContainer?.context ?: return

                val attachmentImageView = if (isLastItem) {
                    createAttachmentImageWithCta(context)
                } else {
                    createAttachmentImage(context)
                }
                
                attachmentImageView.loadImage(attachment.thumbnailURL)

                binding?.layoutImagesContainer?.addView(attachmentImageView)
            }
    }
    private fun createAttachmentImage(context: Context): ImageUnify {
        val imageUnify = ImageUnify(context = context)
        val params = LayoutParams(IMAGE_REVIEW_SIZE.toPx(), IMAGE_REVIEW_SIZE.toPx())
        imageUnify.layoutParams = params
        
        return imageUnify
    }
    
    private fun createAttachmentImageWithCta(context: Context): ImageUnify {
        val imageUnify = ImageUnify(context = context)
        val params = LayoutParams(IMAGE_REVIEW_SIZE.toPx(), IMAGE_REVIEW_SIZE.toPx())
        imageUnify.layoutParams = params

        return imageUnify
    }
}
