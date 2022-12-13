package com.tokopedia.play.broadcaster.shorts.helper

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.tokopedia.play.broadcaster.shorts.container.PlayShortsTestActivity

/**
 * Created By : Jonathan Darwin on December 13, 2022
 */
class PlayShortsLauncher(
    private val context: Context
) {

    fun launchActivity(initialMock: () -> Unit = {}) {
        initialMock()

        val scenario = ActivityScenario.launch<PlayShortsTestActivity>(
            Intent(context, PlayShortsTestActivity::class.java)
        )

        scenario.moveToState(Lifecycle.State.RESUMED)
    }
}
