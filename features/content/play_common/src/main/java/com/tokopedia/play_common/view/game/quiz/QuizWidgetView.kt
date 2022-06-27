package com.tokopedia.play_common.view.game.quiz

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.databinding.ViewQuizWidgetBinding
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder
import com.tokopedia.play_common.view.quiz.QuizListAdapter
import com.tokopedia.play_common.view.quiz.QuizOptionItemDecoration
import com.tokopedia.unifyprinciples.UnifyMotion
import java.util.*

/**
 * @author by astidhiyaa on 18/04/22
 */
class QuizWidgetView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewQuizWidgetBinding.inflate(
        LayoutInflater.from(context),
        this)

    private var mListener: Listener? = null

    private val impressHolder = ImpressHolder()
    private val quizAdapter: QuizListAdapter = QuizListAdapter(object : QuizChoiceViewHolder.Listener{
        override fun onClicked(item: QuizChoicesUiModel) {
            mListener?.onQuizOptionClicked(item)
        }
    })

    private val clickRotateMinAnimation = ObjectAnimator.ofFloat(
        binding.root, View.ROTATION, 0f, -9f
    )

    private val clickRotateMaxAnimation = ObjectAnimator.ofFloat(
        binding.root, View.ROTATION, 0f, 9f
    )

    private val clickScaleXAnimation = ObjectAnimator.ofFloat(
        binding.root, View.SCALE_X, 0.8f, 1f
    )
    private val clickScaleYAnimation = ObjectAnimator.ofFloat(
        binding.root, View.SCALE_Y, 0.8f, 1f
    )

    private val answerFalseAnimator = AnimatorSet().apply {
        playTogether(clickScaleXAnimation, clickScaleYAnimation)
        playSequentially(clickRotateMinAnimation, clickRotateMaxAnimation)
    }

    private val answerTrueAnimator = AnimatorSet().apply {
        playTogether(clickScaleXAnimation, clickScaleYAnimation)
    }

    private val animationListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            binding.root.rotation = 0f
            binding.root.scaleX = 1f
            binding.root.scaleY = 1f
        }

        override fun onAnimationCancel(animation: Animator?) {}

        override fun onAnimationRepeat(animation: Animator?) {}
    }

    init {
        binding.rvQuizQuestion.apply {
            itemAnimator = null
            adapter = quizAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(QuizOptionItemDecoration(context))
            addOnImpressionListener(impressHolder){
                mListener?.onQuizImpressed()
            }
        }

        binding.quizHeader.isEditable = false

        answerFalseAnimator.childAnimations.forEach {
            if (it !is ValueAnimator) return@forEach
            it.duration = UnifyMotion.T3
            it.interpolator = UnifyMotion.EASE_OVERSHOOT
        }

        answerTrueAnimator.childAnimations.forEach {
            if (it !is ValueAnimator) return@forEach
            it.duration = UnifyMotion.T2
            it.interpolator = UnifyMotion.EASE_OVERSHOOT
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.layoutTimer.timerQuizOption.pause()
        mListener = null
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setTitle(title: String) {
        binding.quizHeader.setupQuiz(title)
    }

    fun hideTimer(){
        binding.layoutTimer.root.hide()
    }

    fun setTargetTime(targetTime: Calendar, onFinished: () -> Unit) {
        binding.layoutTimer.timerQuizOption.apply {
            pause()

            targetDate = targetTime
            onFinish = onFinished

            resume()
        }
    }

    fun getHeader(): GameHeaderView {
        return binding.quizHeader
    }

    fun setupQuizForm(listOfChoices: List<QuizChoicesUiModel>) {
        quizAdapter.setItemsAndAnimateChanges(listOfChoices)
    }

    fun setReward(reward: String){
        binding.viewQuizReward.root.shouldShowWithAction(reward.isNotBlank()){
            binding.viewQuizReward.tvGameReward.text = reward
        }
    }

    fun animateAnswer(isCorrect: Boolean){
        if(isCorrect){
            animateCorrectAnswer()
        } else {
            animateWrongAnswer()
        }
    }

    private fun animateCorrectAnswer(){
        answerTrueAnimator.addListener(animationListener)
        answerTrueAnimator.start()
    }

    private fun animateWrongAnswer(){
        answerFalseAnimator.addListener(animationListener)
        answerFalseAnimator.start()
    }


    interface Listener {
        fun onQuizOptionClicked(item: QuizChoicesUiModel)

        fun onQuizImpressed()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        answerFalseAnimator.cancel()
        answerFalseAnimator.removeAllListeners()

        answerTrueAnimator.cancel()
        answerTrueAnimator.removeAllListeners()
    }
}