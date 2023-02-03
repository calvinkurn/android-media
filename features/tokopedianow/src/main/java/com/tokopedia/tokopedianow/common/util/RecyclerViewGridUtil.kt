package com.tokopedia.tokopedianow.common.util

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.common.decoration.ProductCardGridDecoration
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen

object RecyclerViewGridUtil {
    fun RecyclerView.addProductItemDecoration() {
        try {
            val spacing = getDpFromDimen(context, com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
            addItemDecoration(ProductCardGridDecoration(spacing.toIntSafely()))
        } catch (_: Throwable) { /* nothing to do */ }
    }
}
