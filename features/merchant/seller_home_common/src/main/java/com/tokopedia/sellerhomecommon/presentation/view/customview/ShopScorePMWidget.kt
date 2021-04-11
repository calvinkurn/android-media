package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.sellerhomecommon.R
import kotlinx.android.synthetic.main.shc_progress_bar_widget.view.*

/**
 * Created By @ilhamsuaib on 20/05/20
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
        View.inflate(context, R.layout.shc_progress_bar_widget, this)
    }

    fun setProgressTitle(title: String) {
        tv_progress_title.text = title
    }

    fun setCurrentProgressText(progress: String) {
        tv_current_progress.text = progress
    }

    fun setMaxProgressText(progress: String) {
        tv_max_progress.text = "/".plus(progress)
    }

    fun setProgressValue(progress: Int) {
        progress_bar_current.progress = progress.toFloat()
    }

    fun setMaxProgressValue(maxProgress: Int) {
        progress_bar_current.max = maxProgress.toFloat()
    }

    fun setProgressColor(state: State) {
        val colors = when (state) {
            is State.Good -> {
                tv_current_progress.setTextColor(ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_G500))
                intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G600))
            }
            is State.Warning -> {
                tv_current_progress.setTextColor(ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_Y400))
                intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y300), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y400))
            }
            is State.Custom -> {
                tv_current_progress.setTextColor(ContextCompat.getColor(context, state.valueTextColorResId))
                intArrayOf(ContextCompat.getColor(context, state.barStartColorResId), ContextCompat.getColor(context, state.barEndColorResId))
            }
            else -> {
                tv_current_progress.setTextColor(ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_R500))
                intArrayOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R400), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
            }
        }
        progress_bar_current.setProgressColor(colors)
    }

    sealed class State {
        object Good : State()

        object Warning : State()

        object Danger : State()

        data class Custom(
                val valueTextColorResId: Int,
                val barStartColorResId: Int,
                val barEndColorResId: Int
        ) : State()
    }
}
