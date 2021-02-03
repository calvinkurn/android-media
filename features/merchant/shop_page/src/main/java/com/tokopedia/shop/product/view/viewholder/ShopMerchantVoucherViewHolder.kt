package com.tokopedia.shop.product.view.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.datamodel.ShopMerchantVoucherUiModel

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopMerchantVoucherViewHolder(itemView: View, onMerchantVoucherListWidgetListener: MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener?) : AbstractViewHolder<ShopMerchantVoucherUiModel>(itemView) {

    private var merchantVoucherListWidget: MerchantVoucherListWidget? = null

    init {
        findViews(itemView)
        onMerchantVoucherListWidgetListener?.let { merchantVoucherListWidget?.setOnMerchantVoucherListWidgetListener(it) }
    }

    override fun bind(shopMerchantVoucherUiModel: ShopMerchantVoucherUiModel) {
        val recyclerViewState = merchantVoucherListWidget?.recyclerView?.layoutManager?.onSaveInstanceState()

        shopMerchantVoucherUiModel.shopMerchantVoucherViewModelArrayList?.let {
            merchantVoucherListWidget?.setData(ArrayList(it.toMutableList()))
        }

        recyclerViewState?.let {
            merchantVoucherListWidget?.recyclerView?.layoutManager?.onRestoreInstanceState(it)
        }
    }

    private fun findViews(view: View) {
        merchantVoucherListWidget = view.findViewById(R.id.merchantVoucherListWidget)

    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_new_shop_product_merchant_voucher
    }

}
