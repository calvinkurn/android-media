package com.tokopedia.play_common.view.game.quiz

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.databinding.ViewQuizWidgetBinding
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.util.AnimationUtils
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder
import com.tokopedia.play_common.view.quiz.QuizListAdapter
import com.tokopedia.play_common.view.quiz.QuizOptionItemDecoration
import java.util.*

/**
 * @author by astidhiyaa on 18/04/22
 */
@Suppress("MagicNumber")
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

    /**
     * For correct answer
     */
    private val scaleBounceX = AnimationUtils.addSpringAnim(
        view = binding.root, property = SpringAnimation.SCALE_X, startPosition = 0.5f,
        finalPosition = 1f, stiffness = SpringForce.STIFFNESS_LOW, dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY, velocity = 0f
    )

    private val scaleBounceY = AnimationUtils.addSpringAnim(
        view = binding.root, property = SpringAnimation.SCALE_Y, startPosition = 0.5f,
        finalPosition = 1f, stiffness = SpringForce.STIFFNESS_LOW, dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY, velocity = 0f
    )

    /**
     * For wrong answer
     */

    private val scaleX = AnimationUtils.addSpringAnim(
        view = binding.root, property = SpringAnimation.SCALE_X, startPosition = 0.7f,
        finalPosition = 1f, stiffness = SpringForce.STIFFNESS_LOW, dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY, velocity = 0f
    )

    private val scaleY = AnimationUtils.addSpringAnim(
        view = binding.root, property = SpringAnimation.SCALE_Y, startPosition = 0.7f,
        finalPosition = 1f, stiffness = SpringForce.STIFFNESS_LOW, dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY, velocity = 0f
    )

    private val rotate = AnimationUtils.addSpringAnim(
        view = binding.root, property = SpringAnimation.ROTATION, startPosition = -9f,
        finalPosition = 9f, stiffness = SpringForce.STIFFNESS_VERY_LOW, dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY, velocity = 0f
    )

    private val rotateBack = AnimationUtils.addSpringAnim(
        view = binding.root, property = SpringAnimation.ROTATION, startPosition = 9f,
        finalPosition = 0f, stiffness = SpringForce.STIFFNESS_LOW, dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY, velocity = 200f
    )

    private val rotateEndListener =
        DynamicAnimation.OnAnimationUpdateListener { animation, value, velocity ->
            if(value > 8f)
                rotate.skipToEnd()
                rotateBack.start()
        }

    init {
        binding.rvQuizQuestion.apply {
            itemAnimator = null
            adapter = quizAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(QuizOptionItemDecoration(context))
            addImpressionListener(impressHolder){
                mListener?.onQuizImpressed()
            }
        }

        binding.quizHeader.isEditable = false
        rotate.addUpdateListener(rotateEndListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.layoutTimer.timerQuizOption.pause()
        mListener = null
        cancelAllAnim()
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

    fun animateAnswer(isCorrect: Boolean){
        if(isCorrect){
            animateCorrectAnswer()
        } else {
            animateWrongAnswer()
        }
    }

    private fun animateCorrectAnswer(){
        scaleBounceX.start()
        scaleBounceY.start()
    }

    private fun animateWrongAnswer(){
        scaleAnim()
        rotateAnim()
    }

    private fun scaleAnim(){
        scaleX.start()
        scaleY.start()
    }

    private fun rotateAnim(){
        rotate.start()
    }

    interface Listener {
        fun onQuizOptionClicked(item: QuizChoicesUiModel)

        fun onQuizImpressed()
    }

    private fun cancelAllAnim() {
        scaleX.cancel()
        scaleY.cancel()
        rotate.cancel()
        scaleBounceX.cancel()
        scaleBounceY.cancel()
        rotateBack.cancel()
        rotate.removeUpdateListener(rotateEndListener)
    }
}
