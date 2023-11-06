package com.tokopedia.tokopedianow.home.presentation.view

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.imageassets.TokopediaImageUrl
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
        private const val DEFAULT_PROGRESS = 1f
        private const val STAR_SCALE_UP = 1f
        private const val STAR_SCALE_DOWN = 0.7f
        private const val ANIM_DURATION = 600L
    }

    private val binding = LayoutTokopedianowQuestProgressBarViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val defaultStrokeResId = unifyprinciplesR.color.Unify_NN200
    private val defaultColorResId = unifyprinciplesR.color.Unify_BN600

    private var progressAnimEnd = DEFAULT_PROGRESS
    private var progressItems = mutableListOf<HomeQuestProgressCircleView>()
    private var listener: HomeQuestProgressBarListener? = null
    private var animSet: AnimatorSet? = null

    fun bind(
        uiModel: HomeQuestWidgetUiModel,
        listener: HomeQuestProgressBarListener?
    ) {
        binding.root.addOneTimeGlobalLayoutListener {
            val hasFinishedQuest = uiModel.questList
                .firstOrNull { it.isFinished() } != null
            setListener(listener)
            setupView(uiModel)

            if (hasFinishedQuest) {
                startAnimation()
            }
        }
    }

    fun switchItemColor(index: Int) {
        val selectedItemView = progressItems.firstOrNull { it.isSelected }
        val selectedItemIndex = progressItems.indexOf(selectedItemView)
        if (animSet?.isRunning == true || progressItems.isEmpty() || index == selectedItemIndex) return

        val selectedColorResId = unifyprinciplesR.color.Unify_NN0
        val currentItemView = progressItems[index]
        val isSelected = currentItemView.isSelected

        val strokeColorId = if (isSelected) {
            defaultStrokeResId
        } else {
            selectedColorResId
        }

        val imageColorId = if (isSelected) {
            defaultColorResId
        } else {
            selectedColorResId
        }

        currentItemView.setColor(strokeColorId, imageColorId)
        currentItemView.isSelected = !isSelected

        resetProgresItem(selectedItemView)
    }

    private fun setupView(uiModel: HomeQuestWidgetUiModel) {
        binding.apply {
            val questList = uiModel.questList
            val questCount = questList.count()
            val lastFinishedQuest = questList.lastOrNull { it.isFinished() }
            val progressIndex = lastFinishedQuest?.let { questList.indexOf(lastFinishedQuest) }
            val itemSpacing = binding.viewBackground.width / (questCount - 1)

            viewProgress.layoutParams = viewProgress.layoutParams.apply {
                width = DEFAULT_PROGRESS.toInt()
            }

            for (i in 0 until questCount) {
                val view = if (progressItems.getOrNull(i) == null) {
                    val x = i * itemSpacing

                    val progressItemView = HomeQuestProgressCircleView(context).apply {
                        id = i.hashCode()
                    }
                    progressItems.add(progressItemView)
                    container.addView(progressItemView)

                    progressItemView.setOnClickListener {
                        val isSelected = progressItems[i].isSelected
                        listener?.onClickProgressItem(i, isSelected)
                        switchItemColor(i)
                    }

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(container)

                    constraintSet.connect(
                        progressItemView.id,
                        ConstraintSet.TOP,
                        container.id,
                        ConstraintSet.TOP
                    )
                    constraintSet.connect(
                        progressItemView.id,
                        ConstraintSet.BOTTOM,
                        container.id,
                        ConstraintSet.BOTTOM
                    )

                    constraintSet.applyTo(container)

                    progressItemView.x = x.toFloat()
                    progressItemView
                } else {
                    progressItems[i]
                }

                if (i == progressIndex) {
                    progressAnimEnd = view.x
                }

                resetProgresItem(view)
            }

            imageStar.bringToFront()
        }
    }

    private fun resetProgresItem(view: HomeQuestProgressCircleView?) {
        view?.apply {
            setColor(defaultStrokeResId, defaultColorResId)
            isSelected = false
        }
    }

    private fun setListener(listener: HomeQuestProgressBarListener?) {
        this.listener = listener
    }

    private fun startAnimation() {
        binding.apply {
            val progressAnim = ValueAnimator.ofFloat(DEFAULT_PROGRESS, progressAnimEnd)

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
                val x = valueAnimator.animatedValue as Float

                viewProgress.layoutParams = viewProgress.layoutParams.apply {
                    width = x.toInt()
                }

                lottieStar.x = x - lottieXOffset
                imageStar.x = x
            }

            animSet = AnimatorSet().apply {
                playTogether(starScaleXAnim, starScaleYAnim, progressAnim)
            }

            animSet?.addListener(object : AnimatorListener {
                override fun onAnimationStart(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    lottieStar.playAnimation()
                    listener?.onAnimationFinished()
                }

                override fun onAnimationCancel(p0: Animator) {

                }

                override fun onAnimationRepeat(p0: Animator) {

                }
            })

            animSet?.duration = ANIM_DURATION
            animSet?.start()
        }
    }

    interface HomeQuestProgressBarListener {
        fun onClickProgressItem(index: Int, isSelected: Boolean)
        fun onAnimationFinished()
    }
}
