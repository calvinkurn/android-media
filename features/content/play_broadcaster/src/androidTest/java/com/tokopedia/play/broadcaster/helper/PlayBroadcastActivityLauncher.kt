package com.tokopedia.play.broadcaster.helper

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
class PlayBroadcastActivityLauncher(
    private val context: Context,
) {

    fun launch() {
        ActivityScenario.launch<PlayBroadcastActivity>(
            Intent(context, PlayBroadcastActivity::class.java)
        ).moveToState(Lifecycle.State.RESUMED)
    }
}
