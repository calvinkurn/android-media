package com.tokopedia.smartbills.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeBillsModel
import com.tokopedia.smartbills.data.Section
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory

class DividerSBMItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var dimenPaddingLeft = R.dimen.smart_bills_divider_left_padding
    private var dimenPaddingLeftAccordion = com.tokopedia.unifyprinciples.R.dimen.layout_lvl0
    lateinit var mDivider: Drawable

    init {
        initDivider(context)
    }

    fun initDivider(context: Context){
        mDivider = context.getResources().getDrawable(com.tokopedia.abstraction.R.drawable.bg_line_separator_thin)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawVertical(c, parent)
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        var left =  parent.context.resources.getDimensionPixelOffset(dimenPaddingLeft)
        val right = parent.width - parent.paddingRight

        val listItem = (parent.adapter as BaseListAdapter<RechargeBillsModel, SmartBillsAdapterFactory>).data
        for (i in 0 until listItem.size - 1) {
            if (!listItem.isNullOrEmpty()) {
                val item = (parent.adapter as BaseListAdapter<RechargeBillsModel, SmartBillsAdapterFactory>).data.get(i)
                if (item is Section) left = parent.context.resources.getDimensionPixelOffset(dimenPaddingLeftAccordion)
            }
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + parent.context.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_half)
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}