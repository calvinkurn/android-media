package com.tokopedia.epharmacy.adapters

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class EPharmacyImagesDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return

        outRect.right = view.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    }
}