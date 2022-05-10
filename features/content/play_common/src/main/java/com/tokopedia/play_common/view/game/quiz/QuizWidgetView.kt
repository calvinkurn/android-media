package com.tokopedia.play_common.view.game.quiz

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.play_common.databinding.ViewQuizWidgetBinding
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder
import com.tokopedia.play_common.view.quiz.QuizListAdapter
import com.tokopedia.play_common.view.quiz.QuizOptionItemDecoration
import java.util.Calendar

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

    fun setupQuizForm(listOfChoices: List<QuizChoicesUiModel>) {
        quizAdapter.setItemsAndAnimateChanges(listOfChoices)
    }

    fun setReward(reward: String){
        binding.viewGameReward.root.shouldShowWithAction(reward.isNotBlank()){
            binding.viewGameReward.tvGameReward.text = reward
        }
    }

    interface Listener {
        fun onQuizOptionClicked(item: QuizChoicesUiModel)
    }
}