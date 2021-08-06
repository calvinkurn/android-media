package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.MvcSource
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductMerchantVoucherSummaryDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductMerchantVoucherSummaryViewHolder(val view: View, val listener:DynamicProductDetailListener): AbstractViewHolder<ProductMerchantVoucherSummaryDataModel>(view) {

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
            setMerchantVoucher(it.animatedInfos, it.shopId)
        }
        merchantVoucher?.let {
            sendImpressionTracker()
        }
    }

    private fun initMerchantVoucher() {
        merchantVoucher = view.findViewById(R.id.productDetailMerchantVoucherWidget)
    }

    private fun setMerchantVoucher(animatedInfos: List<AnimatedInfos>, shopId: String) {
        merchantVoucher?.setData(MvcData(animatedInfos), shopId, MvcSource.PDP) {
            listener.onMerchantVoucherSummaryClicked(shopId, MvcSource.PDP)
        }
        merchantVoucher?.show()
    }

    private fun sendImpressionTracker(){
        merchantVoucher?.sendImpressionTrackerForPdp()
    }

}