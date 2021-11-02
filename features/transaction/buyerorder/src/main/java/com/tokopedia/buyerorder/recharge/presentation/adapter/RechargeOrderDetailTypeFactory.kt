package com.tokopedia.buyerorder.recharge.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.databinding.*
import com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder.*
import com.tokopedia.buyerorder.recharge.presentation.model.*
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationData

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailTypeFactory(
        private val digitalRecommendationData: DigitalRecommendationData,
        private val topSectionListener: RechargeOrderDetailTopSectionViewHolder.ActionListener,
        private val detailSectionListener: RechargeOrderDetailProductViewHolder.ActionListener,
        private val digitalRecommendationListener: RechargeOrderDetailDigitalRecommendationViewHolder.ActionListener,
        private val staticButtonListener: RechargeOrderDetailStaticButtonViewHolder.ActionListener,
        private val aboutOrderListener: RechargeOrderDetailAboutOrderViewHolder.ActionListener
) : BaseAdapterTypeFactory() {

    fun type(aboutOrderModel: RechargeOrderDetailAboutOrderModel): Int =
            RechargeOrderDetailAboutOrderViewHolder.LAYOUT

    fun type(digitalRecommendationModel: RechargeOrderDetailDigitalRecommendationModel): Int =
            RechargeOrderDetailDigitalRecommendationViewHolder.LAYOUT

    fun type(dividerModel: RechargeOrderDetailDividerModel): Int =
            RechargeOrderDetailDividerViewHolder.LAYOUT

    fun type(paymentModel: RechargeOrderDetailPaymentModel): Int =
            RechargeOrderDetailPaymentViewHolder.LAYOUT

    fun type(productModel: RechargeOrderDetailSectionModel): Int =
            RechargeOrderDetailProductViewHolder.LAYOUT

    fun type(staticButtonModel: RechargeOrderDetailStaticButtonModel): Int =
            RechargeOrderDetailStaticButtonViewHolder.LAYOUT

    fun type(topSectionModel: RechargeOrderDetailTopSectionModel): Int =
            RechargeOrderDetailTopSectionViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                RechargeOrderDetailAboutOrderViewHolder.LAYOUT -> {
                    val binding = ItemOrderDetailRechargeAboutOrdersBinding.bind(parent)
                    RechargeOrderDetailAboutOrderViewHolder(binding, aboutOrderListener)
                }
                RechargeOrderDetailDigitalRecommendationViewHolder.LAYOUT -> {
                    val binding = ItemOrderDetailRechargeDigitalRecommendationBinding.bind(parent)
                    RechargeOrderDetailDigitalRecommendationViewHolder(binding, digitalRecommendationData, digitalRecommendationListener)
                }
                RechargeOrderDetailDividerViewHolder.LAYOUT -> RechargeOrderDetailDividerViewHolder(parent)
                RechargeOrderDetailPaymentViewHolder.LAYOUT -> {
                    val binding = ItemOrderDetailRechargePaymentDetailBinding.bind(parent)
                    RechargeOrderDetailPaymentViewHolder(binding)
                }
                RechargeOrderDetailProductViewHolder.LAYOUT -> {
                    val binding = ItemOrderDetailRechargeProductBinding.bind(parent)
                    RechargeOrderDetailProductViewHolder(binding, detailSectionListener)
                }
                RechargeOrderDetailStaticButtonViewHolder.LAYOUT -> {
                    val binding = ItemOrderDetailRechargeStaticButtonBinding.bind(parent)
                    RechargeOrderDetailStaticButtonViewHolder(binding, staticButtonListener)
                }
                RechargeOrderDetailTopSectionViewHolder.LAYOUT -> {
                    val binding = ItemOrderDetailRechargeTopStatusBinding.bind(parent)
                    RechargeOrderDetailTopSectionViewHolder(binding, topSectionListener)
                }
                else -> super.createViewHolder(parent, type)

            }
}