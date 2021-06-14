package com.tokopedia.topads.auto.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar

/**
 * Author errysuprayogi on 13,May,2019
 */

data class Range(val min: Float, val max: Float, private val defaultIncrement: Int) {
    val increment = if ((max - min) < defaultIncrement) 1 else defaultIncrement
}


internal fun Range.toSeekbarMaximum(): Float = (max - min) / increment


class RangeSeekBar: SeekBar, SeekBar.OnSeekBarChangeListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    var range: Range = Range(0.0f, 100.0f, 1)
        set(value) {
            field = value
            max = value.toSeekbarMaximum().toInt()
        }

    var value: Float = 0.0f
        get() = range.min + progress * range.increment
        set(value) {
            progress = ((value - range.min) / range.increment).toInt()
            field = value
        }

    var onSeekBarChangeListenerDelegate: OnSeekBarChangeListener? = this

    override fun setOnSeekBarChangeListener(l: OnSeekBarChangeListener?) {
        onSeekBarChangeListenerDelegate = l
        super.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onSeekBarChangeListenerDelegate?.onProgressChanged(seekBar, value.toInt(), fromUser)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        onSeekBarChangeListenerDelegate?.onStartTrackingTouch(seekBar)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        onSeekBarChangeListenerDelegate?.onStopTrackingTouch(seekBar)
    }
}