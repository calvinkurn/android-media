package com.tokopedia.product.detail.view.fragment.delegate

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.kotlin.extensions.view.getLocationOnScreen
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.getStatusBarHeight
import com.tokopedia.product.detail.databinding.ProductDetailFragmentBinding
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.viewholder.promo_price.ui.AnimatedImageWithAnchor
import com.tokopedia.product.detail.view.viewmodel.product_detail.ProductDetailViewModel
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext
import com.tokopedia.unifycomponents.toPx
import kotlin.math.roundToInt

class AtcAnimationManager(
    val mediator: PdpComponentCallbackMediator
) {
    private val context: Context?
        get() = mediator.rootView.context
    private val viewModel: ProductDetailViewModel
        get() = mediator.pdpViewModel

    private var resetLaunchEffect = false

    fun runAtcAnimation(binding: ProductDetailFragmentBinding?) {
        if (context == null || binding == null) return
        val toolbar = binding.pdpNavtoolbar

        viewModel.bitmapImage.value?.let { bitmap ->
            val cartViewMenu = toolbar.getCartIconPosition()

            cartViewMenu?.apply {
                post {
                    renderAnimatedImage(
                        binding = binding,
                        target = this,
                        image = bitmap
                    )
                }
            }
        }
    }

    private fun renderAnimatedImage(
        binding: ProductDetailFragmentBinding,
        target: View,
        image: Bitmap
    ) {
        val composeAnimAtc = binding.composeAnimAtc

        composeAnimAtc.apply {
            post {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

                val initialImageCenterCoordinate = getInitialImageCoordinate()
                val targetViewCenterCoordinate = getTargetCenterPosition(target, 2)
                setContent {
                    resetLaunchEffect = !resetLaunchEffect

                    val x = targetViewCenterCoordinate.x - initialImageCenterCoordinate.x
                    val y = initialImageCenterCoordinate.y - targetViewCenterCoordinate.y

                    AnimatedImageWithAnchor(
                        resetLaunchEffect,
                        x,
                        -y,
                        image
                    )
                }
            }
        }
    }

    private fun getInitialImageCoordinate(): Point {
        val centerX = getScreenWidth() * 0.5
        val centerY = getScreenHeight() * 0.4 + context!!.getStatusBarHeight()
        return Point(centerX.roundToInt(), centerY.roundToInt())
    }

    private fun getTargetCenterPosition(
        viewTarget: View,
        padding: Int
    ): Point {
        val location = viewTarget.getLocationOnScreen()
        val halfViewWidth = viewTarget.width / 2
        val halfViewHeight = viewTarget.height / 2

        val x = location.x + halfViewWidth + padding.toPx()
        val y = location.y + halfViewHeight + padding.toPx()

        return Point(x, y)
    }
}
