package com.tokopedia.play_common.ui.leaderboard

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayGameAdapter
import com.tokopedia.play_common.ui.leaderboard.itemdecoration.PlayLeaderBoardItemDecoration
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayGameViewHolder
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton
import java.lang.Exception

/**
 * @author by astidhiyaa on 16/08/22
 */
class PlayGameLeaderboardViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.cl_leaderboard_sheet) {
    private val rvLeaderboard: RecyclerView = findViewById(R.id.rv_leaderboard)
    private val errorView: ConstraintLayout = findViewById(R.id.cl_leaderboard_error)
    private val llPlaceholder: LinearLayout = findViewById(R.id.ll_leaderboard_placeholder)
    private val tvSheetTitle: TextView = findViewById(R.id.tv_sheet_title)
    private val ivSheetClose: ImageView = findViewById(R.id.iv_sheet_close)
    private val btnRefreshError: UnifyButton = findViewById(R.id.btn_action_leaderboard_error)

    private val bottomSheetBehavior = try {
        BottomSheetBehavior.from(rootView)
    } catch (e: IllegalArgumentException) {
        null
    }

    private val leaderboardAdapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            if (itemCount > 0) rvLeaderboard.smoothScrollToPosition(0)
        }
    }

    private val impressHolder = ImpressHolder()

    private val leaderboardAdapter = PlayGameAdapter(object : QuizChoiceViewHolder.Listener {
        override fun onClicked(item: QuizChoicesUiModel) {
            listener.onChoiceItemClicked(item)
        }

    }, object : PlayGameViewHolder.Winner.Listener {
        override fun onChatButtonClicked(item: LeaderboardGameUiModel.Winner, position: Int) {
            listener.onChatWinnerButtonClicked(this@PlayGameLeaderboardViewComponent, item, position)
        }
    }, object : PlayGameViewHolder.Header.Listener{
        override fun onLeaderBoarImpressed(item: LeaderboardGameUiModel.Header) {
            listener.onLeaderBoardImpressed(this@PlayGameLeaderboardViewComponent, item)
        }
    })

    private val itemDecoration : PlayLeaderBoardItemDecoration

    init {
        tvSheetTitle.setText(R.string.play_interactive_leaderboard_title)

        ivSheetClose.setOnClickListener {
            listener.onCloseButtonClicked(this)
        }

        itemDecoration = PlayLeaderBoardItemDecoration(rvLeaderboard.context)

        rvLeaderboard.apply {
            adapter = leaderboardAdapter
            layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        btnRefreshError.setOnClickListener {
            showBtnLoader(shouldShow = true)
            listener.onRefreshButtonClicked(this)
        }

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }

        registerAdapterObserver()

        rootView.addImpressionListener(impressHolder){
            if (btnRefreshError.isVisible) listener.onRefreshButtonImpressed(this)
        }
    }

    fun clearTopPadding() {
        rootView.updatePadding(
            top = 0
        )
    }

    private fun invalidateItemDecoration(){
        try {
            rvLeaderboard.post {
                rvLeaderboard.invalidateItemDecorations()
            }
        } catch (e: Exception) {}
    }

    fun setData(leaderboards: List<LeaderboardGameUiModel>) {
        errorView.hide()
        llPlaceholder.hide()
        rvLeaderboard.show()
        invalidateItemDecoration()
        leaderboardAdapter.setItemsAndAnimateChanges(leaderboards)
    }

    fun setError() {
        showBtnLoader(shouldShow = false)

        errorView.show()
        rvLeaderboard.hide()
        llPlaceholder.hide()
    }

    fun setLoading() {
        errorView.hide()
        rvLeaderboard.hide()
        llPlaceholder.show()
    }

    fun addItemTouchListener(listener: RecyclerView.OnItemTouchListener) {
        rvLeaderboard.addOnItemTouchListener(listener)
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }

        show()
    }

    fun setTitle(title: String) {
        tvSheetTitle.text = title
    }

    private fun showBtnLoader(shouldShow: Boolean) {
        btnRefreshError.isLoading = shouldShow
        btnRefreshError.isClickable = !shouldShow
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

    private fun registerAdapterObserver() {
        leaderboardAdapter.registerAdapterDataObserver(leaderboardAdapterObserver)
    }

    private fun unregisterAdapterObserver() {
        leaderboardAdapter.unregisterAdapterDataObserver(leaderboardAdapterObserver)
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
        unregisterAdapterObserver()
        rvLeaderboard.removeItemDecoration(itemDecoration)
        ViewCompat.setOnApplyWindowInsetsListener(rootView, null)
    }

    interface Listener {
        fun onCloseButtonClicked(view: PlayGameLeaderboardViewComponent)
        fun onChatWinnerButtonClicked(
            view: PlayGameLeaderboardViewComponent,
            winner: LeaderboardGameUiModel.Winner,
            position: Int,
        ) {
        }
        fun onRefreshButtonClicked(view: PlayGameLeaderboardViewComponent)
        fun onChoiceItemClicked(item: QuizChoicesUiModel) {}
        fun onRefreshButtonImpressed(view: PlayGameLeaderboardViewComponent)
        fun onLeaderBoardImpressed(
            view: PlayGameLeaderboardViewComponent,
            leaderboard: LeaderboardGameUiModel.Header
        )
    }
}
