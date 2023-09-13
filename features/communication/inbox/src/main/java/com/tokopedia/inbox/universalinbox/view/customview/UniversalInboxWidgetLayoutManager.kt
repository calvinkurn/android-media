package com.tokopedia.inbox.universalinbox.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil.WIDGET_RATIO_HALF
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel

class UniversalInboxWidgetLayoutManager(
    context: Context?,
    orientation: Int,
    reverseLayout: Boolean
) : LinearLayoutManager(context, orientation, reverseLayout) {


    override fun generateDefaultLayoutParams() =
        scaledLayoutParams(super.generateDefaultLayoutParams())

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?) =
        scaledLayoutParams(super.generateLayoutParams(lp))

    override fun generateLayoutParams(c: Context?, attrs: AttributeSet?) =
        scaledLayoutParams(super.generateLayoutParams(c, attrs))

    private fun scaledLayoutParams(layoutParams: RecyclerView.LayoutParams) =
        layoutParams.apply {
            when (orientation) {
                RecyclerView.HORIZONTAL -> {
                    width = (horizontalSpace * WIDGET_RATIO_HALF).toInt()
                }
                RecyclerView.VERTICAL -> {
                    height = verticalSpace
                }
            }
        }

    private val horizontalSpace get() = width - paddingStart - paddingEnd
    private val verticalSpace get() = height - paddingTop - paddingBottom

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
    }

    override fun canScrollHorizontally(): Boolean = true
    override fun canScrollVertically(): Boolean = false
}
