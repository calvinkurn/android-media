package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderHomeUiModel
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
            imgSobHome.loadImage(R.drawable.onboarding_01)

            tvSobSliderHome.viewTreeObserver.addOnDrawListener {
                tvSobSliderHome.alpha = itemView.viewObserver.alpha
                tvSobSliderHome.translationY = itemView.viewObserver.translationY
            }

            addOnImpressionListener(element.impressionHolder) {
                itemView.imgSobHome.scaleX = itemView.viewObserver.scaleX
                itemView.imgSobHome.scaleY = itemView.viewObserver.scaleY
                runOneTimePopInAnimation()
            }
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