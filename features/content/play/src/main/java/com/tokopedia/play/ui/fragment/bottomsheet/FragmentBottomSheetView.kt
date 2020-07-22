package com.tokopedia.play.ui.fragment.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.fragment.PlayBottomSheetFragment

/**
 * Created by jegul on 05/05/20
 */
class FragmentBottomSheetView(
        private val channelId: String,
        container: ViewGroup,
        private val fragmentManager: FragmentManager
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_fragment_bottom_sheet, container, true)
                    .findViewById(R.id.fl_bottom_sheet)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun init() {
        fragmentManager.findFragmentByTag(BOTTOM_SHEET_FRAGMENT_TAG) ?: PlayBottomSheetFragment.newInstance(channelId).also {
            fragmentManager.beginTransaction()
                    .replace(view.id, it, BOTTOM_SHEET_FRAGMENT_TAG)
                    .commit()
        }
    }

    internal fun release() {
        fragmentManager.findFragmentByTag(BOTTOM_SHEET_FRAGMENT_TAG)?.let { fragment ->
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
        }
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_TAG = "fragment_bottom_sheet"
    }
}