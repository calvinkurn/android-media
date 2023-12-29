package com.tokopedia.play.broadcaster.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.play.broadcaster.view.fragment.beautification.BeautificationTabFragment

/**
 * Created By : Jonathan Darwin on February 27, 2023
 */
class BeautificationPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val classLoader: ClassLoader,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val pageCount = 2

    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            BeautificationTabFragment.Companion.Type.FaceFilter.value -> {
                BeautificationTabFragment.getFaceFilterFragment(
                    fragmentManager,
                    classLoader
                )
            }
            BeautificationTabFragment.Companion.Type.Preset.value -> {
                BeautificationTabFragment.getPresetFragment(
                    fragmentManager,
                    classLoader
                )
            }
            else -> {
                BeautificationTabFragment.getUnknownFragment(
                    fragmentManager,
                    classLoader
                )
            }
        }
    }
}
