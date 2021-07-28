package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CourierInfoViewHolder(itemView: View?) : AbstractViewHolder<ShipmentInfoUiModel.CourierInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_courier
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val ivBuyerOrderDetailFreeShipmentBadge = itemView?.findViewById<ImageUnify>(R.id.ivBuyerOrderDetailFreeShipmentBadge)
    private val tvBuyerOrderDetailCourierValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailCourierValue)
    private val tvBuyerOrderDetailArrivalEstimationValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailArrivalEstimationValue)

    override fun bind(element: ShipmentInfoUiModel.CourierInfoUiModel?) {
        element?.let {
            setupCourierNameAndProductName(it.courierNameAndProductName)
            setupFreeShippingBadge(it.isFreeShipping, it.boBadgeUrl)
            setupArrivalEstimation(it.arrivalEstimation, it.isFreeShipping)
        }
    }

    override fun bind(element: ShipmentInfoUiModel.CourierInfoUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ShipmentInfoUiModel.CourierInfoUiModel && newItem is ShipmentInfoUiModel.CourierInfoUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.courierNameAndProductName != newItem.courierNameAndProductName) {
                        setupCourierNameAndProductName(newItem.courierNameAndProductName)
                    }
                    if (oldItem.isFreeShipping != newItem.isFreeShipping || oldItem.boBadgeUrl != newItem.boBadgeUrl) {
                        setupFreeShippingBadge(newItem.isFreeShipping, newItem.boBadgeUrl)
                    }
                    if (oldItem.arrivalEstimation != newItem.arrivalEstimation || oldItem.isFreeShipping != newItem.isFreeShipping) {
                        setupArrivalEstimation(newItem.arrivalEstimation, newItem.isFreeShipping)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupCourierNameAndProductName(courierNameAndProductName: String) {
        tvBuyerOrderDetailCourierValue?.text = courierNameAndProductName
    }

    private fun setupFreeShippingBadge(freeShipping: Boolean, boBadgeUrl: String) {
        ivBuyerOrderDetailFreeShipmentBadge?.apply {
            ImageHandler.loadImage2(this, boBadgeUrl, com.tokopedia.kotlin.extensions.R.drawable.ic_loading_error)
            showWithCondition(freeShipping)
        }
    }

    private fun setupArrivalEstimation(arrivalEstimation: String, freeShipping: Boolean) {
        tvBuyerOrderDetailArrivalEstimationValue?.text = arrivalEstimation
        tvBuyerOrderDetailArrivalEstimationValue?.setPadding(0, getArrivalEstimationTopMargin(freeShipping), 0, 0)
        tvBuyerOrderDetailArrivalEstimationValue?.showWithCondition(arrivalEstimation.isNotBlank())
    }

    private fun getArrivalEstimationTopMargin(freeShipping: Boolean): Int {
        return if (freeShipping) 0 else itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).toInt()
    }
}