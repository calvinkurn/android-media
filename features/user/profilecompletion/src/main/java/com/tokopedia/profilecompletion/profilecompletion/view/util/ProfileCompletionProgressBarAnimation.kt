package com.tokopedia.profilecompletion.profilecompletion.view.util

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

/**
 * Created by stevenfredian on 6/19/17.
 */
class ProfileCompletionProgressBarAnimation(private val progressBar: ProgressBar) : Animation() {
    private val max: Int = progressBar.max
    private var from = 0f
    private var to = 0f
    fun setValue(from: Int, to: Int) {
	this.from = from * max / 100.toFloat()
	this.to = to * max / 100.toFloat()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
	super.applyTransformation(interpolatedTime, t)
	val value = from + (to - from) * interpolatedTime
	progressBar.progress = value.toInt()
    }

}