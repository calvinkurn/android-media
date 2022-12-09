package com.tokopedia.tokopedianow.recipedetail.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.setMargin

class RecipeInfoTagDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val resources = view.context.resources
        val marginZero = resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_0
        )
        val marginEnd = resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3
        )
        view.setMargin(marginZero, marginZero, marginEnd, marginZero)
    }
}