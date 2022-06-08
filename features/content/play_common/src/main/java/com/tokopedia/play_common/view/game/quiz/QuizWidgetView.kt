package com.tokopedia.play_common.view.game.quiz

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
        answerTrueAnimator.duration = 120L
        answerTrueAnimator.start()
    }

    private fun animateWrongAnswer(){
        answerFalseAnimator.addListener(animationListener)
        answerFalseAnimator.duration = 120L
        answerFalseAnimator.start()
    }


    interface Listener {
        fun onQuizOptionClicked(item: QuizChoicesUiModel)

        fun onQuizImpressed()
    }

    private val clickRotateMinAnimation = ObjectAnimator.ofFloat(
        binding.root, View.ROTATION, 0f, -10f
    )

    private val clickRotateMaxAnimation = ObjectAnimator.ofFloat(
        binding.root, View.ROTATION, 0f, 10f
    )

    private val clickScaleXAnimation = ObjectAnimator.ofFloat(
        binding.root, View.SCALE_X, 1f, 0.8f
    )
    private val clickScaleYAnimation = ObjectAnimator.ofFloat(
        binding.root, View.SCALE_Y, 1f, 0.8f
    )

    private val answerFalseAnimator = AnimatorSet().apply {
        interpolator = AnticipateInterpolator()
        playSequentially(clickRotateMinAnimation, clickRotateMaxAnimation)
        playTogether(clickScaleXAnimation, clickScaleYAnimation)
    }

    private val answerTrueAnimator = AnimatorSet().apply {
        interpolator = AnticipateOvershootInterpolator()
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

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        answerFalseAnimator.cancel()
        answerFalseAnimator.removeAllListeners()

        answerTrueAnimator.cancel()
        answerTrueAnimator.removeAllListeners()
    }
}