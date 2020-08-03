package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.product.manage.R

class CampaignStockVariantDividerDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val divider = ContextCompat.getDrawable(context, R.drawable.divider_campaign_stock)

    private val dividerMarginLeft = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount) {
            var left = dividerMarginLeft.toInt()
            val right = parent.width
            val top = parent.getChildAt(index).top
            val bottom = top + divider?.intrinsicHeight.toZeroIfNull()
            if (index == 0) {
                left = 0
            }
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

}