package com.tokopedia.product.detail.view.fragment.delegate

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.view.View
import android.widget.ImageView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.tokopedia.kotlin.extensions.view.getLocationOnScreen
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.product.detail.databinding.ProductDetailFragmentBinding
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.viewmodel.product_detail.ProductDetailViewModel
import com.tokopedia.product.detail.view.widget.AnimatedImageAnchor
import com.tokopedia.product.detail.view.widget.AnimationData
import com.tokopedia.unifycomponents.toPx

class AtcAnimationManager(
    val mediator: PdpComponentCallbackMediator
) {
    private val context: Context?
        get() = mediator.rootView.context
    private val viewModel: ProductDetailViewModel
        get() = mediator.pdpViewModel

    private var resetLaunchEffect = false

    private var mSourceImageView: ImageView? = null

    fun setSourceView(view: ImageView?) {
        mSourceImageView = view
    }

    fun runAtcAnimation(
        binding: ProductDetailFragmentBinding?,
        shoudShow: Boolean
    ) {
        if (context == null ||
            binding == null ||
            !shoudShow
        ) {
            viewModel.onFinishAnimation()
            return
        }

        if (mSourceImageView == null) {
            viewModel.onFinishAnimation()
            return
        }

        val toolbar = binding.pdpNavtoolbar

        val cartViewMenu = toolbar.getCartIconPosition()

        cartViewMenu?.apply {
            post {
                renderAnimatedImage(
                    binding = binding,
                    target = this,
                    image = mSourceImageView?.drawable!!.toBitmap()
                )
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

                val targetViewCenterCoordinate = getTargetCenterPosition(target, 2)
                setContent {
                    resetLaunchEffect = !resetLaunchEffect

                    AnimatedImageAnchor(
                        AnimationData(
                            resetLaunchEffect = resetLaunchEffect,
                            xTarget = targetViewCenterCoordinate.x,
                            yTarget = targetViewCenterCoordinate.y,
                            image = image,
                            onFinishAnimated = {
                                viewModel.onFinishAnimation()
                            }
                        )
                    )
                }
            }
        }
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
