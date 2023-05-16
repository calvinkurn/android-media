package com.tokopedia.people.views.viewholder

import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.setSpanOnText
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.people.R
import com.tokopedia.people.databinding.ItemUserReviewBinding
import com.tokopedia.people.databinding.ItemUserReviewShimmerBinding
import com.tokopedia.people.databinding.UpItemUserPostLoadingBinding
import com.tokopedia.people.utils.getBoldSpan
import com.tokopedia.people.utils.getClickableSpan
import com.tokopedia.people.utils.getGreenColorSpan
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on May 15, 2023
 */
class UserReviewViewHolder private constructor() {

    class Review(
        private val binding: ItemUserReviewBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserReviewUiModel.Review) {
            binding.apply {
                imgProduct.setImageUrl(item.product.productImageURL)
                tvProduct.text = item.product.productName

                tvVariant.shouldShowWithAction(item.product.productVariant.variantName.isNotEmpty()) {
                    tvVariant.text = itemView.context.getString(
                        R.string.up_profile_user_review_product_variant_template,
                        item.product.productVariant.variantName
                    )
                }

                tvTime.text = item.reviewTime

                setupStar(icStar1, item.rating >= 1)
                setupStar(icStar2, item.rating >= 2)
                setupStar(icStar3, item.rating >= 3)
                setupStar(icStar4, item.rating >= 4)
                setupStar(icStar5, item.rating >= 5)

                setupReviewText(item.reviewText)

                /** TODO: setup media */

                setupLike(item)
            }
        }

        private fun setupStar(icon: IconUnify, isFilled: Boolean) {
            val color = if (isFilled) {
                unifyR.color.Unify_YN300
            } else {
                unifyR.color.Unify_NN300
            }

            icon.setImage(
                IconUnify.STAR_FILLED,
                newLightEnable = MethodChecker.getColor(itemView.context, color),
                newDarkEnable = MethodChecker.getColor(itemView.context, color)
            )
        }

        private fun setupReviewText(reviewText: String) {
            val formattedReviewText = SpannableStringBuilder()

            if (reviewText.length > MAX_REVIEW_CHAR) {
                formattedReviewText.append(
                    itemView.context.getString(
                        R.string.up_profile_user_review_text_template,
                        reviewText.substring(0, MAX_REVIEW_CHAR)
                    )
                )

                /** TODO: handle if the review is opened before, dont close it */
                val highlightedText = itemView.context.getString(R.string.up_link_see_more)
                formattedReviewText.setSpanOnText(
                    highlightedText,
                    getClickableSpan {
                        binding.tvReview.text = reviewText
                    },
                    getBoldSpan(),
                    getGreenColorSpan(itemView.context)
                )
            } else {
                formattedReviewText.append(reviewText)
            }

            binding.tvReview.text = formattedReviewText
            binding.tvReview.movementMethod = LinkMovementMethod.getInstance()
        }

        private fun setupLike(item: UserReviewUiModel.Review) {
            binding.icLike.setImage(if (item.likeDislike.isLike) IconUnify.THUMB_FILLED else IconUnify.THUMB)
            binding.icLike.setOnClickListener {
                listener.onClickLike(item)
            }
            binding.tvLikeCounter.text = item.likeDislike.totalLike.toString()
            binding.tvLikeCounter.setOnClickListener {
                listener.onClickLike(item)
            }
        }

        companion object {

            private const val MAX_REVIEW_CHAR = 140
            fun create(
                parent: ViewGroup,
                listener: Listener
            ) = Review(
                ItemUserReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }

        interface Listener {
            fun onClickLike(review: UserReviewUiModel.Review)
        }
    }

    class Shimmer(
        binding: ItemUserReviewShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(
                parent: ViewGroup
            ) = Shimmer(
                ItemUserReviewShimmerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    class Loading(
        binding: UpItemUserPostLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(
                parent: ViewGroup
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
