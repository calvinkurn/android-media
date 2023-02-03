package com.tokopedia.shop.product.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemNewShopProductMerchantVoucherBinding
import com.tokopedia.shop.product.view.datamodel.ShopMerchantVoucherUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopMerchantVoucherViewHolder(itemView: View) : AbstractViewHolder<ShopMerchantVoucherUiModel>(itemView) {

    private val viewBinding: ItemNewShopProductMerchantVoucherBinding? by viewBinding()
    private var merchantVoucherWidget: MvcView? = null

    init {
        findViews()
    }

    override fun bind(model: ShopMerchantVoucherUiModel) {
        model.data?.apply {
            merchantVoucherWidget?.setData(
                MvcData(
                    animatedInfoList = model.data.animatedInfoList
                ),
                shopId = model.data.shopId ?: "0",
                source = MvcSource.SHOP
            )
        }
    }

    private fun findViews() {
        merchantVoucherWidget = viewBinding?.merchantVoucherWidget
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_new_shop_product_merchant_voucher
    }
}
