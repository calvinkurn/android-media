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
import androidx.core.content.ContextCompat
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowQuestProgressBarViewBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestWidgetUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.tokopedianow.R

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

    private val defaultBorderResId =  R.drawable.tokopedianow_quest_circle_border_default
    private val defaultColorResId = R.color.tokopedianow_quest_progress_dms_color

    private var progressAnimEnd = DEFAULT_PROGRESS
    private var progressItems = mutableListOf<HomeQuestProgressCircleView>()
    private var listener: HomeQuestProgressBarListener? = null
    private var animSet: AnimatorSet? = null

    fun bind(
        uiModel: HomeQuestWidgetUiModel,
        listener: HomeQuestProgressBarListener?
    ) {
        binding.apply {
            resetViewProgressBarWidth()
            root.addOneTimeGlobalLayoutListener {
                val hasFinishedQuest = uiModel.questList
                    .firstOrNull { it.isFinished() } != null
                setListener(listener)
                setupView(uiModel)

                if (hasFinishedQuest) {
                    startAnimation()
                }
            }
        }
    }

    fun switchItemColor(index: Int) {
        val selectedItemView = progressItems.firstOrNull { it.isSelected }
        val selectedItemIndex = progressItems.indexOf(selectedItemView)
        if (animSet?.isRunning == true || progressItems.isEmpty() || index == selectedItemIndex) return

        val selectedColorResId = R.color.tokopedianow_quest_selected_circle_dms_color
        val selectedBorderResId = R.drawable.tokopedianow_quest_circle_border_selected
        val currentItemView = progressItems[index]
        val isSelected = currentItemView.isSelected

        val borderDrawableId = if (isSelected) {
            defaultBorderResId
        } else {
            selectedBorderResId
        }

        val imageColorId = if (isSelected) {
            defaultColorResId
        } else {
            selectedColorResId
        }

        currentItemView.setColor(borderDrawableId, imageColorId)
        currentItemView.isSelected = !isSelected

        resetProgresItem(selectedItemView)
    }

    private fun setupView(uiModel: HomeQuestWidgetUiModel) {
        binding.apply {
            val questList = uiModel.questList
            val questCount = questList.count()
            val currentProgressPosition = uiModel.currentProgressPosition
            val itemSpacing = binding.viewBackground.width / (questCount - 1)
            val viewBackgroundResId = R.color.tokopedianow_quest_circle_border_dms_color
            val starImageDrawable = if(uiModel.isStarted) {
                ContextCompat.getDrawable(context, R.drawable.tokopedianow_ic_quest_star)
            } else {
                ContextCompat.getDrawable(context, R.drawable.tokopedianow_ic_quest_star_grey)
            }

            viewBackground.setBackgroundColor(ContextCompat.getColor(context, viewBackgroundResId))
            imageStar.setImageDrawable(starImageDrawable)

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

                if (i == currentProgressPosition) {
                    setProgressAnimEnd(i, uiModel, view)
                }

                resetProgresItem(view)
            }

            imageStar.bringToFront()
        }
    }

    private fun setProgressAnimEnd(
        index: Int,
        uiModel: HomeQuestWidgetUiModel,
        view: HomeQuestProgressCircleView
    ) {
        val questCount = uiModel.questList.count() - 1
        val currentProgressPosition = uiModel.currentProgressPosition
        val isProgressLastQuest = currentProgressPosition == questCount
        progressAnimEnd = if(index == questCount && isProgressLastQuest) {
            val offset = context.resources.getDimensionPixelSize(
                R.dimen.tokopedianow_quest_star_offset
            )
            view.x - offset
        } else {
            view.x
        }
    }

    private fun resetProgresItem(view: HomeQuestProgressCircleView?) {
        view?.apply {
            setColor(defaultBorderResId, defaultColorResId)
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
                R.dimen.tokopedianow_quest_lottie_offset
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

    private fun resetViewProgressBarWidth() {
        binding.apply {
            viewProgress.layoutParams = viewProgress.layoutParams.apply {
                width = DEFAULT_PROGRESS.toInt()
            }
        }
    }

    interface HomeQuestProgressBarListener {
        fun onClickProgressItem(index: Int, isSelected: Boolean)
        fun onAnimationFinished()
    }
}
