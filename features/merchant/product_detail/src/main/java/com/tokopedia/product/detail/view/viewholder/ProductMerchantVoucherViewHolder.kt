package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductMerchantVoucherDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_voucher.view.*

class ProductMerchantVoucherViewHolder(val view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMerchantVoucherDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_voucher
    }

    override fun bind(element: ProductMerchantVoucherDataModel?) {
        element?.let {
            view.merchantVoucherListWidget.setOnMerchantVoucherListWidgetListener(object : MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener {
                override val isOwner: Boolean
                    get() = listener.isOwner()

                override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
                    listener.onMerchantUseVoucherClicked(merchantVoucherViewModel, position)
                }

                override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
                    listener.onItemMerchantVoucherClicked(merchantVoucherViewModel)
                }

                override fun onSeeAllClicked() {
                    listener.onSeeAllMerchantVoucherClick()
                }

            })

            view.merchantVoucherListWidget.setData(it.voucherData)
        }
    }
}