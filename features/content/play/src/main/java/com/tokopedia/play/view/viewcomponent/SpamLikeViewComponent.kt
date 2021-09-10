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

    fun shot(
        amount: Int = 1,
        reduceOpacity: Boolean = false
    ) {
        spamLike.shot(
            likeAmount = amount,
            shotPerBatch = SHOT_PER_BATCH,
            delayInMs = SPAMMING_LIKE_DELAY,
            reduceOpacity
        )
    }

    fun shotBurst(
        amount: Int = 1,
        isOpaque: Boolean = false
    ) {
        spamLike.shot(
            likeAmount = amount,
            shotPerBatch = SHOT_PER_BATCH,
            isOpaque = isOpaque
        )
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