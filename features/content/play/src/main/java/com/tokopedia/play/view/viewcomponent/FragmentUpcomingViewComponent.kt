package com.tokopedia.play.view.viewcomponent

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.view.fragment.PlayUpcomingFragment
import com.tokopedia.play_common.viewcomponent.ViewComponent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created By : Jonathan Darwin on September 06, 2021
 */
class FragmentUpcomingViewComponent(
    private val channelId: String,
    private val container: ViewGroup,
    @IdRes idRes: Int,
    private val fragmentManager: FragmentManager
): ViewComponent(container, idRes) {

    private var isAlreadyInit: AtomicBoolean = AtomicBoolean(false)

    fun safeInit() = synchronized(this) {
        if (isAlreadyInit.get()) return@synchronized
        isAlreadyInit.compareAndSet(false, true)

        fragmentManager.findFragmentByTag(UPCOMING_FRAGMENT_TAG) ?: getPlayUpcomingFragment().also {
            fragmentManager.beginTransaction()
                .replace(rootView.id, it, UPCOMING_FRAGMENT_TAG)
                .commit()
        }
    }

    private fun getPlayUpcomingFragment(): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(container.context.classLoader, PlayUpcomingFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    companion object {
        private const val UPCOMING_FRAGMENT_TAG = "fragment_upcoming"
    }
}