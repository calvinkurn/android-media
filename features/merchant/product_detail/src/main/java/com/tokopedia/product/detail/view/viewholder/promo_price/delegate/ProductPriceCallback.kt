package com.tokopedia.product.detail.view.viewholder.promo_price.delegate

import com.tokopedia.mvcwidget.views.benefit.PromoBenefitBottomSheet
import com.tokopedia.product.detail.common.showImmediately
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.promo_price.event.ProductPriceEvent
import com.tokopedia.product.detail.view.viewholder.promo_price.tracker.PromoPriceTracker

class ProductPriceCallback(
    mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<ProductPriceEvent>(mediator = mediator) {
    companion object {
        private const val PROMO_PRICE_BS_TAG = "PROMO_PRICE_BS_TAG"
    }

    override fun onEvent(event: ProductPriceEvent) {
        when (event) {
            is ProductPriceEvent.OnPromoPriceClicked -> onPromoPriceClicked(data = event)
        }
    }

    private fun onPromoPriceClicked(
        data: ProductPriceEvent.OnPromoPriceClicked
    ) {
        val p1 = viewModel.getProductInfoP1 ?: return
        PromoPriceTracker.onPromoPriceClicked(
            queueTracker = queueTracker,
            subtitle = data.subtitle,
            defaultPriceFmt = data.defaultPriceFmt,
            slashPriceFmt = data.slashPriceFmt,
            promoPriceFmt = data.promoPriceFmt,
            promoId = data.promoId,
            layoutData = data.trackerData,
            commonTracker = getCommonTracker()
        )

        showImmediately(childFragmentManager, PROMO_PRICE_BS_TAG) {
            PromoBenefitBottomSheet.newInstance(
                metaDataJson = data.bottomSheetParams,
                productId = p1.basic.productID,
                shopId = p1.basic.shopID
            )
        }
    }
}
