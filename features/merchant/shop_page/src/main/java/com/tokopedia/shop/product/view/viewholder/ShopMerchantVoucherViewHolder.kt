package com.tokopedia.shop.product.view.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.datamodel.ShopMerchantVoucherUiModel

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopMerchantVoucherViewHolder(itemView: View) : AbstractViewHolder<ShopMerchantVoucherUiModel>(itemView) {

    private var merchantVoucherWidget: MvcView? = null

    init {
        findViews(itemView)
    }

    override fun bind(model: ShopMerchantVoucherUiModel) {
        if (model.data != null && model.data.isShown == true) {
            merchantVoucherWidget?.show()
            model.data.apply {
                merchantVoucherWidget?.setData(MvcData(
                        title = titles?.firstOrNull()?.text ?: "",
                        subTitle = model.data.subTitle ?: "",
                        imageUrl = model.data.imageURL ?: ""
                ),
                        shopId = model.data.shopId ?: "0"
                )
            }
        } else {
            merchantVoucherWidget?.hide()
        }
    }

    private fun findViews(view: View) {
        merchantVoucherWidget = view.findViewById(R.id.merchantVoucherWidget)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_new_shop_product_merchant_voucher
    }

}
