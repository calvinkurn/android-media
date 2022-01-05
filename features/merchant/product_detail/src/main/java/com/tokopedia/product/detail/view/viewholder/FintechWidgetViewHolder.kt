package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel

class FintechWidgetViewHolder(itemView: View):
    AbstractViewHolder<FintechWidgetDataModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.fintech_widget_layout
    }



    fun update()
    {

    }

    override fun bind(element: FintechWidgetDataModel?) {

    }
}