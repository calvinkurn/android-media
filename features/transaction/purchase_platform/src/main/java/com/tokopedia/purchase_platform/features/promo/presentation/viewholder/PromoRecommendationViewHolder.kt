package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.animation.Animator
import android.view.View
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoRecommendationUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class PromoRecommendationViewHolder(private val view: View,
                                    private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoRecommendationUiModel>(view) {

    private val lottieButtonApplyPromoRecommendation by lazy {
        view.findViewById<LottieAnimationView>(R.id.lottie_button_apply_promo_recommendation)
    }
    private val buttonApplyPromoRecommendation by lazy {
        view.findViewById<UnifyButton>(R.id.button_apply_promo_recommendation)
    }
    private val imageCheckPromoRecommendation by lazy {
        view.findViewById<ImageView>(R.id.image_check_promo_recommendation)
    }
    private val labelPromoRecommendationTitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_recommendation_title)
    }
    private val labelPromoRecommendationSubTitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_recommendation_sub_title)
    }

    companion object {
        val LAYOUT = R.layout.item_promo_recommendation
    }

    override fun bind(element: PromoRecommendationUiModel) {
        if (element.uiState.isButtonSelectEnabled) {
            lottieButtonApplyPromoRecommendation.progress = 0f
            lottieButtonApplyPromoRecommendation.show()
            buttonApplyPromoRecommendation.setOnClickListener {
                buttonApplyPromoRecommendation.isLoading = true
                playAnimation()
                listener.onClickApplyRecommendedPromo()
            }
            imageCheckPromoRecommendation.gone()
            buttonApplyPromoRecommendation.isEnabled = true
            buttonApplyPromoRecommendation.text = itemView.context.getString(R.string.label_promo_recommendation_select)
            labelPromoRecommendationTitle.text = String.format(itemView.context.getString(R.string.promo_checkout_label_promo_recommendation_title, element.uiData.promoCount))
        } else {
            buttonApplyPromoRecommendation.isLoading = false
            imageCheckPromoRecommendation.show()
            buttonApplyPromoRecommendation.isEnabled = false
            buttonApplyPromoRecommendation.text = itemView.context.getString(R.string.label_promo_recommendation_selected)
            labelPromoRecommendationTitle.text = String.format(itemView.context.getString(R.string.promo_checkout_label_promo_recommendation_title_after_apply, element.uiData.promoCount))
        }
        val totalBenefitFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.uiData.promoTotalBenefit, false)
        labelPromoRecommendationSubTitle.text = String.format(itemView.context.getString(R.string.promo_checkout_label_recommendation_benefit, totalBenefitFormatted))
    }

    private fun playAnimation() {
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