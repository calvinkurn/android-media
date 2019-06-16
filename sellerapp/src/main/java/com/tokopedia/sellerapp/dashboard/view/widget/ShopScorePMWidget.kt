package com.tokopedia.sellerapp.dashboard.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.sellerapp.R

/**
 * Created by hendry on 2019-06-15.
 * _________________
 * (_________________)==========#
 *
 */
class ShopScorePMWidget : FrameLayout {
    private lateinit var roundCornerProgressBar: RoundGradientProgressBar
    private lateinit var tvProgressValue: TextView

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    @SuppressLint("NewApi")
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.widget_shop_score_pm, this)
        roundCornerProgressBar = view.findViewById(R.id.progress_value)
        tvProgressValue = view.findViewById(R.id.tv_progress_value)
    }

    fun setProgress(progress: Float) {
        roundCornerProgressBar.progress = progress
        tvProgressValue.text = MethodChecker.fromHtml(
            String.format(context.getString(R.string.score_of_total_score), progress.toInt(), MAX_PROGRESS))
    }

    fun setProgressColor(progressColors: IntArray) {
        roundCornerProgressBar.setProgressColor(progressColors)
    }

    companion object {

        val MAX_PROGRESS = 100
    }


}
