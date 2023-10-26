package com.tokopedia.shop.info.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.FragmentReviewViewpagerItemBinding
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ReviewViewPagerItemFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_REVIEW = "review"
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
            binding?.tpgReviewText?.text = review.reviewText
            binding?.imgAvatar?.loadImage(review.avatar)
            binding?.tpgReviewerName?.text = review.reviewerName
            binding?.tpgReviewerLabel?.text = review.reviewerLabel

            binding?.tpgCompletedReview?.text = review.likeDislike.totalLike.toString() + "lengkap"
            binding?.tpgReviewLikeCount?.text = review.likeDislike.likeStatus.toString() + "terbantu"
            binding?.tpgReviewtime?.text = review.reviewTime
            
            
            //binding?.ratingBarSlot?.addView()
        }
    }

    override fun getScreenName(): String = ReviewViewPagerItemFragment::class.java.simpleName

    override fun initInjector() {

    }


}
