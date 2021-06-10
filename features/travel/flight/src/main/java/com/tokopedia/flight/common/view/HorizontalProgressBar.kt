package com.tokopedia.flight.common.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.tokopedia.flight.R

/**
 * Created by Furqan on 06/10/2021.
 */
class HorizontalProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var progressBar: ProgressBar? = null

    init {
        val view = inflate(context, R.layout.view_horizontal_progress_bar, this)
        progressBar = view.findViewById<View>(R.id.progressBar) as ProgressBar
    }

    fun setProgress(progress: Int) {
        if (Build.VERSION.SDK_INT >= 24) {
            progressBar!!.setProgress(progress, true)
        } else {
            progressBar!!.progress = progress
        }
    }
}