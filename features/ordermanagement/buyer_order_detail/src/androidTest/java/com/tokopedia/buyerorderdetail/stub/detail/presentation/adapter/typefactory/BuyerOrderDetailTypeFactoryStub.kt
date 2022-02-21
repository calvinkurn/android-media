package com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.CourierDriverInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.CourierInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.DigitalRecommendationViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PgRecommendationViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductBundlingViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.TickerViewHolder
import com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.viewholder.CourierDriverInfoViewHolderStub
import com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.viewholder.PgRecommendationViewHolderStub
import com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.viewholder.ProductViewHolderStub
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationData

class BuyerOrderDetailTypeFactoryStub(
    productBundlingViewListener: ProductBundlingViewHolder.Listener,
    tickerViewHolderListener: TickerViewHolder.TickerViewHolderListener,
    digitalRecommendationData: DigitalRecommendationData,
    digitalRecommendationListener: DigitalRecommendationViewHolder.ActionListener,
    courierInfoViewHolderListener: CourierInfoViewHolder.CourierInfoViewHolderListener,
    productViewListener: ProductViewHolder.ProductViewListener,
    navigator: BuyerOrderDetailNavigator,
    buyerOrderDetailBindRecomWidgetListener: PgRecommendationViewHolder.BuyerOrderDetailBindRecomWidgetListener
) : BuyerOrderDetailTypeFactory(
    productBundlingViewListener,
    tickerViewHolderListener,
    digitalRecommendationData,
    digitalRecommendationListener,
    courierInfoViewHolderListener,
    productViewListener,
    navigator,
    buyerOrderDetailBindRecomWidgetListener
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