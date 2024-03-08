package com.tokopedia.cart.view.customview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.cart.databinding.LayoutCartCampaignProductLabelBinding
import com.tokopedia.cart.view.uimodel.HexColor
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.cart.R as cartR

class CartCampaignProductLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val LABEL_IMAGE_MAX_WIDTH_DP = 128
        private const val LABEL_IMAGE_MAX_HEIGHT_DP = 12
        private const val LABEL_CORNER_RADIUS_DP = 4
        private const val LABEL_STROKE_WIDTH_DP = 1

        private const val COUNTDOWN_TIMER_INTERVAL_MS = 1_000L
        private const val COUNTDOWN_TIMER_DEFAULT_TEXT = "00 : 00 : 00"
    }

    private val binding: LayoutCartCampaignProductLabelBinding =
        LayoutCartCampaignProductLabelBinding
            .inflate(LayoutInflater.from(context), this, true)

    private var timer: CountDownTimer? = null

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearTimer()
    }

    fun showImageLabel(
        logoUrl: String,
        backgroundStartColor: HexColor,
        backgroundEndColor: HexColor
    ) {
        if (logoUrl.isBlank() || backgroundStartColor.hexCode.isBlank() || backgroundEndColor.hexCode.isBlank()) {
            binding.container.gone()
            return
        }

        with(binding) {
            iuCampaignIcon.gone()
            iuCampaignLogo.loadImage(
                url = logoUrl,
                properties = {
                    overrideSize(
                        Resize(
                            context.dpToPx(LABEL_IMAGE_MAX_WIDTH_DP).toInt(),
                            context.dpToPx(LABEL_IMAGE_MAX_HEIGHT_DP).toInt()
                        )
                    )
                    fitCenter()
                }
            )
            iuCampaignLogo.visible()
            tpgCampaignLabel.gone()
            if (backgroundStartColor.hexCode.isNotBlank() && backgroundEndColor.hexCode.isNotBlank()) {
                val tickerBackgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    orientation = GradientDrawable.Orientation.LEFT_RIGHT
                    val startColor = Color.parseColor(backgroundStartColor.hexCode)
                    val endColor = Color.parseColor(backgroundEndColor.hexCode)
                    colors = intArrayOf(startColor, endColor)
                    setCornerRadius(context.dpToPx(LABEL_CORNER_RADIUS_DP))
                }
                clProductLabel.background = tickerBackgroundDrawable
            }
            clProductLabel.visible()
            tpgProductLabelCountdown.gone()
            container.visible()
        }
    }

    fun showTextLabel(
        text: String,
        textColor: HexColor,
        backgroundStartColor: HexColor,
        backgroundEndColor: HexColor
    ) {
        if (text.isBlank() || textColor.hexCode.isBlank() || backgroundStartColor.hexCode.isBlank() || backgroundEndColor.hexCode.isBlank()) {
            binding.container.gone()
            return
        }

        with(binding) {
            iuCampaignIcon.gone()
            iuCampaignLogo.gone()
            tpgCampaignLabel.text = text
            if (textColor.hexCode.isNotBlank()) {
                tpgCampaignLabel.setTextColor(Color.parseColor(textColor.hexCode))
            }
            tpgCampaignLabel.visible()
            if (backgroundStartColor.hexCode.isNotBlank() && backgroundEndColor.hexCode.isNotBlank()) {
                val tickerBackgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    orientation = GradientDrawable.Orientation.LEFT_RIGHT
                    val startColor = Color.parseColor(backgroundStartColor.hexCode)
                    val endColor = Color.parseColor(backgroundEndColor.hexCode)
                    colors = intArrayOf(startColor, endColor)
                    setCornerRadius(context.dpToPx(LABEL_CORNER_RADIUS_DP))
                }
                clProductLabel.background = tickerBackgroundDrawable
            }
            clProductLabel.visible()
            tpgProductLabelCountdown.gone()
            container.visible()
        }
    }

    fun showTimedLabel(
        remainingTimeMillis: Long = 0L,
        alwaysShowTimer: Boolean = false,
        iconUrl: String,
        backgroundColor: HexColor
    ) {
        if (iconUrl.isBlank() || backgroundColor.hexCode.isBlank()) {
            binding.container.gone()
            return
        }

        with(binding) {
            if (iconUrl.isNotBlank()) {
                iuCampaignIcon.loadImage(url = iconUrl)
                iuCampaignIcon.visible()
            } else {
                iuCampaignIcon.gone()
            }
            iuCampaignLogo.gone()
            tpgCampaignLabel.gone()
            if (backgroundColor.hexCode.isNotBlank()) {
                val tickerBackgroundDrawable = GradientDrawable().apply {
                    orientation = GradientDrawable.Orientation.LEFT_RIGHT
                    val color = Color.parseColor(backgroundColor.hexCode)
                    colors = intArrayOf(color, color)
                    val cornerRadius = context.dpToPx(LABEL_CORNER_RADIUS_DP)
                    shape = GradientDrawable.RECTANGLE
                    cornerRadii = floatArrayOf(
                        cornerRadius,
                        cornerRadius,
                        0f,
                        0f,
                        0f,
                        0f,
                        cornerRadius,
                        cornerRadius
                    )
                }
                clProductLabel.background = tickerBackgroundDrawable
            }
            clProductLabel.visible()
            if (backgroundColor.hexCode.isNotBlank()) {
                val countdownBackgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    val cornerRadius = context.dpToPx(LABEL_CORNER_RADIUS_DP)
                    setCornerRadius(cornerRadius)
                    setStroke(
                        context.dpToPx(LABEL_STROKE_WIDTH_DP).toInt(),
                        Color.parseColor(backgroundColor.hexCode)
                    )
                }
                container.background = countdownBackgroundDrawable
            }
            timer?.cancel()
            if (alwaysShowTimer) {
                timer = object : CountDownTimer(remainingTimeMillis, COUNTDOWN_TIMER_INTERVAL_MS) {
                    override fun onTick(millisUntilFinished: Long) {
                        val seconds = (millisUntilFinished / 1000) % 60
                        val minutes = (millisUntilFinished / (1000 * 60) % 60)
                        val hours = (millisUntilFinished / (1000 * 60 * 60))
                        val days = (millisUntilFinished / (1000 * 60 * 60 * 24))

                        val hourText = "${if (hours < 10) 0.toString() else ""}$hours"
                        val minuteText = "${if (minutes < 10) 0.toString() else ""}$minutes"
                        val secondText = "${if (seconds < 10) 0.toString() else ""}$seconds"

                        if (days.isMoreThanZero()) {
                            tpgProductLabelCountdown.text =
                                context.getString(
                                    cartR.string.label_cart_countdown_timer_days_format,
                                    days.toInt().toString()
                                )
                        } else {
                            tpgProductLabelCountdown.text =
                                context.getString(
                                    cartR.string.label_cart_countdown_timer_format,
                                    hourText,
                                    minuteText,
                                    secondText
                                )
                        }
                    }

                    override fun onFinish() {
                        tpgProductLabelCountdown.text = COUNTDOWN_TIMER_DEFAULT_TEXT
                    }
                }
                timer?.start()
            } else {
                tpgProductLabelCountdown.text = COUNTDOWN_TIMER_DEFAULT_TEXT
            }
            tpgProductLabelCountdown.visible()
            iuCampaignLogo.gone()
            container.visible()
        }
    }

    fun hideTicker() {
        binding.container.gone()
    }

    private fun clearTimer() {
        timer?.cancel()
        timer = null
    }
}
