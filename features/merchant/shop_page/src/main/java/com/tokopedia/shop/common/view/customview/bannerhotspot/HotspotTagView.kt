package com.tokopedia.shop.common.view.customview.bannerhotspot

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.view.model.ImageHotspotData
import com.tokopedia.shop.databinding.HotspotTagViewBinding
import com.tokopedia.unifyprinciples.UnifyMotion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HotspotTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    companion object{
        private const val ALPHA_HIDE = 0f
        private const val SCALE_X_HIDE = 0.75f
        private const val SCALE_Y_HIDE = 0.75f
        private const val ALPHA_SHOW = 1f
        private const val SCALE_X_SHOW = 1f
        private const val SCALE_Y_SHOW = 1f
        private const val INTRO_ANIMATION_START_DELAY = 1000L
        private const val INTRO_ANIMATION_POST_DELAY = 2000L
    }

    interface Listener{
        fun onHotspotTagClicked(
            hotspotData: ImageHotspotData.HotspotData,
            view: View,
        )
    }

    private var viewBinding : HotspotTagViewBinding
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    init {
        viewBinding = HotspotTagViewBinding.inflate(LayoutInflater.from(context), this)
        setDefaultAnimationState()
    }

    private fun setDefaultAnimationState() {
        alpha = ALPHA_HIDE
        scaleX = SCALE_X_HIDE
        scaleY = SCALE_Y_HIDE
        hide()
    }

    fun bindData(
        hotspotData: ImageHotspotData.HotspotData,
        bannerImageWidth: Int,
        bannerImageHeight: Int,
        listener: Listener
    ) {
        this.x = (bannerImageWidth.toFloat() * (hotspotData.x))
        this.y = (bannerImageHeight.toFloat() * (hotspotData.y))
        setOnClickListener {
            listener.onHotspotTagClicked(hotspotData, it)
        }
    }

    fun showWithAnimation(): ViewPropertyAnimator {
        return animate().scaleX(SCALE_X_SHOW).scaleY(SCALE_Y_SHOW)
            .alpha(ALPHA_SHOW)
            .setInterpolator(UnifyMotion.EASE_OUT)
            .setDuration(UnifyMotion.T3)
            .withStartAction { show() }
    }

    fun hideWithAnimation(): ViewPropertyAnimator {
        return animate()
            .scaleX(SCALE_X_HIDE).scaleY(SCALE_Y_HIDE)
            .alpha(ALPHA_HIDE)
            .setInterpolator(UnifyMotion.EASE_OUT)
            .setDuration(UnifyMotion.T3)
            .withEndAction { hide() }
    }

    fun showIntroAnimation() {
        coroutineScope.launch {
            delay(INTRO_ANIMATION_START_DELAY)
            showWithAnimation().withEndAction {
                coroutineScope.launch{
                    delay(INTRO_ANIMATION_POST_DELAY)
                    hideWithAnimation()
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        job.cancelChildren()
        super.onDetachedFromWindow()
    }
}
