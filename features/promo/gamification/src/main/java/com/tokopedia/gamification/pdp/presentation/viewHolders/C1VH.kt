package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.data.C1VHModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class C1VH(itemView: View) : AbstractViewHolder<C1VHModel>(itemView) {

    private val tvDate = itemView.findViewById<Typography>(gamificationR.id.ketupat_text)

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_top_banner
    }

    override fun bind(element: C1VHModel?) {
        tvDate.text = buildString {
            append("element?.data?.title")
            append(" (")
            append("element?.data?.message")
            append(")")
        }
    }

}
