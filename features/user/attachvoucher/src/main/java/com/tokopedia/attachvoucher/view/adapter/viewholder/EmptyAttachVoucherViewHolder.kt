package com.tokopedia.attachvoucher.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.attachvoucher.R
import kotlinx.android.synthetic.main.item_attachvoucher_empty.view.*

class EmptyAttachVoucherViewHolder(itemView: View?) : AbstractViewHolder<EmptyModel>(itemView) {

    private val shopVoucherUrl = "https://www.tokopedia.com/v1/vouchertoko"

    override fun bind(element: EmptyModel?) {
        bindButtonClick()
    }

    private fun bindButtonClick() {
        itemView.btnCreateVoucher?.setOnClickListener {
            RouteManager.route(it.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, shopVoucherUrl))
        }
    }

    companion object {
        val LAYOUT = R.layout.item_attachvoucher_empty
    }
}