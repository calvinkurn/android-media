package com.tokopedia.productcard_compact.productcard.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.productcard_compact.productcard.utils.ViewUtil.getDpFromDimen

internal class ProductCardCompactProductCardLinearDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacing = getDpFromDimen(parent.context, com.tokopedia.unifyprinciples.R.dimen.unify_space_8).toIntSafely()

        outRect.left = spacing
        outRect.top = spacing
        outRect.right = spacing
        outRect.bottom = spacing
    }
}
