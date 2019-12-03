package com.tokopedia.home_wishlist.view.ext

import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_wishlist.util.SafeClickListener

private fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}

private fun View.addCircleRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, this, true)
    setBackgroundResource(resourceId)
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun RecyclerView.isScrollable(): Boolean{
    return computeHorizontalScrollRange() > width || computeVerticalScrollRange() > height
}