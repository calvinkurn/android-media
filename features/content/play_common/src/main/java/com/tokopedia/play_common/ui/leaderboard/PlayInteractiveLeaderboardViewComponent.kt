package com.tokopedia.play_common.ui.leaderboard

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayInteractiveLeaderboardAdapter
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveLeaderboardViewHolder
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

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    private val leaderboardAdapter = PlayInteractiveLeaderboardAdapter(object : PlayInteractiveLeaderboardViewHolder.Listener{
        override fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int) {
            listener.onChatWinnerButtonClicked(this@PlayInteractiveLeaderboardViewComponent, winner, position)
        }
    })

    init {
        findViewById<TextView>(R.id.tv_sheet_title)
            .setText(R.string.play_interactive_leaderboard_title)

        findViewById<ImageView>(R.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this)
            }

        rvLeaderboard.adapter = leaderboardAdapter

        findViewById<UnifyButton>(R.id.btn_action_leaderboard_error).setOnClickListener {
            listener.onRefreshButtonClicked(this)
        }
    }

    fun setData(leaderboards: List<PlayLeaderboardUiModel>) {
        leaderboardAdapter.setItemsAndAnimateChanges(leaderboards)
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }

        show()
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun showError(shouldShow: Boolean) {
        if (shouldShow) {
            errorView.show()
            rvLeaderboard.hide()
        } else {
            errorView.hide()
            rvLeaderboard.show()
        }
    }

    interface Listener {
        fun onCloseButtonClicked(view: PlayInteractiveLeaderboardViewComponent)
        fun onChatWinnerButtonClicked(
            view: PlayInteractiveLeaderboardViewComponent,
            winner: PlayWinnerUiModel,
            position: Int
        ) {
        }
        fun onRefreshButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        }
    }
}