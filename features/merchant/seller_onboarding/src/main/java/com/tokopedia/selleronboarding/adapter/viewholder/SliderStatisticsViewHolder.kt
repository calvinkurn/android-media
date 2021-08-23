package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderStatisticsUiModel
import com.tokopedia.selleronboarding.utils.SobImageSliderUrl
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_statistics_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderStatisticsViewHolder(itemView: View) :
    AbstractViewHolder<SobSliderStatisticsUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_statistics_view_holder
    }

    private val animationObserver by lazy {
        itemView.findViewById<View>(R.id.viewObserver)
    }

    override fun bind(element: SobSliderStatisticsUiModel) {
        with(itemView) {
            imgSobStatistic?.loadImage(SobImageSliderUrl.IMG_STATISTIC) {
                setPlaceHolder(R.drawable.img_sob_statistic)
            }

            setupAnimation()
        }
    }

    private fun setupAnimation() {
        with(itemView) {
            viewTreeObserver.addOnPreDrawListener {
                tvSobSliderStatisticTitle?.alpha = animationObserver.alpha
                tvSobSliderStatisticTitle?.translationY = animationObserver.translationY

                imgSobStatistic?.scaleX = animationObserver.scaleX
                imgSobStatistic?.scaleY = animationObserver.scaleY
                imgSobStatistic?.alpha = animationObserver.alpha

                return@addOnPreDrawListener true
            }
        }
    }
}