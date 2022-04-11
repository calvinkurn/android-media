package com.tokopedia.play.view.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R
import com.tokopedia.play.view.quiz.QuizChoiceViewHolder
import com.tokopedia.play.view.quiz.QuizChoicesUiModel
import com.tokopedia.play.view.quiz.QuizListAdapter
import com.tokopedia.play.view.quiz.QuizOptionItemDecoration
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import javax.inject.Inject

/**
 * @author by astidhiyaa on 07/04/22
 */
class PlayInteractiveQuizFragment @Inject constructor(): DialogFragment() {

    private lateinit var gameHeaderView: GameHeaderView
    private lateinit var rvQuizOption: RecyclerView
    private lateinit var quizAdapter: QuizListAdapter

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

        setUpView()
    }

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    private fun setUpView(){
        gameHeaderView.title = "Coba"
        gameHeaderView.isEditable = false
        gameHeaderView.type = GameHeaderView.Type.QUIZ

        //TODO() testing purpose should be from the parameters

        val list = mutableListOf<QuizChoicesUiModel.Complete>()
        list.add(0, QuizChoicesUiModel.Complete("1", "Pertanyaan Answere",PlayQuizOptionState.Answered(true)))
        list.add(1, QuizChoicesUiModel.Complete("2", "Pertanyaan Default",PlayQuizOptionState.Default('b')))
        list.add(1, QuizChoicesUiModel.Complete("3", "Pertanyaan Result",PlayQuizOptionState.Result(false)))

        rvQuizOption.apply {
            quizAdapter = QuizListAdapter(object : QuizChoiceViewHolder.Listener{
                override fun onClicked(item: QuizChoicesUiModel.Complete) {

                }
            })
            adapter = quizAdapter
            layoutManager = LinearLayoutManager(rvQuizOption.context, RecyclerView.VERTICAL, false)
            addItemDecoration(QuizOptionItemDecoration(this.context))
        }
        quizAdapter.setItemsAndAnimateChanges(list)
    }

    companion object{
        private const val TAG = "PlayInteractiveQuizFragment"
    }
}