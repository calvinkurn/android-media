package com.tokopedia.play.broadcaster.view

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.broadcaster.view.PlayBroadcastActivity.Companion.PLAY_KEY_CHANNEL_ID


/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastFragment: BaseDaggerFragment() {

    companion object {

        fun newInstance(channelId: String?): PlayBroadcastFragment {
            return PlayBroadcastFragment().apply {
                val args = Bundle()
                args.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = args
            }
        }
    }

    override fun getScreenName(): String = "Play Broadcast"

    override fun initInjector() {

    }
}