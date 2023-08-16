package com.tokopedia.editor.ui.main.component

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.editor.R
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.main.adapter.EditorPagerStateAdapter
import com.tokopedia.editor.ui.main.fragment.image.main.ImageMainEditorFragment
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile

class PagerContainerUiComponent constructor(
    parent: ViewGroup,
    private val fragment: EditorFragmentProvider,
    private val activity: FragmentActivity
) : UiComponent(parent, R.id.uc_pager_container) {

    private val pager: ViewPager2 = findViewById(R.id.pager_container)
    private var mAdapter: EditorPagerStateAdapter? = null

    fun setupView(param: UniversalEditorParam) {
        mAdapter = EditorPagerStateAdapter(activity)

        with(pager) {
            // Currently the editor only support single selection
            offscreenPageLimit = 1

            // Disable the swipe-able by default
            isUserInputEnabled = false

            adapter = mAdapter
        }

        setFragmentDisplayed(param)
    }

    fun updateView(newPath: String?) {
        newPath?.let {
            mAdapter?.fragments?.first()?.let { fragment ->
                try {
                    (fragment as ImageMainEditorFragment).updateImage(it)
                } catch (_: Exception) {}
            }
        }
    }

    private fun setFragmentDisplayed(param: UniversalEditorParam) {
        // Since current editor only supported single selection, hence we've get the first item of [paths]
        val file = param.firstFile.path ?: return

        // check the file whether image or video type
        val mFragment = if (file.asPickerFile().isVideo().not()) {
            fragment.imageMainEditorFragment()
        } else {
            fragment.videoMainEditorFragment()
        }

        mAdapter?.addSingleFragment(mFragment)
    }

    override fun release() {
        super.release()
        mAdapter = null
    }
}
