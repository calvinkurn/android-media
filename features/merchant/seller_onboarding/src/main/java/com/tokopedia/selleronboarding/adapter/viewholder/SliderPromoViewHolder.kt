package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderPromoUiModel
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_promo_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderPromoViewHolder(itemView: View) : AbstractViewHolder<SobSliderPromoUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_promo_view_holder
    }

    override fun bind(element: SobSliderPromoUiModel) {
        with(itemView) {
            tvSobSliderPromoTitle.viewTreeObserver.addOnDrawListener {
                tvSobSliderPromoTitle.alpha = itemView.viewObserver.alpha
                tvSobSliderPromoTitle.translationY = itemView.viewObserver.translationY
            }
            imgSobPromo.run {
                loadImage(R.drawable.onboarding_04)
                viewTreeObserver.addOnDrawListener {
                    scaleX = itemView.viewObserver.scaleX
                    scaleY = itemView.viewObserver.scaleY
                    alpha = itemView.viewObserver.alpha
                }
            }
        }
    }
}