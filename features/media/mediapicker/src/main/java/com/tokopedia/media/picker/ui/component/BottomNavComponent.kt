package com.tokopedia.media.picker.ui.component

import android.graphics.Color
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.unifycomponents.TabsUnify

class BottomNavComponent(
    private val listener: Listener,
    parent: ViewGroup
) : UiComponent(parent, R.id.uc_parent_bottom_nav) {

    private val tabUnify = findViewById<TabsUnify>(R.id.tab_page)

    // to differentiate between tab changes by user click / by .select()
    private var isTabClicked = false

    fun setupView() {
        container().show()

        /**
         * because we set the first page is Camera, we have to set
         * the tabLayout as [Color.TRANSPARENT] background
         */
        transparentBackground()

        if (tabUnify.tabLayout.tabCount == 0) {
            addTab(R.string.picker_title_camera)
            addTab(R.string.picker_title_gallery)

            tabUnify.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        onTabSelectionChanged(it)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {} //no-op
                override fun onTabReselected(tab: TabLayout.Tab?) {} //no-op
            })
        }
    }

    fun navigateToIndexOf(index: Int) {
        val tabSize = tabUnify.tabLayout.tabCount - 1
        var positionIndex = index

        if (index > tabSize) {
            positionIndex = 0
        }

        tabUnify.tabLayout.getTabAt(positionIndex)?.select()
    }

    fun navigateToCameraTab() {
        navigateToIndexOf(PAGE_CAMERA_INDEX)
    }

    private fun transparentBackground() {
        tabUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun onTabSelectionChanged(position: Int) {
        if (position == PAGE_CAMERA_INDEX) {
            listener.onCameraTabSelected(isTabClicked)
        } else if (position == PAGE_GALLERY_INDEX) {
            listener.onGalleryTabSelected(isTabClicked)
        }
        isTabClicked = false
    }

    private fun addTab(id: Int) {
        tabUnify.addNewTab(context.getString(id), false).view.setOnClickListener {
            isTabClicked = true
        }
    }

    override fun release() {}

    interface Listener {
        // isDirectClick is used as flag if tab changes is caused by direct tab click / not
        fun onCameraTabSelected(isDirectClick: Boolean)
        fun onGalleryTabSelected(isDirectClick: Boolean)
    }

    companion object {
        const val PAGE_CAMERA_INDEX = 0
        const val PAGE_GALLERY_INDEX = 1
    }

}