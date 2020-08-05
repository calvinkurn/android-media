package com.tokopedia.play.view.viewcomponent

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.view.fragment.PlayBottomSheetFragment
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 05/08/20
 */
class FragmentBottomSheetViewComponent(
        private val channelId: String,
        private val container: ViewGroup,
        @IdRes idRes: Int,
        private val fragmentManager: FragmentManager
) : ViewComponent(container, idRes) {

    internal fun init() {
        fragmentManager.findFragmentByTag(BOTTOM_SHEET_FRAGMENT_TAG) ?: getPlayBottomSheetFragment().also {
            fragmentManager.beginTransaction()
                    .replace(rootView.id, it, BOTTOM_SHEET_FRAGMENT_TAG)
                    .commit()
        }
    }

    internal fun release() {
        fragmentManager.findFragmentByTag(BOTTOM_SHEET_FRAGMENT_TAG)?.let { fragment ->
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
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