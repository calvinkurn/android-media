package com.tokopedia.play.view.viewcomponent

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play_common.viewcomponent.ViewComponent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by jegul on 05/08/20
 */
class FragmentUserInteractionViewComponent(
        private val channelId: String,
        private val container: ViewGroup,
        @IdRes idRes: Int,
        private val fragmentManager: FragmentManager
) : ViewComponent(container, idRes) {

    private var isAlreadyInit: AtomicBoolean = AtomicBoolean(false)

    fun safeInit() = synchronized(this) {
        if (isAlreadyInit.get()) return@synchronized
        isAlreadyInit.compareAndSet(false, true)

        fragmentManager.findFragmentByTag(USER_INTERACTION_FRAGMENT_TAG) ?: getPlayUserInteractionFragment().also {
            fragmentManager.beginTransaction()
                    .replace(rootView.id, it, USER_INTERACTION_FRAGMENT_TAG)
                    .commit()
        }
    }

    fun setScaledVideoBottomBounds(finalBottomBounds: Int) {
        val fragment = fragmentManager.findFragmentByTag(USER_INTERACTION_FRAGMENT_TAG) as? PlayUserInteractionFragment
        fragment?.maxTopOnChatMode(finalBottomBounds)
    }

    fun startAnimateInsets(isHidingInsets: Boolean) {
        val fragment = fragmentManager.findFragmentByTag(USER_INTERACTION_FRAGMENT_TAG) as? PlayUserInteractionFragment
        fragment?.onStartAnimateInsets(isHidingInsets)
    }

    fun finishAnimateInsets(isHidingInsets: Boolean) {
        val fragment = fragmentManager.findFragmentByTag(USER_INTERACTION_FRAGMENT_TAG) as? PlayUserInteractionFragment
        fragment?.onFinishAnimateInsets(isHidingInsets)
    }

    private fun getPlayUserInteractionFragment(): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(container.context.classLoader, PlayUserInteractionFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    companion object {
        private const val USER_INTERACTION_FRAGMENT_TAG = "fragment_interaction"
    }
}