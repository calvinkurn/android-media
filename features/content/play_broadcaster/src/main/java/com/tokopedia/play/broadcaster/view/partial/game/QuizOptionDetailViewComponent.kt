package com.tokopedia.play.broadcaster.view.partial.game

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play.broadcaster.ui.leaderboard.adapter.PlayBroadcastGameParticipantAdapter
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder
import com.tokopedia.play_common.view.quiz.QuizListAdapter
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

class QuizOptionDetailViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.cl_quiz_option_details) {

    private val nsvQuizDetail: NestedScrollView = findViewById(R.id.nsvQuizDetail)
    private val rvChoice: RecyclerView = findViewById(R.id.rv_choice)
    private val rvWinner: RecyclerView = findViewById(R.id.rv_winner)
    private val rvParticipant: RecyclerView = findViewById(R.id.rv_participants)

    private val choiceAdapter = QuizListAdapter(object : QuizChoiceViewHolder.Listener {
        override fun onClicked(item: QuizChoicesUiModel) {
        }
    })
    private val winnerAdapter = PlayBroadcastGameParticipantAdapter {}
    private val participantAdapter = PlayBroadcastGameParticipantAdapter {
        listener.loadMoreParticipant()
    }

    private val errorView: ConstraintLayout = findViewById(commonR.id.cl_leaderboard_error)
    private val emptyParticipantView: ConstraintLayout = findViewById(R.id.cl_participant_empty)
    private val tvSheetTitle: TextView = findViewById(R.id.tv_sheet_title)
    private val ivSheetBack: ImageView = findViewById(R.id.iv_sheet_back)
    private val placeholder: View = findViewById(R.id.participant_placeholder)
    private val tvEmptyParticipant: TextView = findViewById(R.id.tv_participant_empty_message)
    private val bottomSheetBehavior = try {
        BottomSheetBehavior.from(rootView)
    } catch (e: IllegalArgumentException) {
        null
    }

    init {
        tvSheetTitle.text =
            getString(com.tokopedia.play.broadcaster.R.string.play_bro_ongoing_bottomsheet_title)

        ivSheetBack.setOnClickListener {
            listener.onBackButtonClicked(this)
        }

        findViewById<UnifyButton>(commonR.id.btn_action_leaderboard_error).setOnClickListener {
            listener.onRefreshButtonClicked(this)
        }

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
        rvChoice.adapter = choiceAdapter
        rvWinner.adapter = winnerAdapter
        rvParticipant.adapter = participantAdapter
        rvParticipant.layoutManager = GridLayoutManager(container.context, 2)
    }

    fun setData(choiceDetail: QuizChoiceDetailUiModel, ongoing: Boolean) {
        errorView.hide()
        placeholder.hide()
        choiceAdapter.setItemsAndAnimateChanges(listOf(choiceDetail.choice))
        winnerAdapter.setItemsAndAnimateChanges(choiceDetail.winners)
        participantAdapter.setItemsAndAnimateChanges(choiceDetail.participants)
        rvChoice.show()
        rvWinner.show()
        rvParticipant.show()
        emptyParticipantView.showWithCondition(
            choiceDetail.participants.isEmpty()
                    && choiceDetail.winners.isEmpty()
        )
        if (ongoing)
            tvEmptyParticipant.text = getString(R.string.play_bro_ongoing_bottomsheet_empty_participant_message)
        else {
            if ((choiceDetail.choice.type as PlayQuizOptionState.Participant).isCorrect)
                tvEmptyParticipant.text = getString(R.string.play_bro_finished_bottomsheet_option_right_empty)
            else
                tvEmptyParticipant.text = getString(R.string.play_bro_finished_bottomsheet_option_empty)
        }
    }

    fun setError() {
        placeholder.hide()
        errorView.show()
        rvWinner.hide()
        rvChoice.hide()
        rvParticipant.hide()
    }

    fun setLoading() {
        placeholder.show()
        errorView.hide()
        rvWinner.hide()
        rvChoice.hide()
        rvParticipant.hide()
        emptyParticipantView.hide()
    }

    fun setTitle(title: String) {
        tvSheetTitle.text = title
    }

    fun addOnTouchListener(listener: NestedScrollView.OnScrollChangeListener) {
        nsvQuizDetail.setOnScrollChangeListener(listener)
    }

    override fun show() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else super.show()
    }

    override fun hide() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else super.hide()
    }

    /**
     * Lifecycle Event
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        rootView.requestApplyInsetsWhenAttached()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        ViewCompat.setOnApplyWindowInsetsListener(rootView, null)
    }

    interface Listener {
        fun onBackButtonClicked(view: QuizOptionDetailViewComponent)
        fun onRefreshButtonClicked(view: QuizOptionDetailViewComponent)
        fun loadMoreParticipant()
    }
}