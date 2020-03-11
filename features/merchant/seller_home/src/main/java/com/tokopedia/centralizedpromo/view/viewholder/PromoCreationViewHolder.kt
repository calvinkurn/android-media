package com.tokopedia.centralizedpromo.view.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R.layout.centralized_promo_item_promo_creation
import kotlinx.android.synthetic.main.centralized_promo_item_promo_creation.view.*

class PromoCreationViewHolder(view: View?) : AbstractViewHolder<PromoCreationUiModel>(view) {

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

    class ItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) > 1) {
                    top = margin
                }
                bottom = margin
            }
        }
    }
}