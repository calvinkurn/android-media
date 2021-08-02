package com.tokopedia.play.view.viewcomponent

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.R
import com.tokopedia.play.animation.spamlike.PlaySpamLikeAnimation
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */
class SpamLikeViewComponent(
    container: ViewGroup,
    @IdRes parentView: Int,
    @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val spamLike = findViewById<PlaySpamLikeAnimation>(R.id.spam_like_animation)

    init {
        spamLike.setParentView(findViewById(parentView))
    }

    fun shot() {
        try {
            spamLike.shot()
        }
        catch (e: Exception) {
            Log.d("<ERR>", e.message)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        spamLike.clear()
    }

    interface Listener {

    }
}