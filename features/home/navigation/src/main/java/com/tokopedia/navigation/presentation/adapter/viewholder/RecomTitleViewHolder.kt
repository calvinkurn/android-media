package com.tokopedia.navigation.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.RecomTitle
import com.tokopedia.unifyprinciples.Typography

/**
 * Author errysuprayogi on 15,March,2019
 */
class RecomTitleViewHolder(itemView: View) : AbstractViewHolder<RecomTitle>(itemView) {

    private val textView: Typography

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
