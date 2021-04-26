package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_courier_driver_info.view.*

class CourierDriverInfoViewHolder(itemView: View?) : AbstractViewHolder<ShipmentInfoUiModel.CourierDriverInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_courier_driver_info
    }

    private var element: ShipmentInfoUiModel.CourierDriverInfoUiModel? = null

    init {
        setupClickListeners()
    }

    override fun bind(element: ShipmentInfoUiModel.CourierDriverInfoUiModel?) {
        element?.let {
            this.element = it
            setupDriverPhoto(it.photoUrl)
            setupDriverName(it.name)
            setupDriverPhoneNumber(it.phoneNumber)
            setupDriverPlateNumber(it.plateNumber)
        }
    }

    private fun setupClickListeners() {
        itemView.btnBuyerOrderDetailCallCourierDriver?.setOnClickListener {
            callDriver()
        }
    }

    private fun callDriver() {
        element?.let {
            BuyerOrderDetailNavigator.goToCallingPage(itemView.context, it.phoneNumber)
        }
    }

    private fun setupDriverPhoto(photoUrl: String) {
        itemView.ivBuyerOrderDetailCourierDriverPhoto?.setImageUrl(photoUrl)
    }

    private fun setupDriverName(name: String) {
        itemView.tvBuyerOrderDetailCourierDriverName?.text = name
    }

    private fun setupDriverPhoneNumber(phoneNumber: String) {
        itemView.tvBuyerOrderDetailCourierDriverPhoneNumber?.text = phoneNumber
    }

    private fun setupDriverPlateNumber(plateNumber: String) {
        itemView.tvBuyerOrderDetailCourierDriverPlateNumber?.text = plateNumber
    }
}