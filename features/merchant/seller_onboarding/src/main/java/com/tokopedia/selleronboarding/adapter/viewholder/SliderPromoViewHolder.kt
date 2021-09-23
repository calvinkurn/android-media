package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderPromoUiModel
import com.tokopedia.selleronboarding.utils.OnboardingConst
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_message_view_holder.view.*
import kotlinx.android.synthetic.main.sob_slider_promo_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderPromoViewHolder(itemView: View) : AbstractViewHolder<SobSliderPromoUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_promo_view_holder
    }

    private val animatorObserver by lazy {
        itemView.findViewById<View>(R.id.viewObserver)
    }

    override fun bind(element: SobSliderPromoUiModel) {
        with(itemView) {
            viewTreeObserver.addOnPreDrawListener {
                tvSobSliderPromoTitle?.alpha = animatorObserver.alpha
                tvSobSliderPromoTitle?.translationY = animatorObserver.translationY

                imgSobPromo?.scaleX = animatorObserver.scaleX
                imgSobPromo?.scaleY = animatorObserver.scaleY
                imgSobPromo?.alpha = animatorObserver.alpha

                return@addOnPreDrawListener true
            }

            imgSobPromo?.loadImage(OnboardingConst.ImageUrl.IMG_ADS_PROMOTION) {
                setPlaceHolder(R.drawable.img_sob_ads_promotion)
            }
        }
    }
}