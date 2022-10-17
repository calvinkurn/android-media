package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.databinding.WidgetCreateReviewProductCardBinding
import com.tokopedia.review.feature.createreputation.model.ProductData
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewProductCardUiState
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateReviewProductCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewProductCardBinding>(context, attrs, defStyleAttr) {

    private val transitionHandler = TransitionHandler()

    override val binding = WidgetCreateReviewProductCardBinding.inflate(LayoutInflater.from(context), this, true)

    private fun showLoading() {
        transitionHandler.transitionToShowLoading()
    }

    private fun WidgetCreateReviewProductCardBinding.showProductCard(
        uiState: CreateReviewProductCardUiState.Showing
    ) {
        transitionHandler.transitionToShowProductCard()
        setupProductCard(uiState.productData)
    }

    private fun WidgetCreateReviewProductCardBinding.setupProductCard(
        productData: ProductData
    ) {
        with(layoutProductCard) {
            reviewFormProductName.run {
                text = productData.productName
                showWithCondition(productData.productName.isNotBlank())
            }
            reviewFormProductVariant.run {
                text = productData.productVariant.variantName
                showWithCondition(productData.productVariant.variantName.isNotBlank())
            }
            reviewFormProductImage.loadImage(productData.productImageURL)
        }
    }

    fun updateUi(uiState: CreateReviewProductCardUiState, continuation: Continuation<Unit>) {
        when(uiState) {
            is CreateReviewProductCardUiState.Loading -> {
                showLoading()
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewProductCardUiState.Showing -> {
                binding.showProductCard(uiState)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
        }
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = ANIMATION_DURATION
                addTarget(binding.layoutProductCard.root)
                addTarget(binding.layoutProductCardLoading.root)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun WidgetCreateReviewProductCardBinding.showLoadingLayout() {
            layoutProductCardLoading.root.show()
        }

        private fun WidgetCreateReviewProductCardBinding.hideLoadingLayout() {
            layoutProductCardLoading.root.gone()
        }

        private fun WidgetCreateReviewProductCardBinding.showProductCardLayout() {
            layoutProductCard.root.show()
        }

        private fun WidgetCreateReviewProductCardBinding.hideProductCardLayout() {
            layoutProductCard.root.gone()
        }

        private fun WidgetCreateReviewProductCardBinding.beginDelayedTransition() {
            TransitionManager.beginDelayedTransition(root, fadeTransition)
        }

        fun transitionToShowProductCard() {
            with(binding) {
                beginDelayedTransition()
                hideLoadingLayout()
                showProductCardLayout()
            }
        }

        fun transitionToShowLoading() {
            with(binding) {
                beginDelayedTransition()
                hideProductCardLayout()
                showLoadingLayout()
            }
        }
    }
}