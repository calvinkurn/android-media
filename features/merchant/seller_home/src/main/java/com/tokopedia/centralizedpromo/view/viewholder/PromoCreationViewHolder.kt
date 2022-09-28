package com.tokopedia.centralizedpromo.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.R.layout.centralized_promo_item_promo_creation
import com.tokopedia.sellerhome.databinding.CentralizedPromoItemPromoCreationBinding

class PromoCreationViewHolder(view: View?, val impressionListener: (String) -> Unit) :
    AbstractViewHolder<PromoCreationUiModel>(view) {

    var onClickItemPromo: ((PromoCreationUiModel) -> Unit)? = null

    companion object {
        val RES_LAYOUT = centralized_promo_item_promo_creation
    }

    private val binding by lazy {
        CentralizedPromoItemPromoCreationBinding.bind(itemView)
    }

    override fun bind(element: PromoCreationUiModel) {
        binding.run {
            ivRecommendedPromo.loadImage(element.icon)
            tvRecommendedPromoTitle.text = element.title
            if (element.notAvailableText.isEmpty()) {
                tvRecommendedPromoDescription.text = element.description
            } else {
                tvRecommendedPromoDescription.text = itemView.context.getString(
                    R.string.centralized_promo_description,
                    element.notAvailableText,
                    element.description
                ).parseAsHtml()
            }
            if (element.titleSuffix.equals(getTextBadgeNew(), ignoreCase = true)) {
                tvBadgeNew.show()
            } else {
                tvBadgeNew.gone()
            }

            root.setOnClickListener {
                openApplink(element)
            }

            root.addOnImpressionListener(element.impressHolder) {
                impressionListener.invoke(element.title)
            }
        }
    }

    private fun openApplink(element: PromoCreationUiModel) {
        onClickItemPromo?.invoke(element)
    }

    private fun getTextBadgeNew() =
        itemView.context?.getString(R.string.text_badge_new) ?: String.EMPTY

}