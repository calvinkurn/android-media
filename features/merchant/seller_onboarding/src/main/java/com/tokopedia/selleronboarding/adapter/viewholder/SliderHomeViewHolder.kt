package com.tokopedia.selleronboarding.adapter.viewholder

import android.content.res.Configuration
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderHomeUiModel
import com.tokopedia.selleronboarding.utils.IMG_DEVICE_SCREEN_PERCENT
import com.tokopedia.selleronboarding.utils.setupMarginTitleSob
import kotlinx.android.synthetic.main.partial_view_holder_observer.view.*
import kotlinx.android.synthetic.main.sob_slider_home_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderHomeViewHolder(itemView: View) : AbstractViewHolder<SobSliderHomeUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_home_view_holder
        private const val ANIM_POP_IN_DURATION = 500L
    }

    override fun bind(element: SobSliderHomeUiModel) {
        with(itemView) {

            tvSobSliderHome.viewTreeObserver.addOnDrawListener {
                tvSobSliderHome.alpha = itemView.viewObserver.alpha
                tvSobSliderHome.translationY = itemView.viewObserver.translationY
            }
            addOnImpressionListener(element.impressionHolder) {
                itemView.imgSobHome.scaleX = itemView.viewObserver.scaleX
                itemView.imgSobHome.scaleY = itemView.viewObserver.scaleY
                runOneTimePopInAnimation()
            }
            setupMarginTitleSob { setMarginTitleSobHome() }
        }
    }

    private fun setupImageViewObserver() {
        with(itemView.imgSobHome) {
            viewTreeObserver.addOnDrawListener {
                scaleX = itemView.viewObserver.scaleX
                scaleY = itemView.viewObserver.scaleY
                alpha = itemView.viewObserver.alpha
            }
        }
    }

    private fun setMarginTitleSobHome() {
        with(itemView) {
            val tvSobCurrentView = tvSobSliderHome?.layoutParams as? ConstraintLayout.LayoutParams
            tvSobCurrentView?.topToTop = ConstraintSet.PARENT_ID
            tvSobCurrentView?.topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            tvSobSliderHome?.layoutParams = tvSobCurrentView

            val imgSobCurrentView = imgSobHome?.layoutParams as? ConstraintLayout.LayoutParams
            imgSobCurrentView?.matchConstraintPercentHeight = IMG_DEVICE_SCREEN_PERCENT
            imgSobCurrentView?.topMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
            imgSobHome?.layoutParams = imgSobCurrentView
        }
    }

    private fun runOneTimePopInAnimation() {
        with(itemView) {
            val startScale = 0f
            val endScale = 1f
            val pivotValue = 0.5f
            val animation = ScaleAnimation(
                    startScale, endScale,
                    startScale, endScale,
                    Animation.RELATIVE_TO_SELF, pivotValue,
                    Animation.RELATIVE_TO_SELF, pivotValue
            )
            animation.fillAfter = true
            animation.duration = ANIM_POP_IN_DURATION
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    setupImageViewObserver()
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
            imgSobHome.startAnimation(animation)
        }
    }
}