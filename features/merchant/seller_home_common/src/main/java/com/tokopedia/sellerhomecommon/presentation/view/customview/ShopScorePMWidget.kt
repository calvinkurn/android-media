package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcProgressBarWidgetBinding

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
        binding?.tvShcMaxProgress?.text = "/".plus(progress)
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
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                )
                intArrayOf(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G400
                    ),
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G600
                    )
                )
            }
            is State.Warning -> {
                binding?.tvShcCurrentProgress?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Y400
                    )
                )
                intArrayOf(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Y300
                    ),
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Y400
                    )
                )
            }
            is State.Custom -> {
                binding?.tvShcCurrentProgress?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        state.valueTextColorResId
                    )
                )
                intArrayOf(
                    ContextCompat.getColor(context, state.barStartColorResId),
                    ContextCompat.getColor(context, state.barEndColorResId)
                )
            }
            else -> {
                binding?.tvShcCurrentProgress?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R500
                    )
                )
                intArrayOf(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R400
                    ),
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R500
                    )
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
            val valueTextColorResId: Int,
            val barStartColorResId: Int,
            val barEndColorResId: Int
        ) : State(CUSTOM)
    }
}
