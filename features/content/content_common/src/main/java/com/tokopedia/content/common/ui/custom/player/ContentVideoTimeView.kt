package com.tokopedia.content.common.ui.custom.player

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.content.common.databinding.ViewContentVideoTimeBinding
import java.util.concurrent.TimeUnit
import com.tokopedia.content.common.R as contentcommonR

/**
 * Created by kenny.hadisaputra on 04/04/23
 */
class ContentVideoTimeView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewContentVideoTimeBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    init {
        gravity = Gravity.CENTER
    }

    fun setCurrentPosition(position: Long) {
        binding.tvCurrentPos.text = position.millisToFormattedVideoDuration()
    }

    fun setTotalDuration(duration: Long) {
        binding.tvTotalDuration.text = duration.millisToFormattedVideoDuration()
    }

    private fun Long.millisToFormattedVideoDuration(): String {
        val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(this)
        val seconds = totalSeconds % 60 + if (this % 1000 >= 500) 1 else 0
        val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
        val hours = TimeUnit.SECONDS.toHours(totalSeconds)
        return if (hours <= 0) String.format(context.getString(contentcommonR.string.content_video_time_less_than_hour), minutes, seconds)
        else String.format(context.getString(contentcommonR.string.content_video_time_hours), hours, minutes, seconds)
    }
}
