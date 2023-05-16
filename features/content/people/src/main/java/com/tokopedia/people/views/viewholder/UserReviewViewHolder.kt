package com.tokopedia.people.views.viewholder

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.setSpanOnText
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.people.databinding.ItemUserReviewBinding
import com.tokopedia.people.databinding.UpItemUserPostLoadingBinding
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.R
import com.tokopedia.people.utils.getBoldSpan
import com.tokopedia.people.utils.getClickableSpan
import com.tokopedia.people.utils.getGreenColorSpan
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on May 15, 2023
 */
class UserReviewViewHolder private constructor() {

    class Review(
        private val binding: ItemUserReviewBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserReviewUiModel.Review) {
            binding.apply {
                imgProduct.setImageUrl(item.product.productImageURL)
                tvProduct.text = item.product.productName

                if(item.product.productVariant.variantName.isNotEmpty()) {
                    tvVariant.text = itemView.context.getString(
                        R.string.up_profile_user_review_product_variant_template,
                        item.product.productVariant.variantName
                    )
                }

                setupStar(icStar1, item.rating >= 1)
                setupStar(icStar2, item.rating >= 2)
                setupStar(icStar3, item.rating >= 3)
                setupStar(icStar4, item.rating >= 4)
                setupStar(icStar5, item.rating >= 5)

                setupReviewText(tvReview, item.reviewText)

                tvLikeCounter.text = item.likeDislike.totalLike.toString()
            }
        }

        private fun setupStar(icon: IconUnify, isFilled: Boolean) {
            val color = if (isFilled) {
                unifyR.color.Unify_YN300
            } else {
                unifyR.color.Unify_NN300
            }

            icon.setImage(
                IconUnify.STAR,
                newLightEnable = MethodChecker.getColor(itemView.context, color),
                newDarkEnable = MethodChecker.getColor(itemView.context, color),
            )
        }

        private fun setupReviewText(tvReview: Typography, reviewText: String) {
            val result = SpannableStringBuilder()

            if (reviewText.length > MAX_REVIEW_CHAR) {
                result.append(
                    itemView.context.getString(
                        R.string.up_profile_user_review_text_template,
                        reviewText.substring(0, MAX_REVIEW_CHAR)
                    )
                )

                /** TODO: handle if the review is opened before, dont close it */
                val highlightedText = itemView.context.getString(R.string.up_link_see_more)
                result.setSpanOnText(
                    highlightedText,
                    getClickableSpan {
                        tvReview.text = reviewText
                    },
                    getBoldSpan(),
                    getGreenColorSpan(itemView.context)
                )
            } else {
                result.append(reviewText)
            }

            tvReview.text = reviewText
        }

        companion object {

            private const val MAX_REVIEW_CHAR = 140
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Review(
                ItemUserReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener,
            )
        }

        interface Listener {

        }
    }

    class Loading(
        binding: UpItemUserPostLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(
                parent: ViewGroup,
            ) = Loading(
                UpItemUserPostLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
