package com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.delegate

import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.tracking.DynamicOneLinerTracking
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.ActionUiModel
import com.tokopedia.product.detail.view.viewholder.ActionUiModel.Companion.APPLINK
import com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.event.DynamicOneLinerEvent
import com.tokopedia.product.detail.view.widget.ProductDetailNavigator.goToMvc

class DynamicOneLinerCallback(
    private val mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<DynamicOneLinerEvent>(mediator = mediator) {
    override fun onEvent(event: DynamicOneLinerEvent) {
        when (event) {
            is DynamicOneLinerEvent.OnDynamicOneLinerClicked ->
                onDynamicOneLinerClicked(data = event)
        }
    }

    private fun onDynamicOneLinerClicked(
        data: DynamicOneLinerEvent.OnDynamicOneLinerClicked
    ) {
        val commonTracker = getCommonTracker() ?: return
        if (data.trackDataModel.componentName == ProductDetailConstant.PRODUCT_DYNAMIC_ONELINER_PROMO) {
            onClickDynamicOneLinerPromo()
        } else {
            ActionUiModel(type = APPLINK, link = data.url).navigate()
        }
        DynamicOneLinerTracking.onClickDynamicOneliner(
            data.title,
            commonTracker,
            data.trackDataModel
        )
    }

    private fun onClickDynamicOneLinerPromo() {
        val mvcData = viewModel.getP2()?.merchantVoucherSummary ?: return
        val p1 = viewModel.getProductInfoP1 ?: return
        context?.goToMvc(
            shopId = p1.basic.shopID,
            productId = p1.basic.productID,
            mvcAdditionalData = mvcData.additionalData,
            launcher = mediator.mvcLauncher
        )
    }
}
