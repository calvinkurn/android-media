package com.tokopedia.tokofood.feature.ordertracking.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.databinding.ItemOrderTrackingStatusInfoWidgetBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusUiModel

class OrderTrackingStatusInfoWidget : ConstraintLayout {

    private var binding: ItemOrderTrackingStatusInfoWidgetBinding? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        binding = ItemOrderTrackingStatusInfoWidgetBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    private fun setupLottie(animationUrl: String) {
        val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(context, animationUrl)

        lottieCompositionLottieTask.addListener { result ->
            binding?.lottieOrderTrackingStatus?.run {
                setComposition(result)
                show()
                playAnimation()
                repeatCount = LottieDrawable.INFINITE
            }
        }
    }

    fun setOrderTrackingStatusTitle(orderTrackingStatusUiModel: OrderTrackingStatusUiModel) {
        binding?.tvOrderTrackingStatusTitle?.text = orderTrackingStatusUiModel.title
    }

    fun setOrderTrackingStatusDesc(orderTrackingStatusUiModel: OrderTrackingStatusUiModel) {
        binding?.tvOrderTrackingStatusDesc?.text = orderTrackingStatusUiModel.desc
    }
}