package com.tokopedia.tokopedianow.home.presentation.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowQuestProgressBarViewBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestWidgetUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class HomeQuestProgressBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    companion object {
        private const val STAR_SCALE_UP = 1f
        private const val STAR_SCALE_DOWN = 0.7f
        private const val PROGRESS_ANIM_START = 0
        private const val ANIM_DURATION = 600L
        private const val DEFAULT_PROGRESS = 1
    }

    private val binding = LayoutTokopedianowQuestProgressBarViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var progressAnimEnd = DEFAULT_PROGRESS

    fun bind(uiModel: HomeQuestWidgetUiModel) {
        binding.root.addOneTimeGlobalLayoutListener {
            val hasFinishedQuest = uiModel.questList
                .firstOrNull { it.isFinished() } != null
            setupView(uiModel)

            if (hasFinishedQuest) {
                startAnimation()
            }
        }
    }

    private fun setupView(uiModel: HomeQuestWidgetUiModel) {
        binding.apply {
            val questList = uiModel.questList
            val questCount = questList.count()
            val lastFinishedQuest = questList.lastOrNull { it.isFinished() }
            val progressIndex = lastFinishedQuest?.let { questList.indexOf(lastFinishedQuest) }
            val xMultiplier = getViewX() / (questCount - 1)

            for (i in 0 until questCount) {
                val circle = HomeQuestProgressCircleView(context)
                val x = i * xMultiplier
                if (i == progressIndex) {
                    progressAnimEnd = x.toInt()
                }
                root.addView(circle)
                circle.x = x
            }

            imageStar.bringToFront()
        }
    }

    private fun startAnimation() {
        binding.apply {
            val progressAnim = ValueAnimator.ofInt(PROGRESS_ANIM_START, progressAnimEnd)

            val starScaleXAnim = ObjectAnimator.ofFloat(
                imageStar,
                SCALE_X,
                STAR_SCALE_UP,
                STAR_SCALE_DOWN,
                STAR_SCALE_UP
            )
            val starScaleYAnim = ObjectAnimator.ofFloat(
                imageStar,
                SCALE_Y,
                STAR_SCALE_UP,
                STAR_SCALE_DOWN,
                STAR_SCALE_UP
            )

            progressAnim.addUpdateListener { valueAnimator ->
                val width = valueAnimator.animatedValue as Int
                val layoutParams: ViewGroup.LayoutParams = viewProgress.layoutParams
                layoutParams.width = width

                viewProgress.layoutParams = layoutParams
                imageStar.x = width.toFloat()
            }

            val animSet = AnimatorSet().apply {
                playTogether(starScaleXAnim, starScaleYAnim, progressAnim)
            }

            animSet.duration = ANIM_DURATION
            animSet.start()
        }
    }

    private fun getViewX(): Float {
        val rect = Rect()
        val endOffset = context.resources.getDimensionPixelSize(
            unifyprinciplesR.dimen.unify_space_16
        )
        binding.root.getLocalVisibleRect(rect)
        return rect.right.toFloat() - endOffset
    }
}
