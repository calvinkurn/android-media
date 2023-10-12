package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetCreateReviewTickerBinding
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTickerUiState
import com.tokopedia.unifycomponents.ticker.TickerCallback

class CreateReviewTicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewTickerBinding>(context, attrs, defStyleAttr) {

    private val trackingHandler = TrackingHandler()
    private var createReviewTickerListener: Listener? = null

    override val binding = WidgetCreateReviewTickerBinding.inflate(LayoutInflater.from(context), this, true)

    private fun WidgetCreateReviewTickerBinding.showTicker(uiState: CreateReviewTickerUiState.Showing) {
        with(tickerCreateReview) {
            setHtmlDescription(uiState.ticker.subtitle)
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    createReviewTickerListener?.onTickerDescriptionClicked()
                    trackingHandler.trackClickTicker(uiState)
                }

                override fun onDismiss() {}
            })
            show()
        }
        trackingHandler.trackViewTicker(uiState)
    }

    fun updateUi(uiState: CreateReviewTickerUiState) {
        when(uiState) {
            is CreateReviewTickerUiState.Showing -> {
                binding.showTicker(uiState)
                animateShow()
            }
            else -> {
                animateHide()
            }
        }
    }

    fun setListener(newCreateReviewTickerListener: Listener) {
        createReviewTickerListener = newCreateReviewTickerListener
    }

    private inner class TrackingHandler {
        fun trackViewTicker(uiState: CreateReviewTickerUiState.Showing) {
            uiState.trackerData.ovoDomain.productrevIncentiveOvo?.let {
                CreateReviewTracking.eventViewIncentivesTicker(
                    it.subtitle,
                    uiState.trackerData.reputationId,
                    uiState.trackerData.orderId,
                    uiState.trackerData.productId,
                    uiState.trackerData.userId
                )
            }
        }

        fun trackClickTicker(uiState: CreateReviewTickerUiState.Showing) {
            uiState.trackerData.ovoDomain.productrevIncentiveOvo?.let {
                if (uiState.trackerData.hasIncentive) {
                    CreateReviewTracking.eventClickIncentivesTicker(
                        it.subtitle,
                        uiState.trackerData.reputationId,
                        uiState.trackerData.orderId,
                        uiState.trackerData.productId,
                        uiState.trackerData.userId
                    )
                } else if (uiState.trackerData.hasOngoingChallenge) {
                    CreateReviewTracking.eventClickOngoingChallengeTicker(
                        uiState.trackerData.reputationId,
                        uiState.trackerData.orderId,
                        uiState.trackerData.productId,
                        uiState.trackerData.userId
                    )
                }
            }
        }
    }

    interface Listener {
        fun onTickerDescriptionClicked()
    }
}
