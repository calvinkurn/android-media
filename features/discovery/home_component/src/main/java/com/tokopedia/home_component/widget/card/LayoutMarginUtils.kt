package com.tokopedia.home_component.widget.card

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart

sealed class MarginArea {
    class Start(val value: Int) : MarginArea()
    class Top(val value: Int) : MarginArea()
    class End(val value: Int) : MarginArea()
    class All(
        val start: Int,
        val top: Int,
        val end: Int,
        val bottom: Int,
    ) : MarginArea()
}

fun View.setCustomMargin(area: MarginArea) {
    layoutParams = layoutParams.apply {
        val marginLayoutParams = this as? ViewGroup.MarginLayoutParams
        when (area) {
            is MarginArea.Start -> marginLayoutParams?.marginStart = area.value
            is MarginArea.Top -> marginLayoutParams?.setMargins(
                marginStart,
                area.value,
                marginEnd,
                marginBottom
            )
            is MarginArea.End -> marginLayoutParams?.marginEnd = area.value
            is MarginArea.All -> marginLayoutParams?.setMargins(
                area.start,
                area.top,
                area.end,
                area.bottom
            )
        }
    }
}
