package com.tokopedia.sellerhome.view.widget

/**
 * Created by hendry on 2019-06-16.
 */

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.sellerhome.R
import kotlinx.android.synthetic.main.sah_progress_bar_widget.view.*

/**
 * Created by hendry on 2019-06-15.
 * _________________
 * (_________________)==========#
 *
 */
class ShopScorePMWidget : FrameLayout {

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.sah_progress_bar_widget, this)
    }

    fun setProgress(progress: Float) {
        progress_bar_current.progress = progress
        tv_current_progress.text = "${progress.toInt()}"
    }

    fun setProgressColor(progressColors: IntArray) {
        progress_bar_current.setProgressColor(progressColors)
        tv_current_progress.setTextColor(ContextCompat.getColor(context, progressColors.last()))
    }
}

