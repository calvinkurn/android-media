package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductMerchantVoucherSummaryDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicMerchantVoucherSummaryBinding
import com.tokopedia.product.detail.databinding.ItemDynamicMvcContentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductMerchantVoucherSummaryViewHolder(
    val view: View,
    val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductMerchantVoucherSummaryDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_merchant_voucher_summary
    }

    private val rootBinding by lazyThreadSafetyNone {
        ItemDynamicMerchantVoucherSummaryBinding.bind(view)
    }

    private val mvcBinding by lazyThreadSafetyNone {
        ItemDynamicMvcContentBinding.bind(rootBinding.mvcContentStub.inflate())
    }

    private val mvcWidget by lazyThreadSafetyNone {
        mvcBinding.productDetailMerchantVoucherWidget
    }

    override fun bind(element: ProductMerchantVoucherSummaryDataModel?) {
        val uiModel = element?.uiModel

        if (uiModel?.isShown != true) {
            return
        }

        setImpression(element = element)
        setMerchantVoucher(uiModel = element.uiModel)
    }

    private fun setImpression(element: ProductMerchantVoucherSummaryDataModel) {
        mvcWidget.addOnImpressionListener(ImpressHolder()) {
            mvcWidget.sendImpressionTrackerForPdp()
        }

        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun setMerchantVoucher(uiModel: ProductMerchantVoucherSummaryDataModel.UiModel) {
        val source = MvcSource.PDP
        mvcWidget.setData(
            mvcData = MvcData(uiModel.animatedInfo),
            shopId = uiModel.shopId,
            source = source,
            startActivityForResultFunction = {
                listener.onMerchantVoucherSummaryClicked(source = source, uiModel = uiModel)
            }
        )
        mvcWidget.show()
    }
}
