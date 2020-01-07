package com.tokopedia.shop.newproduct.view.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.shop.R
import com.tokopedia.shop.newproduct.view.datamodel.ShopMerchantVoucherViewModel

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopMerchantVoucherViewHolder(itemView: View, onMerchantVoucherListWidgetListener: MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener?) : AbstractViewHolder<ShopMerchantVoucherViewModel>(itemView) {

    private var merchantVoucherListWidget: MerchantVoucherListWidget? = null

    init {
        findViews(itemView)
        onMerchantVoucherListWidgetListener?.let { merchantVoucherListWidget?.setOnMerchantVoucherListWidgetListener(it) }
    }

    override fun bind(shopMerchantVoucherViewModel: ShopMerchantVoucherViewModel) {
        val recyclerViewState = merchantVoucherListWidget!!.recyclerView!!.layoutManager!!.onSaveInstanceState()

        shopMerchantVoucherViewModel.shopMerchantVoucherViewModelArrayList?.let {
            merchantVoucherListWidget!!.setData(ArrayList(it.toMutableList()))
        }

        if (recyclerViewState != null) {
            merchantVoucherListWidget!!.recyclerView!!.layoutManager!!.onRestoreInstanceState(recyclerViewState)
        }
    }

    private fun findViews(view: View) {
        merchantVoucherListWidget = view.findViewById(R.id.merchantVoucherListWidget)

    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_merchant_voucher
    }

}
