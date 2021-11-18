package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.animation.Animator
import android.view.View
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoRecommendationBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoRecommendationUiModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class PromoRecommendationViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoRecommendationBinding,
                                    private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoRecommendationUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_recommendation
    }

    override fun bind(element: PromoRecommendationUiModel) {
        with(viewBinding) {
            if (element.uiState.isButtonSelectEnabled) {
                lottieButtonApplyPromoRecommendation.progress = 0f
                lottieButtonApplyPromoRecommendation.show()
                buttonApplyPromoRecommendation.setOnClickListener {
                    playAnimation()
                    listener.onClickApplyRecommendedPromo()
                }
                imageCheckPromoRecommendation.gone()
                buttonApplyPromoRecommendation.isEnabled = true
                buttonApplyPromoRecommendation.text = itemView.context.getString(R.string.label_promo_recommendation_select)
                labelPromoRecommendationTitle.text = String.format(itemView.context.getString(R.string.promo_checkout_label_promo_recommendation_title, element.uiData.promoCount))
            } else {
                imageCheckPromoRecommendation.show()
                buttonApplyPromoRecommendation.isEnabled = false
                buttonApplyPromoRecommendation.text = itemView.context.getString(R.string.label_promo_recommendation_selected)
                labelPromoRecommendationTitle.text = String.format(itemView.context.getString(R.string.promo_checkout_label_promo_recommendation_title_after_apply, element.uiData.promoCount))
            }
            val totalBenefitFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.uiData.promoTotalBenefit, false).removeDecimalSuffix()
            labelPromoRecommendationSubTitle.text = String.format(itemView.context.getString(R.string.promo_checkout_label_recommendation_benefit, totalBenefitFormatted))
        }
    }

    private fun playAnimation() {
        with(viewBinding) {
            lottieButtonApplyPromoRecommendation.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animator: Animator?) {}

                override fun onAnimationEnd(animator: Animator?) {
                    lottieButtonApplyPromoRecommendation.gone()
                }

                override fun onAnimationCancel(animator: Animator?) {
                    lottieButtonApplyPromoRecommendation.gone()
                }

                override fun onAnimationStart(animator: Animator?) {}
            })
            if (!lottieButtonApplyPromoRecommendation.isAnimating) {
                lottieButtonApplyPromoRecommendation.playAnimation()
            }
        }
    }

}