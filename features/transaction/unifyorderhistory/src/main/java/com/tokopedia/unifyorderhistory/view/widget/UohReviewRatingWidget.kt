package com.tokopedia.unifyorderhistory.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.applink.UriUtil
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRating
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRatingConfig
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.databinding.UohReviewRatingWidgetBinding
import com.tokopedia.unifyorderhistory.view.UohReviewRatingConfig

class UohReviewRatingWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    companion object {
        private const val STAR_SIZE = 19
        private const val STAR_GAP = 8
    }

    private val binding = UohReviewRatingWidgetBinding.inflate(LayoutInflater.from(context), this)
    private val config: MutableState<UohReviewRatingConfig> = mutableStateOf(UohReviewRatingConfig.Hidden)
    private var listener: Listener? = null

    init {
        setupRatingWidget()
    }

    fun updateUi(data: UohListOrder.UohOrders.Order.Metadata.ExtraComponent?, listener: Listener) {
        this.listener = listener
        if (data == null) {
            gone()
        } else {
            updateContent(data)
        }
    }

    private fun setupRatingWidget() {
        binding.composeViewReviewRating.setContent {
            val widgetConfig by remember { derivedStateOf { config.value.getWidgetConfig() } }
            widgetConfig?.let { WidgetReviewAnimatedRating(config = it) }
        }
    }

    private fun updateContent(data: UohListOrder.UohOrders.Order.Metadata.ExtraComponent) {
        updateLabel(data)
        updateRatingWidget(data)
        updateBackground(data.type)
        show()
    }

    private fun updateLabel(data: UohListOrder.UohOrders.Order.Metadata.ExtraComponent) {
        binding.tvReviewRating.text = HtmlLinkHelper(context, data.label).spannedString ?: ""
    }

    private fun updateRatingWidget(data: UohListOrder.UohOrders.Order.Metadata.ExtraComponent) {
        if (shouldShowRatingWidget(data.type)) {
            config.value = UohReviewRatingConfig.Showing(
                widgetConfig = mutableStateOf(createReviewRatingConfig()),
                appLink = data.action.appUrl
            )
        } else {
            config.value = UohReviewRatingConfig.Hidden
        }
    }

    private fun updateBackground(type: String) {
        binding.ivReviewRatingBackground.showWithCondition(shouldShowBackground(type))
    }

    private fun shouldShowRatingWidget(type: String): Boolean {
        return type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS_WITH_STARS ||
            type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_INTERACTIVE_STARS
    }

    private fun shouldShowBackground(type: String): Boolean {
        return type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS_WITH_STARS ||
            type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS
    }

    private fun createReviewRatingConfig() = WidgetReviewAnimatedRatingConfig(
        rating = Int.ZERO,
        starSize = STAR_SIZE.dp,
        spaceInBetween = STAR_GAP.dp,
        skipInitialAnimation = true,
        onStarClicked = ::onStarClicked
    )

    private fun onStarClicked(previousRating: Int, currentRating: Int) {
        if (previousRating != currentRating) {
            config.value.let { config ->
                if (config is UohReviewRatingConfig.Showing) {
                    config.updateRating(currentRating)
                    listener?.onReviewRatingClicked(composeAppLink(currentRating, config.appLink))
                }
            }
        }
    }

    private fun composeAppLink(rating: Int, appLink: String): String {
        return UriUtil.buildUriAppendParam(
            uri = appLink,
            queryParameters = mapOf("rating" to rating.toString())
        )
    }

    interface Listener {
        fun onReviewRatingClicked(appLink: String)
    }
}
