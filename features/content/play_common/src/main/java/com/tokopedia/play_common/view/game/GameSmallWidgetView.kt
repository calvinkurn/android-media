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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.timerEngagementTools.pause()
        listener = null
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