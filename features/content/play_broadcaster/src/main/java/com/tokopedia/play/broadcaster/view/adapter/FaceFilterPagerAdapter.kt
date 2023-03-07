package com.tokopedia.play.broadcaster.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.play.broadcaster.view.fragment.facefilter.PlayBroMakeupTabFragment

/**
 * Created By : Jonathan Darwin on February 27, 2023
 */
class FaceFilterPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val classLoader: ClassLoader,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val pageCount = 2

    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            PlayBroMakeupTabFragment.Companion.Type.FaceFilter.value -> {
                PlayBroMakeupTabFragment.getFaceFilterFragment(
                    fragmentManager,
                    classLoader
                )
            }
            PlayBroMakeupTabFragment.Companion.Type.Preset.value -> {
                PlayBroMakeupTabFragment.getPresetFragment(
                    fragmentManager,
                    classLoader
                )
            }
            else -> {
                PlayBroMakeupTabFragment.getUnknownFragment(
                    fragmentManager,
                    classLoader
                )
            }
        }
    }
}
