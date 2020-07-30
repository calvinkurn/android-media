package com.tokopedia.centralizedpromo.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.R.layout.centralized_promo_item_promo_creation
import kotlinx.android.synthetic.main.centralized_promo_item_promo_creation.view.*

class PromoCreationViewHolder(view: View?) : AbstractViewHolder<PromoCreationUiModel>(view) {

    var onFreeShippingImpression: (() -> Unit)? = null
    var onFreeShippingClicked: (() -> Unit)? = null

    companion object {
        val RES_LAYOUT = centralized_promo_item_promo_creation
    }

    override fun bind(element: PromoCreationUiModel) {
        with(itemView) {
            ImageHandler.loadImageWithId(ivRecommendedPromo, element.imageDrawable)
            tvRecommendedPromoTitle.text = element.title
            tvRecommendedPromoDescription.text = element.description

            if (element.extra.isNotBlank()) {
                tvRecommendedPromoExtra.text = element.extra
                tvRecommendedPromoExtra.show()
                tvRecommendedPromoDescription.setPadding(
                        context.resources.getDimension(R.dimen.layout_lvl0).toInt(),
                        context.resources.getDimension(R.dimen.layout_lvl0).toInt(),
                        context.resources.getDimension(R.dimen.layout_lvl0).toInt(),
                        context.resources.getDimension(R.dimen.layout_lvl4).toInt())
            } else {
                tvRecommendedPromoExtra.text = ""
                tvRecommendedPromoDescription.setPadding(
                        context.resources.getDimension(R.dimen.layout_lvl0).toInt(),
                        context.resources.getDimension(R.dimen.layout_lvl0).toInt(),
                        context.resources.getDimension(R.dimen.layout_lvl0).toInt(),
                        context.resources.getDimension(R.dimen.layout_lvl2).toInt())
            }

            addOnImpressionListener(element.impressHolder) {
                if(isFreeShippingPromo(element.title)) {
                    onFreeShippingImpression?.invoke()
                } else {
                    CentralizedPromoTracking.sendImpressionPromoCreation(element.title)
                }
            }

            setOnClickListener {
                openApplink(element.applink, element.title)
            }
        }
    }

    private fun openApplink(url: String, title: String) {
        with(itemView) {
            if (RouteManager.route(context, url)) {
                trackClickPromo(title)
            }
        }
    }

    private fun trackClickPromo(title: String) {
        if (isFreeShippingPromo(title)) {
            onFreeShippingClicked?.invoke()
        } else {
            CentralizedPromoTracking.sendClickPromoCreation(title)
        }
    }

    private fun isFreeShippingPromo(title: String): Boolean {
        return title == getString(R.string.centralized_promo_promo_creation_free_shipping_title)
    }
}