package com.tokopedia.centralized_promo.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralized_promo.view.model.RecommendedPromotionUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R.layout.sah_item_centralized_promo_recommendation
import kotlinx.android.synthetic.main.sah_item_centralized_promo_recommendation.view.*

class RecommendedPromotionViewHolder(view: View?) : AbstractViewHolder<RecommendedPromotionUiModel>(view) {

    companion object {
        val RES_LAYOUT = sah_item_centralized_promo_recommendation
    }

    override fun bind(element: RecommendedPromotionUiModel) {
        with(itemView) {
            ImageHandler.loadImageWithId(ivRecommendedPromo, element.imageDrawable)
            tvRecommendedPromoTitle.text = element.title
            tvRecommendedPromoDescription.text = element.description

            if (element.extra.isNotBlank()) {
                tvRecommendedPromoExtra.text = element.extra
                tvRecommendedPromoExtra.show()
            } else {
                tvRecommendedPromoExtra.text = ""
                icRecommendedPromoExtra.gone()
            }

            setOnClickListener { openApplink(element.applink) }
        }
    }

    private fun openApplink(url: String) {
        with(itemView) {
            RouteManager.route(context, url)
        }
    }
}