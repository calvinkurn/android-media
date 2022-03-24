package com.tokopedia.media.picker.ui.activity.main.component

import android.graphics.Color
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.picker.utils.addOnTabSelected
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.unifycomponents.TabsUnify

class BottomNavComponent(
    private val listener: Listener,
    parent: ViewGroup
) : UiComponent(parent, R.id.uc_parent_bottom_nav) {

    private val tabUnify = findViewById<TabsUnify>(R.id.tab_page)

    private val tabCount by lazy {
        tabUnify.tabLayout.tabCount
    }

    fun setupView() {
        container().show()
        transparentBackground()

        if (tabCount.isMoreThanZero()) return

        addTab(R.string.picker_title_camera)
        addTab(R.string.picker_title_gallery)

        tabUnify.tabLayout.addOnTabSelected(::onTabSelectionChanged)
    }

    fun onStartPositionChanged(startPosition: Int) {
        var position = startPosition

        if (startPosition > tabCount) {
            position = 0
        }

        tabUnify.tabLayout.getTabAt(position)?.select()
        onTabSelectionChanged(position)
    }

    fun navigateToCameraTab() {
        tabUnify.tabLayout.getTabAt(PAGE_CAMERA_INDEX)?.select()
    }

    private fun transparentBackground() {
        tabUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun onTabSelectionChanged(position: Int) {
        if (position == PAGE_CAMERA_INDEX) {
            listener.onCameraTabSelected()
        } else if (position == PAGE_GALLERY_INDEX) {
            listener.onGalleryTabSelected()
        }
    }

    private fun addTab(id: Int) {
        tabUnify.addNewTab(context.getString(id))
    }

    override fun release() {}

    interface Listener {
        fun onCameraTabSelected()
        fun onGalleryTabSelected()
    }

    companion object {
        private const val PAGE_CAMERA_INDEX = 0
        private const val PAGE_GALLERY_INDEX = 1
    }

}