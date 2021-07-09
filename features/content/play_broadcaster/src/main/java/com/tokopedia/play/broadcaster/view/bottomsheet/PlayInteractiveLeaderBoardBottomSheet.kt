package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.PlayInteractiveLeaderboardViewComponent
import com.tokopedia.play_common.viewcomponent.viewComponent
import javax.inject.Inject


/**
 * Created by mzennis on 06/07/21.
 */
class PlayInteractiveLeaderBoardBottomSheet @Inject constructor() : DialogFragment(), PlayInteractiveLeaderboardViewComponent.Listener {

    private val leaderboardView by viewComponent { PlayInteractiveLeaderboardViewComponent(it, this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.bottom_sheet_play_interactive_leaderboard,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
    }

    private fun setupView(view: View) {
        leaderboardView.show()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    override fun onCloseButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        dismiss()
    }

    override fun onChatWinnerButtonClicked(
        view: PlayInteractiveLeaderboardViewComponent,
        winner: PlayWinnerUiModel,
        position: Int
    ) {
        // TODO goto topchat
    }

    companion object {
        private const val TAG = "PlayInteractiveLeaderBoardBottomSheet"
    }
}