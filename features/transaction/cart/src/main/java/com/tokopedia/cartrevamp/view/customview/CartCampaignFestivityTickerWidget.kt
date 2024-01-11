package com.tokopedia.cartrevamp.view.customview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.LayoutCartCampaignFestivityTickerWidgetBinding
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage

class CartCampaignFestivityTickerWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutCartCampaignFestivityTickerWidgetBinding =
        LayoutCartCampaignFestivityTickerWidgetBinding.inflate(LayoutInflater.from(context), this, true)

    private var timer: CountDownTimer? = null
    private var onCountdownTick: ((remainingTimeMs: Long) -> Unit)? = null
    private var onCountdownFinish: (() -> Unit)? = null

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timer = null
    }

    fun showLogoTicker(logoUrl: String) {
        // TODO: Implement real data
        binding.iuCampaignLogo.loadImage(ContextCompat.getDrawable(context, (R.drawable.test_campaign_logo)))
        //binding.iuCampaignLogo.loadImage(logoUrl)
        binding.iuCampaignLogo.visible()
    }

    fun hideLogoTicker() {
        binding.iuCampaignLogo.gone()
    }

    fun showTextTicker(
        iconUrl: String,
        text: String,
        backgroundGradientStartHexColor: String,
        backgroundGradientEndHexColor: String
    ) {
        with(binding) {
            if (iconUrl.isNotBlank()) {
                iuCampaignIcon.loadImage(url = iconUrl)
                iuCampaignIcon.visible()
            } else {
                iuCampaignIcon.gone()
            }
            tpgCampaignLabel.text = text
            val gradientBackgroundDrawable = GradientDrawable().apply {
                orientation = GradientDrawable.Orientation.LEFT_RIGHT
                val startColor = Color.parseColor(backgroundGradientStartHexColor)
                val endColor = Color.parseColor(backgroundGradientEndHexColor)
                colors = intArrayOf(startColor, endColor)
                val cornerRadius = context.dpToPx(4)
                shape = GradientDrawable.RECTANGLE
                cornerRadii = floatArrayOf(0f, 0f, cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius, 0f, 0f)
            }
            clCampaignTicker.background = gradientBackgroundDrawable
        }
    }

    fun hideTextTicker() {
        binding.clCampaignTicker.background = null
        binding.iuCampaignIcon.gone()
        binding.tpgCampaignLabel.gone()
    }

    fun showCountdown(remainingTimeMs: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(1_800_000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60) % 60)
                val hours = (millisUntilFinished / (1000 * 60 * 60))

                val hourText = "${if (hours < 10) 0.toString() else ""}${hours}"
                val minuteText = "${if (minutes < 10) 0.toString() else ""}${minutes}"
                val secondText = "${if (seconds < 10) 0.toString() else ""}${seconds}"

                binding.tpgCampaignCountdown.text = "$hourText : $minuteText : $secondText"

                onCountdownTick?.invoke(millisUntilFinished)
            }

            override fun onFinish() {
                onCountdownFinish?.invoke()
            }
        }
        binding.tpgCampaignCountdown.visible()
        timer?.start()
    }

    fun hideCountdown() {
        binding.tpgCampaignCountdown.gone()
    }
}