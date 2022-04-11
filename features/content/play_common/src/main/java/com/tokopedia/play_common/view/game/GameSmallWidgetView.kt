package com.tokopedia.play_common.view.game

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ViewGameInteractiveBinding
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.Calendar

/**
 * @author by astidhiyaa on 04/04/22
 */
class GameSmallWidgetView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = ViewGameInteractiveBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var listener: Listener? = null

    init {
        binding.root.setOnClickListener {
            listener?.onWidgetClicked(this@GameSmallWidgetView)
        }
    }

    var description: String = ""
        set(value) {
            field = value
            binding.tvEngagementDesc.text = value
        }

    private fun setBackground(){
        val drawableValue = when(type){
            PlayGameType.Quiz -> R.drawable.bg_play_quiz_header
            PlayGameType.TapTap -> R.drawable.bg_play_quiz_header
            else -> R.drawable.bg_play_quiz_header
        }
        val drawableType = ContextCompat.getDrawable(context, drawableValue)
        drawableType?.alpha = 178

        binding.root.background = drawableType
    }

    private fun setQuizIcon(){
        val (icon, color) = when(type){
            PlayGameType.Quiz -> Pair(IconUnify.QUIZ, R.color.play_bro_quiz_icon_color)
            PlayGameType.TapTap -> Pair(IconUnify.GIFT, R.color.play_bro_taptap_icon_color)
            else -> Pair(IconUnify.QUIZ, R.color.play_bro_quiz_icon_color)
        }

        val iconType = getIconUnifyDrawable(context = context, iconId = icon, assetColor = color)

        binding.ivEngagementTools.setImageDrawable(iconType)
    }

    private fun setTimerType(){
        val (timerVariant, timerInfo) = when(type){
            PlayGameType.Quiz -> Pair(TimerUnifySingle.VARIANT_MAIN, R.string.play_common_widget_interactive_end)
            PlayGameType.TapTap -> Pair(TimerUnifySingle.VARIANT_GENERAL, R.string.play_common_widget_interactive_start)
            else -> Pair(TimerUnifySingle.VARIANT_MAIN, R.string.play_common_widget_interactive_end)
        }

        binding.timerEngagementTools.timerVariant = timerVariant
        binding.tvEngagementTimerInfo.text = context.getString(timerInfo)
    }

    fun setTimer(duration: Long, onFinished: () -> Unit){
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, duration.toInt())

        binding.timerEngagementTools.apply {
            pause()

            targetDate = calendar
            onFinish = onFinished

            resume()
        }
    }

    /**
     * Setting the variant of the timer based on variant supported by TimerUnifySingle
     * defined in [TimerUnifySingle.Companion]
     *
     * @param variant - the integer variant supported by TimerUnifySingle
     */
    fun setTimerVariant(variant: Int) {
        binding.timerEngagementTools.timerVariant = variant
    }

    fun setContentBackground(drawable: Drawable) {
        binding.flBackground.background = drawable
    }

    fun setIcon(icon: Drawable) {
        binding.ivEngagementTools.setImageDrawable(icon)
    }

    fun setTimerInfo(info: String) {
        binding.tvEngagementTimerInfo.text = info
    }

    fun cancelTimer(){
        binding.timerEngagementTools.timer?.cancel()
    }

    fun setListener(listener: Listener?){
        this.listener = listener
    }

    interface Listener{
        fun onWidgetClicked(view: GameSmallWidgetView)
    }
}