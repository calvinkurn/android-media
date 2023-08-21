package com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.CourierDriverInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.CourierInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.DigitalRecommendationViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OrderResolutionViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PartialProductItemViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PgRecommendationViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofRefundInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductBundlingViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductListToggleViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.TickerViewHolder
import com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.viewholder.CourierDriverInfoViewHolderStub
import com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.viewholder.PgRecommendationViewHolderStub
import com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.viewholder.ProductViewHolderStub
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationData
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.viewholder.ScpRewardsMedalTouchPointWidgetViewHolder

class BuyerOrderDetailTypeFactoryStub(
    productBundlingViewListener: ProductBundlingViewHolder.Listener,
    tickerViewHolderListener: TickerViewHolder.TickerViewHolderListener,
    digitalRecommendationData: DigitalRecommendationData,
    digitalRecommendationListener: DigitalRecommendationViewHolder.ActionListener,
    courierInfoViewHolderListener: CourierInfoViewHolder.CourierInfoViewHolderListener,
    productListToggleListener: ProductListToggleViewHolder.Listener,
    scpRewardsMedalTouchPointWidgetListener: ScpRewardsMedalTouchPointWidgetViewHolder.ScpRewardsMedalTouchPointWidgetListener,
    pofRefundInfoListener: PofRefundInfoViewHolder.Listener,
    owocInfoListener: OwocInfoViewHolder.Listener,
    productViewListener: PartialProductItemViewHolder.ProductViewListener,
    navigator: BuyerOrderDetailNavigator,
    buyerOrderDetailBindRecomWidgetListener: PgRecommendationViewHolder.BuyerOrderDetailBindRecomWidgetListener,
    orderResolutionListener: OrderResolutionViewHolder.OrderResolutionListener
) : BuyerOrderDetailTypeFactory(
    productBundlingViewListener,
    tickerViewHolderListener,
    digitalRecommendationData,
    digitalRecommendationListener,
    courierInfoViewHolderListener,
    productListToggleListener,
    pofRefundInfoListener,
    scpRewardsMedalTouchPointWidgetListener,
    owocInfoListener,
    productViewListener,
    navigator,
    buyerOrderDetailBindRecomWidgetListener,
    orderResolutionListener
) {
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CourierDriverInfoViewHolder.LAYOUT -> CourierDriverInfoViewHolderStub(parent, navigator)
            ProductViewHolder.LAYOUT -> ProductViewHolderStub(parent, productViewListener, navigator)
            PgRecommendationViewHolder.LAYOUT -> PgRecommendationViewHolderStub(parent, buyerOrderDetailBindRecomWidgetListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
