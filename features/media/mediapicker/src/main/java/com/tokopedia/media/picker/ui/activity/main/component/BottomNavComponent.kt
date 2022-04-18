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

            tabUnify.tabLayout.addOnTabSelected(
                ::onTabSelectionChanged
            )
        }
    }

    fun onStartPositionChanged(startPosition: Int) {
        var position = startPosition
        val tabCount = tabUnify.tabLayout.tabCount

        // force the position to zero if the value more than tab count
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
            listener.onCameraTabSelected(isTabClicked)
        } else if (position == PAGE_GALLERY_INDEX) {
            listener.onGalleryTabSelected(isTabClicked)
        }
        isTabClicked = false
    }

    private fun addTab(id: Int) {
        tabUnify.addNewTab(context.getString(id)).view.setOnClickListener {
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
        private const val PAGE_CAMERA_INDEX = 0
        private const val PAGE_GALLERY_INDEX = 1
    }

}