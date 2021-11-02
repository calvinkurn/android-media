package com.tokopedia.buyerorder.recharge.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.recharge.presentation.model.*

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailAdapter(typeFactory: RechargeOrderDetailTypeFactory) :
        BaseAdapter<RechargeOrderDetailTypeFactory>(typeFactory) {

    fun updateItems(data: RechargeOrderDetailModel) {
        val newItems = setupItems(data)
        visitables.clear()
        visitables.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun setupItems(
            data: RechargeOrderDetailModel
    ): List<Visitable<RechargeOrderDetailTypeFactory>> {
        return mutableListOf<Visitable<RechargeOrderDetailTypeFactory>>().apply {
            setupTopSection(data.topSectionModel)
            setupDetailSection(data.detailsSection)
            setupPaymentSection(data.paymentSectionModel)
            setupDigitalRecommendationWidget()
            setupSBMStaticButton()
            setupLanggananStaticButton()
        }
    }

    private fun MutableList<Visitable<RechargeOrderDetailTypeFactory>>.setupTopSection(topModel: RechargeOrderDetailTopSectionModel) {
        add(topModel)
        addDivider()
    }

    private fun MutableList<Visitable<RechargeOrderDetailTypeFactory>>.setupDetailSection(detailModel: RechargeOrderDetailSectionModel) {
        add(detailModel)
        addDivider()
    }

    private fun MutableList<Visitable<RechargeOrderDetailTypeFactory>>.setupPaymentSection(paymentModel: RechargeOrderDetailPaymentModel) {
        add(paymentModel)
        addDivider()
    }

    private fun MutableList<Visitable<RechargeOrderDetailTypeFactory>>.setupDigitalRecommendationWidget() {
        add(RechargeOrderDetailDigitalRecommendationModel())
        addDivider()
    }

    private fun MutableList<Visitable<RechargeOrderDetailTypeFactory>>.setupSBMStaticButton() {
        add(RechargeOrderDetailStaticButtonModel(
                iconUrl = "",
                iconRes = R.drawable.ic_recharge_order_detail_sbm,
                title = "",
                titleRes = R.string.recharge_order_detail_sbm_label,
                subtitle = "",
                subtitleRes = R.string.recharge_order_detail_sbm_detail,
                actionUrl = ApplinkConst.DIGITAL_SMARTBILLS
        ))
        addDivider()
    }

    private fun MutableList<Visitable<RechargeOrderDetailTypeFactory>>.setupLanggananStaticButton() {
        add(RechargeOrderDetailStaticButtonModel(
                iconUrl = "",
                iconRes = R.drawable.ic_recharge_order_detail_mybills,
                title = "",
                titleRes = R.string.recharge_order_detail_mybills_label,
                subtitle = "",
                subtitleRes = R.string.recharge_order_detail_mybills_detail,
                actionUrl = ACTION_URL_LANGGANAN
        ))
        addDivider()
    }

    private fun MutableList<Visitable<RechargeOrderDetailTypeFactory>>.addDivider() {
        add(RechargeOrderDetailDividerModel())
    }

    fun removeDigitalRecommendation() {
        val index = visitables.indexOfLast { it is RechargeOrderDetailDigitalRecommendationModel }

        if (index != -1) {
            visitables.removeAt(index + 1) // remove divider
            notifyItemRemoved(index + 1)
            visitables.removeAt(index) // remove digital recommendation
            notifyItemRemoved(index)
        }
    }

    companion object {
        private const val ACTION_URL_LANGGANAN = "https://www.tokopedia.com/langganan"
    }

}