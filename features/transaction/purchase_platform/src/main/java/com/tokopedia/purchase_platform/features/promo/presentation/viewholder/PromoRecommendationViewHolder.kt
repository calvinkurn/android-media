package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.animation.Animator
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoRecommendationUiModel
import kotlinx.android.synthetic.main.item_promo_recommendation.view.*

class PromoRecommendationViewHolder(private val view: View,
                                    private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoRecommendationUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_recommendation
    }

    override fun bind(element: PromoRecommendationUiModel) {
        if (element.uiState.isButtonSelectEnabled) {
            itemView.button_apply_promo_recommendation.setOnClickListener {
                itemView.button_apply_promo_recommendation.isLoading = true
                listener.onClickApplyRecommendedPromo(element)
            }
            itemView.image_check_promo_recommendation.gone()
            itemView.button_apply_promo_recommendation.isEnabled = true
            itemView.button_apply_promo_recommendation.text = "Pilih"
        } else {
            itemView.button_apply_promo_recommendation.isLoading = false
            playAnimation()
            itemView.image_check_promo_recommendation.show()
            itemView.button_apply_promo_recommendation.isEnabled = false
            itemView.button_apply_promo_recommendation.text = "Dipilih"
        }
        itemView.label_promo_recommendation_title.text = element.uiData.title
        itemView.label_promo_recommendation_sub_title.text = element.uiData.subTitle
    }

    private fun playAnimation() {
        itemView.lottie_button_apply_promo_recommendation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animator: Animator?) {}

            override fun onAnimationEnd(animator: Animator?) {

            }

            override fun onAnimationCancel(animator: Animator?) {}

            override fun onAnimationStart(animator: Animator?) {}
        })
        if (!itemView.lottie_button_apply_promo_recommendation.isAnimating) {
            itemView.lottie_button_apply_promo_recommendation.playAnimation()
        }

    }

}