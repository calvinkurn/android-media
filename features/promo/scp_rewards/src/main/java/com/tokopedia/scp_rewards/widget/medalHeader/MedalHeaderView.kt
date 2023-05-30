package com.tokopedia.scp_rewards.widget.medalHeader

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.scp_rewards.common.utils.loadLottieFromUrl
import com.tokopedia.scp_rewards.databinding.WidgetMedalHeaderBinding
import com.tokopedia.scp_rewards.detail.presentation.ui.IMG_DETAIL_BASE

class MedalHeaderView(private val context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding = WidgetMedalHeaderBinding.inflate(LayoutInflater.from(context), this)

    fun bindData(data: MedalHeader) {
        with(binding) {
            loadSparks(data.lottieSparklesUrl)
            lottieView.loadLottie(data)
            loadBackground(data.background, data.backgroundColor)
            binding.ivBadgeBase.setImageUrl(IMG_DETAIL_BASE)
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
