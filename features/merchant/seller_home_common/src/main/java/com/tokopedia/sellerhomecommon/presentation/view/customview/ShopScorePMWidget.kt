package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcProgressBarWidgetBinding

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class ShopScorePMWidget : FrameLayout {

    companion object {
        private const val MAX_PROGRESS_FORMAT = "/%s"
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private var binding: ShcProgressBarWidgetBinding? = null

    private fun initView(context: Context) {
        View.inflate(context, R.layout.shc_progress_bar_widget, this)
        binding = ShcProgressBarWidgetBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    fun setProgressTitle(title: String) {
        binding?.tvShcProgressTitle?.text = title
    }

    fun setCurrentProgressText(progress: String) {
        binding?.tvShcCurrentProgress?.text = progress
    }

    fun setMaxProgressText(progress: String) {
        binding?.tvShcMaxProgress?.text = String.format(MAX_PROGRESS_FORMAT, progress)
    }

    fun setProgressValue(progress: Int) {
        binding?.shcProgressBarCurrent?.progress = progress.toFloat()
    }

    fun setMaxProgressValue(maxProgress: Int) {
        binding?.shcProgressBarCurrent?.max = maxProgress.toFloat()
    }

    fun setProgressColor(state: State) {
        val colors = when (state) {
            is State.Good -> {
                binding?.tvShcCurrentProgress?.setTextColor(
                    context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                )
                intArrayOf(
                    context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500),
                    context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_GN600)
                )
            }
            is State.Warning -> {
                binding?.tvShcCurrentProgress?.setTextColor(
                    context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_YN400)
                )
                intArrayOf(
                    context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_YN300),
                    context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_YN400)
                )
            }
            is State.Custom -> {
                binding?.tvShcCurrentProgress?.setTextColor(
                    context.getResColor(state.valueTextColorResId)
                )
                intArrayOf(
                    context.getResColor(state.barStartColorResId),
                    context.getResColor(state.barEndColorResId)
                )
            }
            else -> {
                binding?.tvShcCurrentProgress?.setTextColor(
                    context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                )
                intArrayOf(
                    context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_RN400),
                    context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                )
            }
        }
        binding?.shcProgressBarCurrent?.setProgressColor(colors)
    }

    sealed class State(open val name: String) {

        companion object {
            private const val GOOD = "GOOD"
            private const val WARNING = "WARNING"
            private const val DANGER = "DANGER"
            private const val CUSTOM = "CUSTOM"
        }

        object Good : State(GOOD)

        object Warning : State(WARNING)

        object Danger : State(DANGER)

        data class Custom(
            @ColorRes val valueTextColorResId: Int,
            @ColorRes val barStartColorResId: Int,
            @ColorRes val barEndColorResId: Int
        ) : State(CUSTOM)
    }
}
