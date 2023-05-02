package com.tokopedia.media.picker.ui.component

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.media.R
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.adapter.PickerFragmentStateAdapter
import com.tokopedia.media.picker.ui.adapter.decoration.PickerFragmentDecoration
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.cache.PickerCacheManager

class PagerContainerUiComponent constructor(
    activity: FragmentActivity,
    parent: ViewGroup,
    param: PickerCacheManager,
    private val factory: PickerFragmentFactory
) : UiComponent(parent, R.id.uc_vp_container) {

    private val viewPager = container() as ViewPager2
    private val mAdapter = PickerFragmentStateAdapter(activity)

    init {
        with(viewPager) {
            addItemDecoration(
                PickerFragmentDecoration(
                    context,
                    param.get().pageType()
                )
            )

            offscreenPageLimit = 2 // camera & gallery
            isUserInputEnabled = false // disabled swipes
            adapter = mAdapter
        }
    }

    fun cameraFragment(): CameraFragment? {
        return getCurrentFragment() as? CameraFragment?
    }

    fun isGalleryFragmentActive(): Boolean {
        return getCurrentFragment() is GalleryFragment
    }

    fun setupCommonPage() {
        mAdapter.addFragments(
            listOf(
                factory.cameraFragment(),
                factory.galleryFragment()
            )
        )
    }

    fun setupCameraPage() {
        mAdapter.addSingleFragment(
            factory.cameraFragment()
        )
    }

    fun setupGalleryPage() {
        mAdapter.addSingleFragment(
            factory.galleryFragment()
        )
    }

    fun setupPermissionPage() {
        mAdapter.addSingleFragment(
            factory.permissionFragment()
        )
    }

    fun navigateToCameraPage() {
        viewPager.currentItem = 0
    }

    fun navigateToGalleryPage() {
        viewPager.currentItem = 1
    }

    private fun getCurrentFragment(): Fragment {
        return mAdapter.fragments[viewPager.currentItem]
    }
}
