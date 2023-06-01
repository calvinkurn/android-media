package com.tokopedia.scp_rewards.widget.medalHeader

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards.common.utils.grayscale
import com.tokopedia.scp_rewards.common.utils.hide
import com.tokopedia.scp_rewards.common.utils.loadLottieFromUrl
import com.tokopedia.scp_rewards.databinding.WidgetMedalHeaderBinding

class MedalHeaderView(private val context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding = WidgetMedalHeaderBinding.inflate(LayoutInflater.from(context), this)

    fun bindData(data: MedalHeader) {
        with(binding) {
            if (data.isGrayScale) {
                ivMedalIcon.visible()
                ivMedalIcon.grayscale()
                lottieViewSparks.hide()
                lottieView.hide()
                ivMedalIcon.setImageUrl(data.medalIconUrl ?: "")

            } else {
                ivMedalIcon.hide()
                lottieViewSparks.visible()
                lottieView.visible()
                loadSparks(data.lottieSparklesUrl)
                lottieView.loadLottie(data)
            }
            loadBackground(data.background, data.backgroundColor)
            binding.ivBadgeBase.setImageUrl(data.podiumUrl ?: "")
        }
    }

    private fun WidgetMedalHeaderBinding.loadSparks(lottieSparklesUrl: String?) {
        lottieViewSparks.loadLottieFromUrl(
            url = lottieSparklesUrl,
            autoPlay = true
        )
    }

    private fun WidgetMedalHeaderBinding.loadBackground(
        backgroundUrl: String?,
        backgroundColor: String?
    ) {
        ivBackground.setImageUrl(backgroundUrl.orEmpty())
        binding.apply { setBackgroundColor(Color.parseColor(backgroundColor ?: "#000000")) }
    }
}
