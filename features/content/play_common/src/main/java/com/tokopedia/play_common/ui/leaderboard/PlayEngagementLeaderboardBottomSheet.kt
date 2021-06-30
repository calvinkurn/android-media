package com.tokopedia.play_common.ui.leaderboard

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayEngagementLeaderboardAdapter
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayEngagementLeaderboardViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify


/**
 * Created by mzennis on 29/06/21.
 */
class PlayEngagementLeaderboardBottomSheet : BottomSheetUnify() {

    private lateinit var rvLeaderboard: RecyclerView

    private val leaderboardAdapter = PlayEngagementLeaderboardAdapter(object : PlayEngagementLeaderboardViewHolder.Listener{
        override fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int) {
            Toast.makeText(requireContext(), "$position - ${winner.name}", Toast.LENGTH_SHORT).show()
        }

    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupObserve()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        setChild(getContentView())
    }

    private fun getContentView(): View {
        val view = View.inflate(requireContext(), R.layout.bottom_sheet_play_engagement_leaderboard, null)
        rvLeaderboard = view.findViewById(R.id.rv_leaderboard)
        return view
    }

    private fun setupView(view: View) {
        setTitle(getString(R.string.play_engage_leaderboard_title))
        isFullpage = false

        rvLeaderboard.adapter = leaderboardAdapter
    }

    private fun setupObserve() {
        leaderboardAdapter.setItems(mockLeaderboardItems)
    }

    private val mockLeaderboardItems = List(3) {
        PlayLeaderboardUiModel(
            title = listOf("Giveaway Kotak Pensil", "Giveaway LCD tv", "Giveaway CD Blackpink").random(),
            winners = if (it == 1) emptyList() else List(5) { child ->
                PlayWinnerUiModel(
                    rank = child + 1,
                    id = "${child + 1}",
                    name = listOf("Nick", "Elon", "Selena", "Suzane", "Eggy").random(),
                    imageUrl = "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/gueo3qthwrv8y5laemzs/joyride-run-flyknit-running-shoe-sqfqGQ.jpg",
                    allowChat = true
                )
            },
            otherParticipantText = if (it == 1) "" else "Dari 100 peserta game"
        )
    }

    companion object {

        private const val TAG = "PlayEngageTimePickerBottomSheet"
    }

}