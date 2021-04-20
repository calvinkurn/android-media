package com.tokopedia.attachvoucher.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.attachvoucher.R
import kotlinx.android.synthetic.main.item_attachvoucher_empty.view.*

class EmptyAttachVoucherViewHolder(itemView: View?) : AbstractViewHolder<EmptyModel>(itemView) {

    private val emptyIconUrl = "https://ecs7.tokopedia.net/android/others/Voucher_Ilustration@3x.png"

    override fun bind(element: EmptyModel?) {
        bindEmptyImage()
        bindButtonClick()
    }

    private fun bindEmptyImage() {
        ImageHandler.loadImageAndCache(itemView.ivEmpty, emptyIconUrl)
    }

    private fun bindButtonClick() {
        itemView.btnCreateVoucher?.setOnClickListener {
            RouteManager.route(it.context, ApplinkConst.SellerApp.CREATE_VOUCHER)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_attachvoucher_empty
    }
}