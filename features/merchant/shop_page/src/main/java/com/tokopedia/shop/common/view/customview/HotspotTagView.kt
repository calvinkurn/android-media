package com.tokopedia.shop.common.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.shop.common.view.model.ImageHotspotData
import com.tokopedia.shop.databinding.HotspotTagViewBinding
import com.tokopedia.unifyprinciples.UnifyMotion

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
    }

    interface Listener{
        fun onHotspotTagClicked(
            hotspotData: ImageHotspotData.HotspotData,
            view: View,
        )
    }

    private var viewBinding : HotspotTagViewBinding

    init {
        viewBinding = HotspotTagViewBinding.inflate(LayoutInflater.from(context), this)
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

    fun showWithAnimation() {
        animate().scaleX(SCALE_X_SHOW).scaleY(SCALE_Y_SHOW)
            .alpha(ALPHA_SHOW)
            .setInterpolator(UnifyMotion.EASE_OUT)
            .duration = UnifyMotion.T3
    }

    fun hideWithAnimation() {
        animate().scaleX(SCALE_X_HIDE).scaleY(SCALE_Y_HIDE)
            .alpha(ALPHA_HIDE)
            .setInterpolator(UnifyMotion.EASE_IN_OUT)
            .duration = UnifyMotion.T3
    }

}
