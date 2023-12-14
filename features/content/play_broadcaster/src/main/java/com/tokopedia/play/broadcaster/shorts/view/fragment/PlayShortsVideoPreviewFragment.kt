package com.tokopedia.play.broadcaster.shorts.view.fragment

import androidx.fragment.app.FragmentManager
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment

/**
 * Created By : Jonathan Darwin on December 14, 2023
 */
class PlayShortsVideoPreviewFragment : PlayShortsBaseFragment() {

    var videoUri: String = ""

    override fun getScreenName(): String = TAG

    companion object {
        const val TAG = "PlayShortsVideoPreviewFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayShortsVideoPreviewFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayShortsVideoPreviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayShortsVideoPreviewFragment::class.java.name
            ) as PlayShortsVideoPreviewFragment
        }
    }
}
