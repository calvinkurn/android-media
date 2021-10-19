package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.databinding.SobSliderPromoViewHolderBinding
import com.tokopedia.selleronboarding.model.SobSliderPromoUiModel
import com.tokopedia.selleronboarding.utils.OnboardingConst

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderPromoViewHolder(itemView: View) : AbstractViewHolder<SobSliderPromoUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_promo_view_holder
    }

    private val binding by lazy {
        SobSliderPromoViewHolderBinding.bind(itemView)
    }
    private val animObserver by lazy {
        itemView.findViewById<View>(R.id.viewObserver)
    }

    override fun bind(element: SobSliderPromoUiModel) {
        with(binding) {
            root.viewTreeObserver.addOnPreDrawListener {
                tvSobSliderPromoTitle.alpha = animObserver.alpha
                tvSobSliderPromoTitle.translationY = animObserver.translationY

                imgSobPromo.scaleX = animObserver.scaleX
                imgSobPromo.scaleY = animObserver.scaleY
                imgSobPromo.alpha = animObserver.alpha

                return@addOnPreDrawListener true
            }

            imgSobPromo.loadImage(OnboardingConst.ImageUrl.IMG_ADS_PROMOTION) {
                setPlaceHolder(R.drawable.img_sob_ads_promotion)
            }
        }
    }
}