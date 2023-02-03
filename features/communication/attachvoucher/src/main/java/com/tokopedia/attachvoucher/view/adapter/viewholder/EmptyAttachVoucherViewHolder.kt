package com.tokopedia.attachvoucher.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.databinding.ItemAttachvoucherEmptyBinding
import com.tokopedia.utils.view.binding.viewBinding

class EmptyAttachVoucherViewHolder(itemView: View?) : AbstractViewHolder<EmptyModel>(itemView) {

    private val emptyIconUrl = "https://images.tokopedia.net/android/others/Voucher_Ilustration@3x.png"

    private val binding: ItemAttachvoucherEmptyBinding? by viewBinding()

    override fun bind(element: EmptyModel?) {
        bindEmptyImage()
        bindButtonClick()
    }

    private fun bindEmptyImage() {
        ImageHandler.loadImageAndCache(binding?.ivEmpty, emptyIconUrl)
    }

    private fun bindButtonClick() {
        binding?.btnCreateVoucher?.setOnClickListener {
            RouteManager.route(it.context, ApplinkConst.SellerApp.CREATE_VOUCHER)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_attachvoucher_empty
    }
}
