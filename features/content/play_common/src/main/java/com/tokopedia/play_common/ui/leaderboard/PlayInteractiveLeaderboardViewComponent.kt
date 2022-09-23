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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayInteractiveLeaderboardAdapter
import com.tokopedia.play_common.ui.leaderboard.itemdecoration.PlayLeaderBoardItemDecoration
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveLeaderboardViewHolder
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton


/**
 * Created by mzennis on 29/06/21.
 */
class PlayInteractiveLeaderboardViewComponent(
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

    private val leaderboardAdapter = PlayInteractiveLeaderboardAdapter(object : PlayInteractiveLeaderboardViewHolder.Listener {
        override fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int) {
            listener.onChatWinnerButtonClicked(
                this@PlayInteractiveLeaderboardViewComponent,
                winner,
                position
            )
        }

        override fun onChoiceItemClicked(item: QuizChoicesUiModel) {
            listener.onChoiceItemClicked(item)
        }

        override fun onLeaderBoardImpressed(leaderboard: PlayLeaderboardUiModel) {
            listener.onLeaderBoardImpressed(
                this@PlayInteractiveLeaderboardViewComponent,
                leaderboard
            )
        }
    })

    private val leaderboardAdapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (itemCount > 0) layoutManager.scrollToPositionWithOffset(0, 0)
        }
    }

    private val impressHolder = ImpressHolder()

    private val layoutManager = LinearLayoutManager(rvLeaderboard.context)

    init {
        tvSheetTitle.setText(R.string.play_interactive_leaderboard_title)

        ivSheetClose.setOnClickListener {
            listener.onCloseButtonClicked(this)
        }

        rvLeaderboard.apply {
            addItemDecoration(PlayLeaderBoardItemDecoration(rvLeaderboard.context))
            adapter = leaderboardAdapter
            layoutManager = layoutManager
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

        btnRefreshError.addImpressionListener(impressHolder){
            listener.onRefreshButtonImpressed(this)
        }
    }

    fun clearTopPadding(){
        rootView.updatePadding(
            top = 0
        )
    }

    fun setData(leaderboards: List<PlayLeaderboardUiModel>) {
        errorView.hide()
        llPlaceholder.hide()
        rvLeaderboard.show()
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

    fun setTitle(title:String) {
        tvSheetTitle.text = title
    }

    private fun showBtnLoader(shouldShow: Boolean){
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
        ViewCompat.setOnApplyWindowInsetsListener(rootView, null)
    }

    interface Listener {
        fun onCloseButtonClicked(view: PlayInteractiveLeaderboardViewComponent)
        fun onChatWinnerButtonClicked(
            view: PlayInteractiveLeaderboardViewComponent,
            winner: PlayWinnerUiModel,
            position: Int,
        ){}
        fun onRefreshButtonClicked(view: PlayInteractiveLeaderboardViewComponent)
        fun onChoiceItemClicked(item: QuizChoicesUiModel) {}
        fun onRefreshButtonImpressed(view: PlayInteractiveLeaderboardViewComponent)
        fun onLeaderBoardImpressed(view: PlayInteractiveLeaderboardViewComponent, leaderboard: PlayLeaderboardUiModel)
    }
}