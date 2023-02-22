package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

open class CourierDriverInfoViewHolder(
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
            setupDriverPlateNumber(it.plateNumber)
            setupCallingButton(it.phoneNumber)
        }
    }

    override fun bind(element: ShipmentInfoUiModel.CourierDriverInfoUiModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupClickListeners() {
        btnBuyerOrderDetailCallCourierDriver?.setOnClickListener {
            callDriver()
        }
    }

    private fun callDriver() {
        element?.let { navigator.goToCallingPage(it.phoneNumber) }
    }

    protected open fun setupDriverPhoto(photoUrl: String) {
        ivBuyerOrderDetailCourierDriverPhoto?.setImageUrl(photoUrl)
    }

    private fun setupDriverName(name: String) {
        tvBuyerOrderDetailCourierDriverName?.run {
            text = name
            showWithCondition(name.isNotBlank())
        }
    }

    private fun setupDriverPlateNumber(plateNumber: String) {
        tvBuyerOrderDetailCourierDriverPlateNumber?.run {
            text = plateNumber
            showWithCondition(plateNumber.isNotBlank())
        }
    }

    private fun setupCallingButton(phoneNumber: String) {
        btnBuyerOrderDetailCallCourierDriver?.showWithCondition(phoneNumber.isNotBlank())
    }
}