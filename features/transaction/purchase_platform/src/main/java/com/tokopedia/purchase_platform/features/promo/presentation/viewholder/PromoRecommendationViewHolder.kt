package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.animation.Animator
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.PromoCheckoutActionListener
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
            itemView.lottie_button_apply_promo_recommendation.progress = 0f
            itemView.lottie_button_apply_promo_recommendation.show()
            itemView.button_apply_promo_recommendation.setOnClickListener {
                itemView.button_apply_promo_recommendation.isLoading = true
                playAnimation()
                listener.onClickApplyRecommendedPromo()
            }
            itemView.image_check_promo_recommendation.gone()
            itemView.button_apply_promo_recommendation.isEnabled = true
            itemView.button_apply_promo_recommendation.text = "Pilih"
            itemView.label_promo_recommendation_title.text = String.format(itemView.context.getString(R.string.promo_checkout_label_promo_recommendation_title, element.uiData.promoCount))
        } else {
            itemView.button_apply_promo_recommendation.isLoading = false
            itemView.image_check_promo_recommendation.show()
            itemView.button_apply_promo_recommendation.isEnabled = false
            itemView.button_apply_promo_recommendation.text = "Dipilih"
            itemView.label_promo_recommendation_title.text = String.format(itemView.context.getString(R.string.promo_checkout_label_promo_recommendation_title_after_apply, element.uiData.promoCount))
        }
        val totalBenefitFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.uiData.promoTotalBenefit, false)
        itemView.label_promo_recommendation_sub_title.text = String.format(itemView.context.getString(R.string.promo_checkout_label_recommendation_benefit, totalBenefitFormatted))
    }

    private fun playAnimation() {
        itemView.lottie_button_apply_promo_recommendation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animator: Animator?) {}

            override fun onAnimationEnd(animator: Animator?) {
                itemView.lottie_button_apply_promo_recommendation.gone()
            }

            override fun onAnimationCancel(animator: Animator?) {
                itemView.lottie_button_apply_promo_recommendation.gone()
            }

            override fun onAnimationStart(animator: Animator?) {}
        })
        if (!itemView.lottie_button_apply_promo_recommendation.isAnimating) {
            itemView.lottie_button_apply_promo_recommendation.playAnimation()
        }

    }

}