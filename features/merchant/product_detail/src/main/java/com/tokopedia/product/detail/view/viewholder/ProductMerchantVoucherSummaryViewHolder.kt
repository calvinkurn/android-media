package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.MvcSource
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductMerchantVoucherSummaryDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductMerchantVoucherSummaryViewHolder(val view: View): AbstractViewHolder<ProductMerchantVoucherSummaryDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_merchant_voucher_summary
    }

    private var merchantVoucher: MvcView? = null

    override fun bind(element: ProductMerchantVoucherSummaryDataModel?) {
        if(element?.isShown != true) {
            view.layoutParams.height = 0
            return
        }
        view.layoutParams.height = view.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl7).toInt()
        initMerchantVoucher()
        element.let {
            setMerchantVoucher(it.title, it.subTitle, it.imageURL, it.shopId)
        }
    }

    private fun initMerchantVoucher() {
        merchantVoucher = view.findViewById(R.id.productDetailMerchantVoucherWidget)
    }

    private fun setMerchantVoucher(title: String, subtitle: String, imageUrl: String, shopId: String) {
        merchantVoucher?.setData(MvcData(title, subtitle, imageUrl), shopId,false, MvcSource.PDP)
        merchantVoucher?.show()
    }

}