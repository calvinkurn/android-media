package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.CourierActionButtonAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.CourierButtonListener
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

open class CourierDriverInfoViewHolder(
    private val itemView: View,
    private val navigator: BuyerOrderDetailNavigator,
    private val courierButtonListener: CourierButtonListener
) : AbstractViewHolder<ShipmentInfoUiModel.CourierDriverInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_courier_driver_info
    }

    private val container = itemView.findViewById<ConstraintLayout>(R.id.container)
    private val ivBuyerOrderDetailCourierDriverPhoto = itemView.findViewById<ImageUnify>(R.id.ivBuyerOrderDetailCourierDriverPhoto)
    private val tvBuyerOrderDetailCourierDriverName = itemView.findViewById<Typography>(R.id.tvBuyerOrderDetailCourierDriverName)
    private val tvBuyerOrderDetailCourierDriverPlateNumber = itemView.findViewById<Typography>(R.id.tvBuyerOrderDetailCourierDriverPlateNumber)
    private val rvBuyerOrderDetailCourierDriverButton = itemView.findViewById<RecyclerView>(R.id.rv_courier_driver_btn)

    private var element: ShipmentInfoUiModel.CourierDriverInfoUiModel? = null

    override fun bind(element: ShipmentInfoUiModel.CourierDriverInfoUiModel?) {
        element?.let {
            this.element = it
            setupDriverPhoto(it.photoUrl)
            setupDriverName(it.name)
            setupDriverPlateNumber(it.plateNumber)
            setupActionButtons(element)
        }
    }

    override fun bind(element: ShipmentInfoUiModel.CourierDriverInfoUiModel?, payloads: MutableList<Any>) {
        bind(element)
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

    private fun setupActionButtons(element: ShipmentInfoUiModel.CourierDriverInfoUiModel) {
        CourierActionButtonAdapter(navigator, courierButtonListener).apply {
            this.driverActionButtonList = element.buttonList
            rvBuyerOrderDetailCourierDriverButton?.adapter = this
            rvBuyerOrderDetailCourierDriverButton.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                element.buttonList.size == Int.ONE
            ) // Reverse layout if only one
        }
    }
}
