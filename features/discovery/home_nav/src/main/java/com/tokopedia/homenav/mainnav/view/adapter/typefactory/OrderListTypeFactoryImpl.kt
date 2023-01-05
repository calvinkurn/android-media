package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderEmptyViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderPaymentRevampViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderProductRevampViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderReviewViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OtherTransactionViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderPaymentViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderProductViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OtherTransactionRevampViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderEmptyModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentRevampModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductRevampModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.OrderReviewModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionRevampModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderNavVisitable
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener

class OrderListTypeFactoryImpl(val mainNavListener: MainNavListener) : BaseAdapterTypeFactory(), OrderListTypeFactory {

    override fun type(orderProductRevampModel: OrderProductRevampModel): Int {
        return OrderProductRevampViewHolder.LAYOUT
    }

    override fun type(orderPaymentRevampModel: OrderPaymentRevampModel): Int {
        return OrderPaymentRevampViewHolder.LAYOUT
    }

    override fun type(otherTransactionModel: OtherTransactionModel): Int {
        return OtherTransactionViewHolder.LAYOUT
    }

    override fun type(orderEmptyModel: OrderEmptyModel): Int {
        return OrderEmptyViewHolder.LAYOUT
    }

    override fun type(orderPaymentModel: OrderPaymentModel): Int {
        return OrderPaymentViewHolder.LAYOUT
    }

    override fun type(orderProductModel: OrderProductModel): Int {
        return OrderProductViewHolder.LAYOUT
    }

    override fun type(otherTransactionRevampModel: OtherTransactionRevampModel): Int {
        return OtherTransactionRevampViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<OrderNavVisitable> {
        return when (viewType) {
            OrderProductRevampViewHolder.LAYOUT -> OrderProductRevampViewHolder(view, mainNavListener)
            OrderPaymentRevampViewHolder.LAYOUT -> OrderPaymentRevampViewHolder(view, mainNavListener)
            OrderReviewViewHolder.LAYOUT -> OrderReviewViewHolder(view, mainNavListener)
            OrderEmptyViewHolder.LAYOUT -> OrderEmptyViewHolder(view, mainNavListener)
            OrderProductViewHolder.LAYOUT -> OrderProductViewHolder(view, mainNavListener)
            OrderPaymentViewHolder.LAYOUT -> OrderPaymentViewHolder(view, mainNavListener)
            OtherTransactionViewHolder.LAYOUT -> OtherTransactionViewHolder(view, mainNavListener)
            OtherTransactionRevampViewHolder.LAYOUT -> OtherTransactionRevampViewHolder(view, mainNavListener)
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<OrderNavVisitable>
    }
}
