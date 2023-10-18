package com.tokopedia.play.view.viewcomponent

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.view.fragment.PlayBottomSheetFragment
import com.tokopedia.play_common.viewcomponent.ViewComponent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by jegul on 05/08/20
 */
class FragmentBottomSheetViewComponent(
    private val channelId: String,
    private val container: ViewGroup,
    @IdRes idRes: Int,
    private val fragmentManager: FragmentManager
) : ViewComponent(container, idRes) {

    private var isAlreadyInit: AtomicBoolean = AtomicBoolean(false)

    fun safeInit() = synchronized(this) {
        if (isAlreadyInit.get()) return@synchronized
        isAlreadyInit.compareAndSet(false, true)

        fragmentManager.findFragmentByTag(BOTTOM_SHEET_FRAGMENT_TAG) ?: getPlayBottomSheetFragment().also {
            fragmentManager.beginTransaction()
                .replace(rootView.id, it, BOTTOM_SHEET_FRAGMENT_TAG)
                .commit()
        }
    }

    fun safeRelease() = synchronized(this) {
        if (!isAlreadyInit.get()) return@synchronized
        isAlreadyInit.compareAndSet(true, false)

        fragmentManager.findFragmentByTag(BOTTOM_SHEET_FRAGMENT_TAG)?.let { fragment ->
            fragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
    }

    private fun getFragment(): PlayBottomSheetFragment? {
        return fragmentManager.findFragmentByTag(BOTTOM_SHEET_FRAGMENT_TAG) as? PlayBottomSheetFragment
    }

    private fun getOrCreateFragment(): PlayBottomSheetFragment {
        val existingFragment = getFragment()
        return if (existingFragment != null) {
            existingFragment
        } else {
            val fragmentFactory = fragmentManager.fragmentFactory
            return fragmentFactory.instantiate(
                container.context.classLoader,
                BOTTOM_SHEET_FRAGMENT_TAG
            ).apply {
                arguments = Bundle().apply {
                    putString(PLAY_KEY_CHANNEL_ID, channelId)
                }
            } as PlayBottomSheetFragment
        }
    }

    private fun getPlayBottomSheetFragment(): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(container.context.classLoader, PlayBottomSheetFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_TAG = "fragment_bottom_sheet"
    }
}
