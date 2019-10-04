package com.tokopedia.settingnotif.usersetting.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingSectionViewHolder

class NotifSettingDividerDecoration(context: Context?) : RecyclerView.ItemDecoration() {

    private var divider: Drawable? = null
    private var dividerHeight: Int = 0
    private var dividerLeftPadding: Int = 0

    init {
        context?.let {
            divider = ContextCompat.getDrawable(it, R.drawable.bg_line_separator_thin)
            dividerHeight = context.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_half)
            dividerLeftPadding = context.resources.getDimensionPixelSize(R.dimen.dp_56)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val parentPaddingLeft = parent.paddingLeft
        val parentPaddingRight = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (childIndex in 0 until childCount) {
            val childView = parent.getChildAt(childIndex)
            val childViewHolder = parent.getChildViewHolder(childView)

            if (childViewHolder is SettingSectionViewHolder) continue

            val childParams = childView.layoutParams as RecyclerView.LayoutParams

            val left = parentPaddingLeft + dividerLeftPadding
            val right = parentPaddingRight
            val top = childView.bottom + childParams.bottomMargin
            val bottom = top + dividerHeight

            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

}