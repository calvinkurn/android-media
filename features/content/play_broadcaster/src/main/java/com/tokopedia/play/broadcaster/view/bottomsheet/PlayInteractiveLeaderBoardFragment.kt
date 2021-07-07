package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.PlayInteractiveLeaderboardViewComponent
import com.tokopedia.play_common.viewcomponent.viewComponent


/**
 * Created by mzennis on 06/07/21.
 */
class PlayInteractiveLeaderBoardFragment : TkpdBaseV4Fragment(), PlayInteractiveLeaderboardViewComponent.Listener {

    private val leaderboardSheetView by viewComponent { PlayInteractiveLeaderboardViewComponent(it, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getScreenName(): String = TAG

    override fun onCloseButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {

    }

    override fun onChatWinnerButtonClicked(
        view: PlayInteractiveLeaderboardViewComponent,
        winner: PlayWinnerUiModel,
        position: Int
    ) {

    }

    companion object {
        private const val TAG = "PlayInteractiveLeaderBoardBottomSheet"
    }
}