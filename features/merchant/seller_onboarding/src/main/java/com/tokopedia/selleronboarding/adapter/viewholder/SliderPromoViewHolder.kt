package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderPromoUiModel
import com.tokopedia.selleronboarding.utils.IMG_DEVICE_SCREEN_PERCENT
import com.tokopedia.selleronboarding.utils.setupMarginTitleSob
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
            tvSobSliderPromoTitle?.viewTreeObserver?.addOnDrawListener {
                tvSobSliderPromoTitle.alpha = itemView.viewObserver.alpha
                tvSobSliderPromoTitle.translationY = itemView.viewObserver.translationY
            }
            imgSobPromo.run {
                viewTreeObserver.addOnDrawListener {
                    scaleX = itemView.viewObserver.scaleX
                    scaleY = itemView.viewObserver.scaleY
                    alpha = itemView.viewObserver.alpha
                }
            }
            setupMarginTitleSob { setMarginPromoTitle() }
        }
    }

    private fun setMarginPromoTitle() {
        with(itemView) {
            val tvSobCurrentView = tvSobSliderPromoTitle?.layoutParams as? ConstraintLayout.LayoutParams
            tvSobCurrentView?.topToTop = ConstraintSet.PARENT_ID
            tvSobCurrentView?.topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            tvSobSliderPromoTitle?.layoutParams = tvSobCurrentView

            val imgSobCurrentView = imgSobPromo?.layoutParams as? ConstraintLayout.LayoutParams
            imgSobCurrentView?.matchConstraintPercentHeight = IMG_DEVICE_SCREEN_PERCENT
            imgSobCurrentView?.topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
            imgSobPromo?.layoutParams = imgSobCurrentView
        }
    }
}