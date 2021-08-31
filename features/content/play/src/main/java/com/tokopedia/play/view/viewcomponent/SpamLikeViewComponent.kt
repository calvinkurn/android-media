package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.R
import com.tokopedia.play.animation.spamlike.PlaySpamLikeView
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */
class SpamLikeViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val job = SupervisorJob()
    private val spamLike = findViewById<PlaySpamLikeView>(R.id.spam_like_animation)

    init {
        spamLike.setParentView(container)
    }

    val maxShot: Long
        get() = spamLike.getMaxShot().toLong()

    fun shot(amount: Int = 1, reduceOpacity: Boolean = false) {
        if(amount == spamLike.getMaxShot()) spamLike.shotAll(amount, SHOT_PER_BATCH, reduceOpacity)
        else spamLike.shotWithDelay(amount, SHOT_PER_BATCH, SPAMMING_LIKE_DELAY, reduceOpacity)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        job.cancelChildren()
        spamLike.clear(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        job.cancel()
        spamLike.clear(false)
    }

    private companion object {
        const val SHOT_PER_BATCH = 3
        const val SPAMMING_LIKE_DELAY = 200L
    }
}