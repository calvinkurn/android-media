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

    fun setProgress(progress: Int) {
        progress_bar_current.progress = progress.toFloat()
        tv_current_progress.text = "$progress"
    }

    fun setMaxProgress(maxProgress: Int) {
        tv_max_progress.text = "/".plus("$maxProgress")
    }

    fun setProgressTitle(title: String) {
        tv_progress_title.text = title
    }

    fun setProgressColor(state: State) {
        val colors = when (state) {
            State.GREEN -> intArrayOf(ContextCompat.getColor(context, R.color.Green_G400), ContextCompat.getColor(context, R.color.Green_G600))
            State.YELLOW -> intArrayOf(ContextCompat.getColor(context, R.color.Yellow_Y300), ContextCompat.getColor(context, R.color.Yellow_Y400))
            State.RED -> intArrayOf(ContextCompat.getColor(context, R.color.Red_R400), ContextCompat.getColor(context, R.color.Red_R500))
        }

        progress_bar_current.setProgressColor(colors)
        tv_current_progress.setTextColor(colors.last())
    }

    enum class State {
        GREEN,
        YELLOW,
        RED
    }
}

