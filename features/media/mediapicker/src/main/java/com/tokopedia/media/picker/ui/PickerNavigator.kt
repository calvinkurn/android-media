package com.tokopedia.media.picker.ui

import android.content.Context
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.tokopedia.media.common.types.PickerFragmentType
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment

class PickerNavigator constructor(
    private val context: Context,
    @IdRes private val containerId: Int,
    private val fm: FragmentManager,
    factory: PickerFragmentFactory
) {

    private var permissionFragment: Fragment? = null
    private var cameraFragment: Fragment? = null
    private var galleryFragment: Fragment? = null

    @PickerFragmentType
    var currentSelectedPage = PickerFragmentType.NONE

    private val pages = mutableMapOf<Fragment?, String?>()

    init {
        permissionFragment = factory.permissionFragment()
        cameraFragment = factory.cameraFragment()
        galleryFragment = factory.galleryFragment()

        addPage(permissionFragment, context.getString(com.tokopedia.media.R.string.picker_title_permission))
        addPage(cameraFragment, context.getString(com.tokopedia.media.R.string.picker_title_camera))
        addPage(galleryFragment, context.getString(com.tokopedia.media.R.string.picker_title_gallery))
    }

    fun permissionFragment(): PermissionFragment {
        return fragmentOf(
            PickerFragmentType.PERMISSION
        ) as PermissionFragment
    }

    fun cameraFragment(): CameraFragment {
        return fragmentOf(
            PickerFragmentType.CAMERA
        ) as CameraFragment
    }

    fun galleryFragment(): GalleryFragment {
        return fragmentOf(
            PickerFragmentType.GALLERY
        ) as GalleryFragment
    }

    fun start(@PickerFragmentType page: Int) {
        val transaction = fm.beginTransaction()
        val fragment = pageFragment(page)

        if (fm.fragments.isEmpty()) {
            addPage(fragment, transaction)
        }

        fragment?.let {
            showFragment(it, transaction)
            setSelectedPage(page)
        }
    }

    fun cleanUp() {
        val transaction = fm.beginTransaction()
        fm.fragments.forEach {
            if (it.isAdded) {
                transaction.remove(it)
            }
        }
        transaction.commitAllowingStateLoss()
        pages.clear()
    }

    fun onPageSelected(@PickerFragmentType page: Int) {
        if (isActivityResumed() && !isCurrentPageOf(page)) {
            showPage(page)
        }
    }

    private fun fragmentOf(@PickerFragmentType type: Int): Fragment? {
        val fragment = pageFragment(type) ?: return null

        val tag = fragment::class.java.canonicalName
        val fragmentByTag = fm.findFragmentByTag(tag)

        return fragmentByTag ?: fragment
    }

    private fun isCurrentPageOf(@PickerFragmentType page: Int): Boolean {
        val fragment = fragmentOf(page) ?: return false
        val fragmentState = fragment.lifecycle.currentState

        return page == currentSelectedPage && fragmentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    private fun showPage(page: Int) {
        pageFragment(page)?.let {
            val transaction = fm.beginTransaction()
            showFragment(it, transaction)
            setSelectedPage(page)
        }
    }

    private fun addPage(fragment: Fragment?, title: String?) {
        fragment?.let {
            pages[it] = title
        }
    }

    private fun addPage(fragment: Fragment?, transaction: FragmentTransaction) {
        pages.keys.forEach {
            it?.let { _fragment ->
                val tag = _fragment::class.java.canonicalName
                transaction.add(containerId, _fragment, tag)

                if (_fragment != fragment) {
                    transaction.setMaxLifecycle(_fragment, Lifecycle.State.CREATED)
                }
            }
        }
    }

    private fun hideAllFragment(transaction: FragmentTransaction) {
        fm.fragments.forEach { transaction.hide(it) }
    }

    private fun setSelectedPage(@PickerFragmentType page: Int) {
        currentSelectedPage = page
    }

    private fun showFragment(fragment: Fragment, transaction: FragmentTransaction) {
        val tag = fragment::class.java.canonicalName
        val fragmentByTag = fm.findFragmentByTag(tag)
        val selectedFragment = fragmentByTag ?: fragment

        val currentState = selectedFragment.lifecycle.currentState
        val isFragmentNotResumed = !currentState.isAtLeast(Lifecycle.State.RESUMED)

        if (isFragmentNotResumed) {
            try {
                transaction.setMaxLifecycle(selectedFragment, Lifecycle.State.RESUMED)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        hideAllFragment(transaction)

        transaction
            .show(selectedFragment)
            .commit()
    }

    private fun isActivityResumed(): Boolean {
        val state = (context as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.RESUMED || state == Lifecycle.State.STARTED
    }

    private fun pageFragment(@PickerFragmentType page: Int): Fragment? {
        return when(page) {
            PickerFragmentType.PERMISSION -> permissionFragment
            PickerFragmentType.CAMERA -> cameraFragment
            PickerFragmentType.GALLERY -> galleryFragment
            else -> null
        }
    }

}