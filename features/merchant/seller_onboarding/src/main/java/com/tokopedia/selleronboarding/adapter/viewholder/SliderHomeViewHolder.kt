package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.databinding.SobSliderHomeViewHolderBinding
import com.tokopedia.selleronboarding.model.SobSliderHomeUiModel
import com.tokopedia.selleronboarding.utils.OnboardingConst

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderHomeViewHolder(itemView: View) : AbstractViewHolder<SobSliderHomeUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_home_view_holder
        private const val ANIM_POP_IN_DURATION = 500L
        private const val START_SCALE = 0f
        private const val END_SCALE = 1f
        private const val PIVOT_VALUE = 0.5f
    }

    private val animationObserver by lazy {
        itemView.findViewById<View>(R.id.viewObserver)
    }

    private val binding by lazy { SobSliderHomeViewHolderBinding.bind(itemView) }

    override fun bind(element: SobSliderHomeUiModel) {
        with(binding) {
            setupAnimation(element)

            imgSobHome.run {
                loadImage(OnboardingConst.ImageUrl.IMG_SOB_HOME) {
                    setPlaceHolder(R.drawable.img_sob_home)
                }
            }
        }
    }

    private fun setupAnimation(element: SobSliderHomeUiModel) {
        with(binding) {
            root.viewTreeObserver.addOnPreDrawListener {
                tvSobSliderHome.alpha = animationObserver.alpha
                tvSobSliderHome.translationY = animationObserver.translationY

                return@addOnPreDrawListener true
            }

            root.addOnImpressionListener(element.impressionHolder) {
                imgSobHome.scaleX = animationObserver.scaleX
                imgSobHome.scaleY = animationObserver.scaleY
                runOneTimePopInAnimation()
            }
        }
    }

    private fun setupImageViewObserver() {
        with(binding.imgSobHome) {
            viewTreeObserver.addOnDrawListener {
                scaleX = animationObserver.scaleX
                scaleY = animationObserver.scaleY
                alpha = animationObserver.alpha
            }
        }
    }

    private fun runOneTimePopInAnimation() {
        with(binding) {
            val animation = ScaleAnimation(
                START_SCALE, END_SCALE,
                START_SCALE, END_SCALE,
                Animation.RELATIVE_TO_SELF, PIVOT_VALUE,
                Animation.RELATIVE_TO_SELF, PIVOT_VALUE
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

            root.post {
                imgSobHome.visible()
                imgSobHome.startAnimation(animation)
            }
        }
    }
}