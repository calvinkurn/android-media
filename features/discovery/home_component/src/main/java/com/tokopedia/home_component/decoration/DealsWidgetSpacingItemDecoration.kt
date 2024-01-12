package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.util.VpsWidgetTabletConfiguration
import com.tokopedia.home_component.util.toDpInt

/**
 * created by frenzel
 */
class DealsWidgetSpacingItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private val SPACING_HORIZONTAL = 4f.toDpInt()
        private val SPACING_VERTICAL = 2f.toDpInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val context = parent.context
        val position = parent.getChildAdapterPosition(view)
        val spanCount = VpsWidgetTabletConfiguration.getSpanCount(context)

        if (position % spanCount == 0) {
            outRect.right = SPACING_HORIZONTAL / 2
        } else {
            outRect.left = SPACING_HORIZONTAL / 2
        }
        if (!DeviceScreenInfo.isTablet(context) && position < spanCount) {
            outRect.top = 0
            outRect.bottom = SPACING_VERTICAL
        } else {
            outRect.top = SPACING_VERTICAL
            outRect.bottom = 0
        }
    }
}
