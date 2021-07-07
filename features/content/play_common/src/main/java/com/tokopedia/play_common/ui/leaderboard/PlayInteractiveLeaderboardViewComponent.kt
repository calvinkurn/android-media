package com.tokopedia.play_common.ui.leaderboard

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayInteractiveLeaderboardAdapter
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveLeaderboardViewHolder
import com.tokopedia.play_common.viewcomponent.ViewComponent


/**
 * Created by mzennis on 29/06/21.
 */
class PlayInteractiveLeaderboardViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.cl_leaderboard_sheet) {

    private val rvLeaderboard: RecyclerView = findViewById(R.id.rv_leaderboard)

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    private val leaderboardAdapter = PlayInteractiveLeaderboardAdapter(object : PlayInteractiveLeaderboardViewHolder.Listener{
        override fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int) {
            listener.onChatWinnerButtonClicked(this@PlayInteractiveLeaderboardViewComponent, winner, position)
        }
    })

    private val mockLeaderboardItems = List(5) {
        PlayLeaderboardUiModel(
            title = listOf("Giveaway Kotak Pensil", "Giveaway LCD tv", "Giveaway CD Blackpink").random(),
            winners = if (it%2 == 1) emptyList() else List(5) { child ->
                PlayWinnerUiModel(
                    rank = it + child + 1,
                    id = "${child + 1}",
                    name = listOf("Nick", "Elon", "Selena", "Suzane", "Eggy").random(),
                    imageUrl = "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/gueo3qthwrv8y5laemzs/joyride-run-flyknit-running-shoe-sqfqGQ.jpg",
                    allowChat = true
                )
            },
            otherParticipantText =  if (it%2 == 1) "" else "Dari 100 peserta game"
        )
    }

    init {
        findViewById<TextView>(R.id.tv_sheet_title)
            .setText(R.string.play_interactive_leaderboard_title)

        findViewById<ImageView>(R.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this)
            }

        rvLeaderboard.adapter = leaderboardAdapter

        setData(mockLeaderboardItems)
    }

    fun setData(leaderboards: List<PlayLeaderboardUiModel>) {
        leaderboardAdapter.setItems(leaderboards)
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

    interface Listener {
        fun onCloseButtonClicked(view: PlayInteractiveLeaderboardViewComponent)
        fun onChatWinnerButtonClicked(
            view: PlayInteractiveLeaderboardViewComponent,
            winner: PlayWinnerUiModel,
            position: Int
        ) {
        }
    }
}