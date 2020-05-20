package com.tokopedia.play.broadcaster.view

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.play.broadcaster.R


/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_broadcaster)
    }
}