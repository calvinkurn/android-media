package com.tokopedia.scp_rewards.widget.medalHeader

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards.common.utils.hide
import com.tokopedia.scp_rewards.databinding.WidgetMedalHeaderBinding
import com.tokopedia.scp_rewards_common.R
import com.tokopedia.scp_rewards_common.grayscale
import com.tokopedia.scp_rewards_common.loadImageOrFallback
import com.tokopedia.scp_rewards_common.parseColor

class MedalHeaderView(private val context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding = WidgetMedalHeaderBinding.inflate(LayoutInflater.from(context), this)

    fun bindData(data: MedalHeaderData, onMedalClickAction: (() -> Unit)? = null) {
        with(binding) {
            if (data.isGrayScale) {
                ivMedalIcon.visible()
                ivMedalIcon.grayscale()
                lottieView.hide()
                ivMedalIcon.loadImageOrFallback(data.medalIconUrl, R.drawable.fallback_badge)
            } else {
                ivMedalIcon.hide()
                lottieView.visible()
                lottieView.loadLottie(data, onMedalClickAction)
            }
            loadBackground(data.background, data.backgroundColor)
            ivBadgeBase.loadImageOrFallback(data.baseImageURL)
        }
    }

    private fun WidgetMedalHeaderBinding.loadBackground(
        backgroundUrl: String?,
        backgroundColor: String?
    ) {
        ivBackground.setImageUrl(backgroundUrl.orEmpty())
        binding.apply { setBackgroundColor(parseColor(backgroundColor) ?: Color.BLACK) }
    }
}
