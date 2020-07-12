package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.product.manage.R

class CampaignStockEventInfoDividerDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val divider = ContextCompat.getDrawable(context, R.drawable.divider_campaign_stock)

    // This variable is depended on the availability of ticker model on list. Change this if the ticker is unavailable
    private val startDecoratingIndex = 2

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.childCount >= startDecoratingIndex)
        for (index in startDecoratingIndex until parent.childCount) {
            val left = 0
            val right = parent.width
            val top = parent.getChildAt(index).top
            val bottom = top + divider?.intrinsicHeight.toZeroIfNull()
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }
}