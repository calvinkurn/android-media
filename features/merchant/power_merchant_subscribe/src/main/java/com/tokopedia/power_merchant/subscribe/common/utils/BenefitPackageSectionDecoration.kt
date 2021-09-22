package com.tokopedia.power_merchant.subscribe.common.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isZero

class BenefitPackageSectionDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position.isZero()) {
            parent.adapter.also {
                outRect.left =
                    view.resources.getDimensionPixelSize(
                        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3
                    )
            }
        }
    }
}