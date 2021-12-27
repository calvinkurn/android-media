package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.PayLaterWidgetDataModel
import com.tokopedia.product.detail.databinding.PaylaterWidgetBaseViewBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class PayLaterWidgetViewHolder(itemView: View, listener: DynamicProductDetailListener) :
    AbstractViewHolder<PayLaterWidgetDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.paylater_widget_base_view
    }

    private val binding = PaylaterWidgetBaseViewBinding.bind(itemView)

    override fun bind(element: PayLaterWidgetDataModel?) {
        TODO("Not yet implemented")
    }
}