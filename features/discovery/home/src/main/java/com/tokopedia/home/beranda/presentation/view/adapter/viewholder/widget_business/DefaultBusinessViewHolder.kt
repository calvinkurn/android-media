package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemView

class DefaultBusinessViewHolder (
        itemView: View?,
        private val listener: BusinessUnitItemView
) : EmptyViewHolder(itemView) {

    override fun bind(element: EmptyModel?) {

    }

    companion object {
        val LAYOUT: Int = R.layout.layout_template_empty_business_widget
    }

}
