package com.tokopedia.buyerorderdetail.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailCourierDriverIconBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.CourierButtonListener
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.utils.view.binding.viewBinding

class CourierActionButtonAdapter(
    private val navigator: BuyerOrderDetailNavigator,
    private val courierButtonListener: CourierButtonListener
) : RecyclerView.Adapter<CourierActionButtonAdapter.ViewHolder>() {

    var driverActionButtonList: List<ShipmentInfoUiModel.CourierDriverInfoUiModel.Button> =
        emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_buyer_order_detail_courier_driver_icon, parent, false)
        return ViewHolder(view, navigator, courierButtonListener)
    }

    override fun getItemCount(): Int {
        return driverActionButtonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(driverActionButtonList.getOrNull(position))
    }

    class ViewHolder(
        itemView: View,
        private val navigator: BuyerOrderDetailNavigator,
        private val courierButtonListener: CourierButtonListener
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding: ItemBuyerOrderDetailCourierDriverIconBinding? by viewBinding()

        init {
            revertParentsClip()
        }

        fun bind(button: ShipmentInfoUiModel.CourierDriverInfoUiModel.Button?) {
            setButtonIcon(button)
            setupChatCounter(button)
            setupListener(button)
        }

        private fun setButtonIcon(button: ShipmentInfoUiModel.CourierDriverInfoUiModel.Button?) {
            binding?.btnIconActionDriver?.setImageWithUnifyIcon(button?.icon.toIntOrZero())
        }

        private fun setupListener(button: ShipmentInfoUiModel.CourierDriverInfoUiModel.Button?) {
            binding?.btnIconActionDriver?.setOnClickListener {
                button?.let {
                    when (it.actionValue) {
                        Action.URL.value -> {
                            navigator.openAppLink(it.value, shouldRefreshWhenBack = true)
                            if (it.key == CHAT_DRIVER) {
                                trackClickChatButton(button.value, button.counter)
                            }
                        }
                        Action.ACTION.value -> {
                            if (it.key == CALL_DRIVER) {
                                navigator.goToCallingPage(button.value)
                            }
                        }
                    }
                }
            }
        }

        private fun setupChatCounter(button: ShipmentInfoUiModel.CourierDriverInfoUiModel.Button?) {
            if (button?.key == CHAT_DRIVER) {
                val queryParams = getQueryParamFromApplink(
                    button.value,
                    ApplinkConst.TokoChat.ORDER_ID_GOJEK,
                    ApplinkConst.TokoChat.PARAM_SOURCE
                )
                if (queryParams.size >= TOTAL_PARAMS_NEEDED) {
                    courierButtonListener.initGroupBooking(
                        queryParams[0],
                        queryParams[1]
                    )
                }
                binding?.btnIconActionDriver?.apply {
                    val strCounter = getStringCounter(button.counter)
                    notificationRef.setNotification(
                        notif = strCounter,
                        notificationType = NotificationUnify.COUNTER_TYPE,
                        colorType = NotificationUnify.COLOR_PRIMARY
                    )
                    val xPosition = getXPosition(strCounter)
                    setNotifXY(
                        xPosition,
                        ICON_PERCENTAGE_Y_POSITION
                    )
                    notificationRef.setVisibility(strCounter)
                }
            } else {
                binding?.btnIconActionDriver?.notificationRef?.setVisibility("")
            }
        }

        private fun getQueryParamFromApplink(
            applink: String,
            vararg params: String
        ): List<String> {
            val result = mutableListOf<String>()
            val uri = Uri.parse(applink)
            params.forEach {
                uri.getQueryParameter(it)?.let { arg ->
                    result.add(arg)
                }
            }
            return result
        }

        private fun getXPosition(strCounter: String): Float {
            return if (strCounter.length > THRESHOLD_STR_COUNTER_LENGTH) {
                ICON_DEFAULT_PERCENTAGE_X_POSITION
            } else {
                ICON_MAX_PERCENTAGE_X_POSITION
            }
        }

        private fun getStringCounter(counter: Int): String {
            return when {
                (counter < 1) -> ""
                (counter > 99) -> "99+"
                else -> counter.toString()
            }
        }

        private fun NotificationUnify.setVisibility(strCounter: String) {
            if (strCounter.isBlank() || strCounter.toIntOrZero() <= 0) {
                setBackgroundDrawable(null)
                gone()
            } else {
                show()
            }
        }

        private fun trackClickChatButton(applink: String, counter: Int) {
            val queryParams = getQueryParamFromApplink(
                applink,
                ApplinkConst.TokoChat.ORDER_ID_GOJEK,
                ApplinkConst.TokoChat.PARAM_SOURCE
            )
            if (queryParams.size >= TOTAL_PARAMS_NEEDED) {
                courierButtonListener.onChatButtonClicked(
                    queryParams[0],
                    queryParams[1],
                    getStringCounter(counter)
                )
            }
        }

        private fun revertParentsClip() {
            binding?.btnIconActionDriver?.doOnLayout {
                var view: Any? = binding?.root?.parent
                while (view is ViewGroup) {
                    view.clipChildren = true
                    view = view.parent
                }
                revertParentsClip()
            }
        }

        enum class Action(val value: String) {
            URL("URL"),
            ACTION("ACTION")
        }

        companion object {
            private const val CALL_DRIVER = "call_driver"
            const val CHAT_DRIVER = "chat_driver"

            private const val ICON_DEFAULT_PERCENTAGE_X_POSITION = 0.9f
            private const val ICON_MAX_PERCENTAGE_X_POSITION = 0.75f
            private const val ICON_PERCENTAGE_Y_POSITION = -0.25f

            private const val TOTAL_PARAMS_NEEDED = 2
            private const val THRESHOLD_STR_COUNTER_LENGTH = 2
        }
    }
}
