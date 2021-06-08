package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class CourierDriverInfoViewHolder(
        itemView: View?,
        private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<ShipmentInfoUiModel.CourierDriverInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_courier_driver_info
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val btnBuyerOrderDetailCallCourierDriver = itemView?.findViewById<UnifyButton>(R.id.btnBuyerOrderDetailCallCourierDriver)
    private val ivBuyerOrderDetailCourierDriverPhoto = itemView?.findViewById<ImageUnify>(R.id.ivBuyerOrderDetailCourierDriverPhoto)
    private val tvBuyerOrderDetailCourierDriverName = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailCourierDriverName)
    private val tvBuyerOrderDetailCourierDriverPhoneNumber = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailCourierDriverPhoneNumber)
    private val tvBuyerOrderDetailCourierDriverPlateNumber = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailCourierDriverPlateNumber)

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

    override fun bind(element: ShipmentInfoUiModel.CourierDriverInfoUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is ShipmentInfoUiModel.CourierDriverInfoUiModel && newItem is ShipmentInfoUiModel.CourierDriverInfoUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    this.element = newItem
                    if (oldItem.photoUrl != newItem.photoUrl) {
                        setupDriverPhoto(newItem.photoUrl)
                    }
                    if (oldItem.name != newItem.name) {
                        setupDriverName(newItem.name)
                    }
                    if (oldItem.phoneNumber != newItem.phoneNumber) {
                        setupDriverPhoneNumber(newItem.phoneNumber)
                    }
                    if (oldItem.plateNumber != newItem.plateNumber) {
                        setupDriverPlateNumber(newItem.plateNumber)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupClickListeners() {
        btnBuyerOrderDetailCallCourierDriver?.setOnClickListener {
            callDriver()
        }
    }

    private fun callDriver() {
        element?.let { navigator.goToCallingPage(it.phoneNumber) }
    }

    private fun setupDriverPhoto(photoUrl: String) {
        ivBuyerOrderDetailCourierDriverPhoto?.setImageUrl(photoUrl)
    }

    private fun setupDriverName(name: String) {
        tvBuyerOrderDetailCourierDriverName?.text = name
    }

    private fun setupDriverPhoneNumber(phoneNumber: String) {
        tvBuyerOrderDetailCourierDriverPhoneNumber?.text = phoneNumber
    }

    private fun setupDriverPlateNumber(plateNumber: String) {
        tvBuyerOrderDetailCourierDriverPlateNumber?.text = plateNumber
    }
}