package com.tokopedia.tokopedianow.test.common.productcard.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.test.R
import com.tokopedia.tokopedianow.test.utils.ViewUtil

internal class TokoNowProductCardItemLinearDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacing = ViewUtil.getDpFromDimen(
            context = parent.context,
            R.dimen.tokopedianow_product_card_item_padding
        ).toInt()

        outRect.left = spacing
        outRect.top = spacing
        outRect.right = spacing
        outRect.bottom = spacing
    }
}
