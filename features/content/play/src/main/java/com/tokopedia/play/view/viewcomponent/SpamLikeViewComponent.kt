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
        CoroutineScope(Dispatchers.IO + job).launch {
            for(i in 1..amount * LIKE_ICON_MULTIPLIER) {
                delay(100)
                withContext(Dispatchers.Main) {
                    spamLike.shot(reduceOpacity)
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        job.cancel()
        spamLike.clear()
    }

    companion object {
        private const val LIKE_ICON_MULTIPLIER = 3
    }
}