package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderStatisticsUiModel
import com.tokopedia.selleronboarding.utils.IMG_DEVICE_SCREEN_PERCENT
import com.tokopedia.selleronboarding.utils.SobImageSliderUrl
import com.tokopedia.selleronboarding.utils.setupMarginTitleSob
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_statistics_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderStatisticsViewHolder(itemView: View) : AbstractViewHolder<SobSliderStatisticsUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_statistics_view_holder
    }

    override fun bind(element: SobSliderStatisticsUiModel) {
        with(itemView) {
            tvSobSliderStatisticTitle?.viewTreeObserver?.addOnDrawListener {
                tvSobSliderStatisticTitle.alpha = itemView.viewObserver.alpha
                tvSobSliderStatisticTitle.translationY = itemView.viewObserver.translationY
            }
            imgSobStatistic?.run {
                viewTreeObserver.addOnDrawListener {
                    scaleX = itemView.viewObserver.scaleX
                    scaleY = itemView.viewObserver.scaleY
                    alpha = itemView.viewObserver.alpha
                }
            }
            imgSobStatistic?.loadImage(SobImageSliderUrl.IMG_STATISTIC) {
                setPlaceHolder(R.drawable.img_sob_statistic)
            }
            setupMarginTitleSob { setMarginStatisticTitle() }
        }
    }

    private fun setMarginStatisticTitle() {
        with(itemView) {
            val tvSobCurrentView = tvSobSliderStatisticTitle?.layoutParams as? ConstraintLayout.LayoutParams
            tvSobCurrentView?.topToTop = ConstraintSet.PARENT_ID
            tvSobCurrentView?.topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            tvSobSliderStatisticTitle?.layoutParams = tvSobCurrentView

            val imgSobCurrentView = imgSobStatistic?.layoutParams as? ConstraintLayout.LayoutParams
            imgSobCurrentView?.matchConstraintPercentHeight = IMG_DEVICE_SCREEN_PERCENT
            imgSobCurrentView?.topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
            imgSobStatistic?.layoutParams = imgSobCurrentView
        }
    }
}