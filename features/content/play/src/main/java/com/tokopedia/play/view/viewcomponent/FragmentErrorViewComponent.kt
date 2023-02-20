package com.tokopedia.play.view.viewcomponent

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.view.fragment.PlayErrorFragment
import com.tokopedia.play_common.viewcomponent.ViewComponent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by jegul on 05/08/20
 */
class FragmentErrorViewComponent(
        private val channelId: String,
        private val container: ViewGroup,
        @IdRes idRes: Int,
        private val fragmentManager: FragmentManager
) : ViewComponent(container, idRes) {

    private var isAlreadyInit: AtomicBoolean = AtomicBoolean(false)

    val activeFragment: PlayErrorFragment? by lazy (LazyThreadSafetyMode.NONE) {
        fragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG) as? PlayErrorFragment
    }

    fun safeInit() = synchronized(this) {
        if (isAlreadyInit.get()) return@synchronized
        isAlreadyInit.compareAndSet(false, true)

        fragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG) ?: getPlayErrorFragment().also {
            fragmentManager.beginTransaction()
                    .replace(rootView.id, it, ERROR_FRAGMENT_TAG)
                    .commit()
        }
    }

    private fun getPlayErrorFragment(): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(container.context.classLoader, PlayErrorFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    companion object {
        private const val ERROR_FRAGMENT_TAG = "fragment_error"
    }
}
