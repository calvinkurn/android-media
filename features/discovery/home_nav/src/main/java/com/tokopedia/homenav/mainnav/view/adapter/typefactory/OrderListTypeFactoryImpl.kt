package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderPaymentViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderProductViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OtherTransactionViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderReviewViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderEmptyViewHolder
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderNavVisitable
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderReviewModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderEmptyModel

class OrderListTypeFactoryImpl(val mainNavListener: MainNavListener) : BaseAdapterTypeFactory(), OrderListTypeFactory {

    override fun type(orderProductModel: OrderProductModel): Int {
        return OrderProductViewHolder.LAYOUT
    }

    override fun type(orderPaymentModel: OrderPaymentModel): Int {
        return OrderPaymentViewHolder.LAYOUT
    }

    override fun type(otherTransactionModel: OtherTransactionModel): Int {
        return OtherTransactionViewHolder.LAYOUT
    }

    override fun type(orderReviewModel: OrderReviewModel): Int {
        return OrderReviewViewHolder.LAYOUT
    }

    override fun type(orderEmptyModel: OrderEmptyModel): Int {
        return OrderEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<OrderNavVisitable> {
        return when (viewType) {
            OrderProductViewHolder.LAYOUT -> OrderProductViewHolder(view, mainNavListener)
            OrderPaymentViewHolder.LAYOUT -> OrderPaymentViewHolder(view, mainNavListener)
            OrderReviewViewHolder.LAYOUT -> OrderReviewViewHolder(view, mainNavListener)
            OrderEmptyViewHolder.LAYOUT -> OrderEmptyViewHolder(view, mainNavListener)
            OtherTransactionViewHolder.LAYOUT -> OtherTransactionViewHolder(view, mainNavListener)
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<OrderNavVisitable>
    }
}