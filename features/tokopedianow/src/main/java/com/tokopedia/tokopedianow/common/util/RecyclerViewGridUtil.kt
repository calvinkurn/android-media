package com.tokopedia.tokopedianow.common.util

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.common.decoration.ProductCardGridDecoration
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

object RecyclerViewGridUtil {
    fun RecyclerView.addProductItemDecoration() {
        try {
            val spacing = getDpFromDimen(context, unifyprinciplesR.dimen.unify_space_16)
            addItemDecoration(ProductCardGridDecoration(spacing.toIntSafely()))
        } catch (_: Throwable) { /* nothing to do */ }
    }

    fun RecyclerView.addCategoryProductItemDecoration() {
        try {
            val spacing = getDpFromDimen(context, unifyprinciplesR.dimen.unify_space_16).toIntSafely()
            val topSpacing = getDpFromDimen(context, unifyprinciplesR.dimen.unify_space_0).toIntSafely()
            addItemDecoration(ProductCardGridDecoration(spacing, topSpacing))
        } catch (_: Throwable) { /* nothing to do */ }
    }
}
