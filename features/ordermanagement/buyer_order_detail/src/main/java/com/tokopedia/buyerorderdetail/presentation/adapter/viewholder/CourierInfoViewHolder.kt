package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.item_buyer_order_detail_shipment_info_courier.view.*

class CourierInfoViewHolder(itemView: View?) : AbstractViewHolder<ShipmentInfoUiModel.CourierInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_courier
    }

    override fun bind(element: ShipmentInfoUiModel.CourierInfoUiModel?) {
        element?.let {
            setupCourierNameAndProductName(it.courierNameAndProductName)
            setupFreeShippingBadge(it.isFreeShipping)
            setupArrivalEstimation(it.arrivalEstimation)
        }
    }

    private fun setupCourierNameAndProductName(courierNameAndProductName: String) {
        itemView.tvBuyerOrderDetailCourierValue?.text = courierNameAndProductName
    }

    private fun setupFreeShippingBadge(freeShipping: Boolean) {
        itemView.ivBuyerOrderDetailFreeShipmentBadge?.showWithCondition(freeShipping)
    }

    private fun setupArrivalEstimation(arrivalEstimation: String) {
        itemView.tvBuyerOrderDetailArrivalEstimationValue?.text = arrivalEstimation
    }
}