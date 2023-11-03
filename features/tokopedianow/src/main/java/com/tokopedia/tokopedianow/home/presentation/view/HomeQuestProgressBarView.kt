package com.tokopedia.tokopedianow.home.presentation.view

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.dpToPx
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
        private const val CIRCLE_Y_OFFSET = 6
    }

    private val binding = LayoutTokopedianowQuestProgressBarViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var progressAnimEnd = DEFAULT_PROGRESS
    private var circleViewList = mutableListOf<HomeQuestProgressCircleView>()

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
            val circleYOffset = context.dpToPx(CIRCLE_Y_OFFSET)

            for (i in 0 until questCount) {
                val x = i * xMultiplier

                if (circleViewList.getOrNull(i) == null) {
                    val y = viewProgress.y + circleYOffset
                    val view = HomeQuestProgressCircleView(context)
                    circleViewList.add(view)
                    root.addView(view)
                    view.y = y
                    view.x = x
                } else {
                    val view = circleViewList[i]
                    root.removeView(view)
                    root.addView(view)
                }

                if (i == progressIndex) {
                    progressAnimEnd = x.toInt()
                }
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
            val lottieXOffset = context.resources.getDimensionPixelSize(
                unifyprinciplesR.dimen.unify_space_8
            )

            lottieStar.setAnimationFromUrl(TokopediaImageUrl.TOKOPEDIANOW_LOTTIE_QUEST_STAR)
            lottieStar.setFailureListener { }

            progressAnim.addUpdateListener { valueAnimator ->
                val width = valueAnimator.animatedValue as Int
                val x = width.toFloat()

                viewProgress.layoutParams = viewProgress.layoutParams.apply {
                    this.width = width
                }

                lottieStar.x = x - lottieXOffset
                imageStar.x = x
            }

            val animSet = AnimatorSet().apply {
                playTogether(starScaleXAnim, starScaleYAnim, progressAnim)
            }

            animSet.addListener(object : AnimatorListener {
                override fun onAnimationStart(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    lottieStar.playAnimation()
                }

                override fun onAnimationCancel(p0: Animator) {

                }

                override fun onAnimationRepeat(p0: Animator) {

                }
            })

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
