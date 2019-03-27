package com.tokopedia.navigation.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.RecomTitle

/**
 * Author errysuprayogi on 15,March,2019
 */
class RecomTitleViewHolder(itemView: View) : AbstractViewHolder<RecomTitle>(itemView) {

    private val textView: TextViewCompat

    init {
        textView = itemView.findViewById(R.id.title)
    }

    override fun bind(element: RecomTitle) {
        textView.text = element.title
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_title
    }
}
