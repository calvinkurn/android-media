package com.tokopedia.media.picker.ui.component

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.R
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerNavigator
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.types.FragmentType

class ParentContainerComponent(
    fragmentManager: FragmentManager,
    fragmentFactory: PickerFragmentFactory,
    parent: ViewGroup
) : UiComponent(parent, R.id.uc_parent_container) {

    private val navigator: PickerNavigator? by lazy {
        PickerNavigator(
            context,
            componentId,
            fragmentManager,
            fragmentFactory
        )
    }

    fun open(@FragmentType type: Int) {
        navigator?.open(type)
    }

    fun cameraFragment(): CameraFragment? {
        return navigator?.cameraFragment()
    }

    fun addBottomNavMargin() {
        val marginBottom = context.resources.getDimensionPixelOffset(R.dimen.picker_page_margin_bottom)
        container().setBottomMargin(marginBottom)
    }

    fun resetBottomNavMargin() {
        container().setBottomMargin(0)
    }

    fun isFragmentActive(@FragmentType fragmentType: Int): Boolean{
        return navigator?.isCurrentPageOf(fragmentType) ?: false
    }

    private fun View.setBottomMargin(value: Int) {
        val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, 0, 0, value)
    }

    override fun release() {}

}