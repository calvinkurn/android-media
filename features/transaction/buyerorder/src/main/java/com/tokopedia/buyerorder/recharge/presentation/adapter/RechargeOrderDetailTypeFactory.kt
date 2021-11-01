package com.tokopedia.buyerorder.recharge.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargePaymentDetailBinding
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeProductBinding
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeTopStatusBinding
import com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder.RechargeOrderDetailDividerViewHolder
import com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder.RechargeOrderDetailPaymentViewHolder
import com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder.RechargeOrderDetailProductViewHolder
import com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder.RechargeOrderDetailTopSectionViewHolder
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailDividerModel
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailPaymentModel
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailSectionModel
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailTopSectionModel

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailTypeFactory(
        private val topSectionListener: RechargeOrderDetailTopSectionViewHolder.RechargeOrderDetailTopSectionActionListener,
        private val detailSectionListener: RechargeOrderDetailProductViewHolder.RechargeOrderDetailProductActionListener
) : BaseAdapterTypeFactory() {

    fun type(dividerModel: RechargeOrderDetailDividerModel): Int =
            RechargeOrderDetailDividerViewHolder.LAYOUT

    fun type(paymentModel: RechargeOrderDetailPaymentModel): Int =
            RechargeOrderDetailPaymentViewHolder.LAYOUT

    fun type(productModel: RechargeOrderDetailSectionModel): Int =
            RechargeOrderDetailProductViewHolder.LAYOUT

    fun type(topSectionModel: RechargeOrderDetailTopSectionModel): Int =
            RechargeOrderDetailTopSectionViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                RechargeOrderDetailDividerViewHolder.LAYOUT -> RechargeOrderDetailDividerViewHolder(parent)
                RechargeOrderDetailPaymentViewHolder.LAYOUT -> {
                    val binding = ItemOrderDetailRechargePaymentDetailBinding.bind(parent)
                    RechargeOrderDetailPaymentViewHolder(binding)
                }
                RechargeOrderDetailProductViewHolder.LAYOUT -> {
                    val binding = ItemOrderDetailRechargeProductBinding.bind(parent)
                    RechargeOrderDetailProductViewHolder(binding, detailSectionListener)
                }
                RechargeOrderDetailTopSectionViewHolder.LAYOUT -> {
                    val binding = ItemOrderDetailRechargeTopStatusBinding.bind(parent)
                    RechargeOrderDetailTopSectionViewHolder(binding, topSectionListener)
                }
                else -> super.createViewHolder(parent, type)

            }
}