package com.tokopedia.buyerorderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailCourierDriverIconBinding
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.utils.view.binding.viewBinding

class CourierActionButtonAdapter(
    private val driver: ShipmentInfoUiModel.CourierDriverInfoUiModel,
    private val navigator: BuyerOrderDetailNavigator
): RecyclerView.Adapter<CourierActionButtonAdapter.ViewHolder>() {

    var driverActionButtonList: List<ShipmentInfoUiModel.CourierDriverInfoUiModel.Button> =
        emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_buyer_order_detail_courier_driver_icon, parent, false)
        return ViewHolder(view, driver, navigator)
    }

    override fun getItemCount(): Int {
        return driverActionButtonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(driverActionButtonList.getOrNull(position))
    }

    class ViewHolder(
        itemView: View,
        private val driver: ShipmentInfoUiModel.CourierDriverInfoUiModel,
        private val navigator: BuyerOrderDetailNavigator
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding: ItemBuyerOrderDetailCourierDriverIconBinding? by viewBinding()

        fun bind(button: ShipmentInfoUiModel.CourierDriverInfoUiModel.Button?) {
            setButtonIcon(button)
            setupListener(button)
        }

        private fun setButtonIcon(button: ShipmentInfoUiModel.CourierDriverInfoUiModel.Button?) {
            binding?.btnIconActionDriver?.setImage(IconUnify.CALL)
        }

        private fun setupListener(button: ShipmentInfoUiModel.CourierDriverInfoUiModel.Button?) {
            binding?.btnIconActionDriver?.setOnClickListener {
                button?.let {
                    when (it.actionValue) {
                        Action.URL.value -> {
                            navigator.openWebView(it.value, shouldRefreshWhenBack = true)
                        }
                        Action.APPLINK.value -> {
                            navigator.openAppLink(it.value, shouldRefreshWhenBack = true)
                        }
                        Action.ACTION.value -> {
                            if (it.value == CALL_DRIVER) {
                                navigator.goToCallingPage(driver.phoneNumber)
                            }
                        }
                    }
                }
            }
        }

        enum class Action (val value: String) {
            URL("URL"),
            APPLINK("APPLINK"),
            ACTION("ACTION")
        }

        companion object {
            private const val CALL_DRIVER = "call_driver"
        }
    }
}
