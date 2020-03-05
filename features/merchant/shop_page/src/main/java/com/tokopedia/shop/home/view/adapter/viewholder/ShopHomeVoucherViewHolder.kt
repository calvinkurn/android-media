package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.res.Resources
import android.view.View

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget

import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import java.util.ArrayList

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopHomeVoucherViewHolder(
        itemView: View
) : AbstractViewHolder<ShopHomeVoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_merchant_voucher
    }

    init {
        findView(itemView)
    }

    private fun findView(itemView: View) {
        merchantVoucherListWidget = itemView.findViewById(R.id.merchantVoucherListWidget)
    }

    private var merchantVoucherListWidget: MerchantVoucherListWidget? = null


    override fun bind(model: ShopHomeVoucherUiModel) {
        val recyclerViewState = merchantVoucherListWidget?.recyclerView!!.getLayoutManager()!!.onSaveInstanceState()
        merchantVoucherListWidget?.setData(model.data as ArrayList<MerchantVoucherViewModel>?)
        if (recyclerViewState != null) {
            merchantVoucherListWidget?.recyclerView!!.getLayoutManager()!!.onRestoreInstanceState(recyclerViewState)
        }
    }
}