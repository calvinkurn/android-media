package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMerchantVoucherDataModel
import com.tokopedia.product.detail.view.adapter.ProductMerchantVoucherAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.MarginItemDecoration
import kotlinx.android.synthetic.main.item_dynamic_voucher.view.*
import kotlinx.android.synthetic.main.item_shimmer_shop_voucher.view.*

class ProductMerchantVoucherViewHolder(val view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMerchantVoucherDataModel>(view) {

    private val voucherAdapter by lazy {
        ProductMerchantVoucherAdapter()
    }

    companion object {
        val LAYOUT = R.layout.item_dynamic_voucher
    }

    override fun bind(element: ProductMerchantVoucherDataModel?) {
        if (element?.shouldRenderInitialData != false) {
            showLoading()
            if (element?.voucherData?.isNotEmpty() == true) {
                hideLoading()
                view.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
                renderRv(element)
                element.shouldRenderInitialData = false
            }
        }
    }

    private fun renderRv(element: ProductMerchantVoucherDataModel?) = with(view) {
        rv_merchant_voucher?.run {
            adapter = voucherAdapter
            if (itemDecorationCount == 0)
                addItemDecoration(MarginItemDecoration(16, 0, 0, 0))
            voucherAdapter.setListener(object : ProductMerchantVoucherAdapter.PdpMerchantVoucherInterface {
                override fun onMerchantVoucherClicked(data: MerchantVoucherViewModel) {
                    listener.onItemMerchantVoucherClicked(data, getComponentTrackData(element))
                }
            })

            voucherAdapter.setData(element?.voucherData ?: listOf())
        }
    }
    
    private fun showLoading() = with(view) {
        rv_merchant_voucher?.hide()
        shimmer_shop_voucher_container?.show()
    }

    private fun hideLoading() = with(view) {
        rv_merchant_voucher?.show()
        shimmer_shop_voucher_container?.hide()
    }

    private fun getComponentTrackData(element: ProductMerchantVoucherDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}