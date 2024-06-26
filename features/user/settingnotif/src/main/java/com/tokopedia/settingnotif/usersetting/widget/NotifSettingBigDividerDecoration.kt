package com.tokopedia.settingnotif.usersetting.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.ChangeItemViewHolder
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SellerSectionViewHolder
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingSectionViewHolder

class NotifSettingBigDividerDecoration(context: Context?) : RecyclerView.ItemDecoration() {

    private var divider: Drawable? = null
    private var dividerHeight: Int = 0

    init {
        context?.let {
            divider = ContextCompat.getDrawable(context, R.drawable.setting_notif_bg_line_separator_big)
            dividerHeight = context.resources.getDimensionPixelSize(R.dimen.setting_notif_dp_12)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val childViewHolder = parent.getChildViewHolder(view)
        val childPosition = parent.getChildAdapterPosition(view)

        if (childViewHolder is SettingSectionViewHolder && childPosition != 0) {
            outRect.top = dividerHeight
        } else {
            outRect.setEmpty()
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (childIndex in 0 until childCount) {
            val childView = parent.getChildAt(childIndex)
            val childViewHolder = parent.getChildViewHolder(childView)
            val nextChildPosition = childIndex + 1

            if (nextChildPosition >= childCount) continue
            if (childViewHolder is SellerSectionViewHolder) continue
            if (childViewHolder is ChangeItemViewHolder) continue

            val nextChildView = parent.getChildAt(nextChildPosition)
            val nextChildViewHolder = parent.getChildViewHolder(nextChildView)
            if (nextChildViewHolder is SettingSectionViewHolder) {
                drawBigDivider(c, parent, childView)
            }
        }
    }

    private fun drawBigDivider(c: Canvas, parent: RecyclerView, childView: View) {
        val childParams = childView.layoutParams as RecyclerView.LayoutParams

        val left = 0
        val right = parent.width - parent.paddingRight
        val top = childView.bottom + childParams.bottomMargin
        val bottom = top + dividerHeight

        divider?.setBounds(left, top, right, bottom)
        divider?.draw(c)
    }

}