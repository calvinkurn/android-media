package com.tokopedia.shop.info.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.FragmentReviewViewpagerItemBinding
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ReviewViewPagerItemFragment : BaseDaggerFragment() {

    companion object {
        private const val MAX_STAR_COUNT = 5
        private const val ICON_STAR_HEIGHT = 16
        private const val ICON_STAR_WIDTH = 16
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
            
            val hasReviewerLabel = review.reviewerLabel.isNotEmpty()
            if (hasReviewerLabel) {
                binding?.tpgBullet?.visible()
                binding?.tpgReviewerLabel?.text = review.reviewerLabel    
            }
            
            binding?.tpgCompletedReview?.text = review.likeDislike.totalLike.toString() + " lengkap"
            binding?.tpgReviewLikeCount?.text = review.likeDislike.likeStatus.toString() + " terbantu"
            binding?.tpgReviewtime?.text = review.reviewTime
            
            renderRatingBar(review.rating)
        }
    }

    private fun renderRatingBar(rating: Int) {
        for (currentIndex in Int.ONE..MAX_STAR_COUNT) {
            val context = binding?.ratingBarSlot?.context ?: return
            val iconUnify = createStarIconUnify(context)

            if (currentIndex <= rating) {
                iconUnify.setImage(
                    newIconId = IconUnify.STAR_FILLED,
                    newLightEnable = MethodChecker.getColor(
                        iconUnify.context,
                        unifyprinciplesR.color.Unify_YN300
                    ),
                    newLightDisable = MethodChecker.getColor(
                        iconUnify.context,
                        unifyprinciplesR.color.Unify_YN300
                    ),
                    newDarkEnable = MethodChecker.getColor(
                        iconUnify.context,
                        unifyprinciplesR.color.Unify_YN600
                    ),
                    newDarkDisable = MethodChecker.getColor(
                        iconUnify.context,
                        unifyprinciplesR.color.Unify_YN600
                    )
                )
            } else {
                iconUnify.setImage(
                    newIconId = IconUnify.STAR_FILLED,
                    newLightEnable = MethodChecker.getColor(
                        iconUnify.context,
                        unifyprinciplesR.color.Unify_NN300
                    ),
                    newLightDisable = MethodChecker.getColor(
                        iconUnify.context,
                        unifyprinciplesR.color.Unify_NN300
                    ),
                    newDarkEnable = MethodChecker.getColor(
                        iconUnify.context,
                        unifyprinciplesR.color.Unify_NN600
                    ),
                    newDarkDisable = MethodChecker.getColor(
                        iconUnify.context,
                        unifyprinciplesR.color.Unify_NN600
                    )
                )
            }

            binding?.ratingBarSlot?.addView(iconUnify)
        }
    }
    private fun createStarIconUnify(context: Context): IconUnify {
        val iconUnify = IconUnify(context = context)
        val params = LayoutParams(ICON_STAR_WIDTH.toPx(), ICON_STAR_HEIGHT.toPx())
        iconUnify.layoutParams = params
        
        return iconUnify
    }
    
    override fun getScreenName(): String = ReviewViewPagerItemFragment::class.java.simpleName

    override fun initInjector() {

    }


}
