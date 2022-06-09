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
class VpsWidgetSpacingItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private val SPACING_HORIZONTAL = 8f.toDpInt()
        private val SPACING_VERTICAL = 12f.toDpInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val context = parent.context
        val position = parent.getChildAdapterPosition(view)
        val spanCount = VpsWidgetTabletConfiguration.getSpanCount(context)

        outRect.top = SPACING_VERTICAL/2
        outRect.left = SPACING_HORIZONTAL/2
        outRect.right = SPACING_HORIZONTAL/2
        if(!DeviceScreenInfo.isTablet(context) && position < spanCount){
            outRect.bottom = SPACING_VERTICAL/2
        }
    }
}