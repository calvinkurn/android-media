package com.tokopedia.play.view.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.quiz.QuizListAdapter
import com.tokopedia.play_common.view.quiz.QuizOptionItemDecoration
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*
import javax.inject.Inject

/**
 * @author by astidhiyaa on 07/04/22
 */
class PlayInteractiveQuizFragment @Inject constructor(): DialogFragment() {

    private lateinit var gameHeaderView: GameHeaderView
    private lateinit var rvQuizOption: RecyclerView
    private lateinit var quizAdapter: QuizListAdapter
    private lateinit var timerQuiz: TimerUnifySingle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_play_quiz_interactive_ongoing, container)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setStyle(STYLE_NO_FRAME, android.R.style.Theme)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameHeaderView = view.findViewById(R.id.quiz_header)
        rvQuizOption = view.findViewById(R.id.rv_quiz_question)
        timerQuiz = view.findViewById(R.id.timer_quiz_option)

        setUpView()
    }

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    private fun setUpView(){
        gameHeaderView.title = "Coba"
        gameHeaderView.isEditable = false
        gameHeaderView.type = GameHeaderView.Type.QUIZ

        val gCal = Calendar.getInstance()
        gCal.add(Calendar.MILLISECOND, 10000)
        timerQuiz.targetDate = gCal

        //TODO() testing purpose should be from the parameters

    }

    companion object{
        private const val TAG = "PlayInteractiveQuizFragment"
    }
}