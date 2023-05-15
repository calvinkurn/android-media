package com.tokopedia.people.views.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.people.databinding.ItemUserReviewBinding
import com.tokopedia.people.databinding.UpItemUserPostLoadingBinding
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.R
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

                /** TODO: handle Selengkapnya */
                tvReview.text = item.reviewText

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

        companion object {
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
