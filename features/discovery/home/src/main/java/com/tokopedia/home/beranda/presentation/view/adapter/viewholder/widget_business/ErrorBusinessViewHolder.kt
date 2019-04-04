package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemView
import kotlinx.android.synthetic.main.layout_template_error_business_widget.view.*

class ErrorBusinessViewHolder (itemView: View?,
                               private val listener: BusinessUnitItemView)
    : ErrorNetworkViewHolder(itemView) {

    override fun bind(errorNetworkModel: ErrorNetworkModel?) {
        itemView.buttonReload.setOnClickListener {
            listener.onReloadButtonClick()
        }
    }

    companion object {
        val LAYOUT: Int = R.layout.layout_template_error_business_widget
    }

}