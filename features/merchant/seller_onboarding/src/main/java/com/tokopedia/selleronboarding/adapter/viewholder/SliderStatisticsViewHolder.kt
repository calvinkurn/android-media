package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.databinding.SobSliderStatisticsViewHolderBinding
import com.tokopedia.selleronboarding.model.SobSliderStatisticsUiModel
import com.tokopedia.selleronboarding.utils.OnboardingConst

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderStatisticsViewHolder(
    itemView: View
) : AbstractViewHolder<SobSliderStatisticsUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_statistics_view_holder
    }

    private val binding by lazy {
        SobSliderStatisticsViewHolderBinding.bind(itemView)
    }
    private val animObserver by lazy {
        itemView.findViewById<View>(R.id.viewObserver)
    }

    override fun bind(element: SobSliderStatisticsUiModel) {
        with(binding) {
            imgSobStatistic.loadImage(OnboardingConst.ImageUrl.IMG_STATISTIC) {
                setPlaceHolder(R.drawable.img_sob_statistic)
            }

            setupAnimation()
        }
    }

    private fun setupAnimation() {
        with(binding) {
            root.viewTreeObserver.addOnPreDrawListener {
                tvSobSliderStatisticTitle.alpha = animObserver.alpha
                tvSobSliderStatisticTitle.translationY = animObserver.translationY

                imgSobStatistic.scaleX = animObserver.scaleX
                imgSobStatistic.scaleY = animObserver.scaleY
                imgSobStatistic.alpha = animObserver.alpha

                return@addOnPreDrawListener true
            }
        }
    }
}