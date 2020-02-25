package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
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
            itemView.image_check_promo_recommendation.gone()
            itemView.button_apply_promo_recommendation.isEnabled = true
            itemView.button_apply_promo_recommendation.text = "Pilih"
        } else {
            itemView.image_check_promo_recommendation.show()
            itemView.button_apply_promo_recommendation.isEnabled = false
            itemView.button_apply_promo_recommendation.text = "Dipilih"
        }
        itemView.label_promo_recommendation_title.text = element.uiData.title
        itemView.label_promo_recommendation_sub_title.text = element.uiData.subTitle
    }

}