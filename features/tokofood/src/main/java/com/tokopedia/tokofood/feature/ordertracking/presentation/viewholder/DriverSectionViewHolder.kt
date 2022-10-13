package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingDriverSectionBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.DriverInformationAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverInformationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel

class DriverSectionViewHolder(
    view: View,
    private val listener: Listener
) : BaseOrderTrackingViewHolder<DriverSectionUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_driver_section
    }

    private val binding = ItemTokofoodOrderTrackingDriverSectionBinding.bind(itemView)

    override fun bind(element: DriverSectionUiModel) {
        with(binding) {
            setDriverName(element.name)
            setDriverPhotoUrl(element.photoUrl)
            setLicensePlatNumber(element.licensePlateNumber)
            setupDriverCall(element.isCallable)
            setupDriverChat(element.isEnableChat, element.goFoodOrderNumber)
            setupDriverInformationAdapter(element.driverInformationList)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let {
            val (oldItem, newItem) = it
            if (oldItem is DriverSectionUiModel && newItem is DriverSectionUiModel) {
                if (oldItem.name != newItem.name) {
                    binding.setDriverName(newItem.name)
                }
                if (oldItem.photoUrl != newItem.photoUrl) {
                    binding.setDriverPhotoUrl(newItem.photoUrl)
                }
                if (oldItem.licensePlateNumber != newItem.licensePlateNumber) {
                    binding.setLicensePlatNumber(newItem.licensePlateNumber)
                }
                if (oldItem.isCallable != newItem.isCallable) {
                    binding.setupDriverCall(newItem.isCallable)
                }
                if (oldItem.isEnableChat != newItem.isEnableChat ||
                    oldItem.goFoodOrderNumber != newItem.goFoodOrderNumber
                ) {
                    binding.setupDriverChat(newItem.isEnableChat, newItem.goFoodOrderNumber)
                }
            }
        }
    }

    private fun ItemTokofoodOrderTrackingDriverSectionBinding.setDriverName(driverName: String) {
        tvOrderTrackingDriverName.text = driverName
    }

    private fun ItemTokofoodOrderTrackingDriverSectionBinding.setDriverPhotoUrl(
        driverPhotoUrl: String
    ) {
        imgOrderTrackingDriver.setImageUrl(driverPhotoUrl)
    }

    private fun ItemTokofoodOrderTrackingDriverSectionBinding.setLicensePlatNumber(platNumber: String) {
        tvOrderTrackingDriverPlatNumber.text = platNumber
    }

    private fun ItemTokofoodOrderTrackingDriverSectionBinding.setupDriverCall(isCallable: Boolean) {
        icDriverCall.run {
            var (isClickableCall, callIconColor) = getIsEnableAndColorIcons(root.context, isCallable)
            callIconColor = if(callIconColor != Int.ZERO) callIconColor else null

            isClickable = isClickableCall
            setImage(IconUnify.CALL, callIconColor, callIconColor)

            if (isClickableCall) {
                setOnClickListener {
                    listener.onClickDriverCall()
                }
            }
        }
    }

    private fun ItemTokofoodOrderTrackingDriverSectionBinding.setupDriverChat(
        isEnableChat: Boolean,
        goFoodOrderNumber: String
    ) {
        icDriverChat.run {
            if (isShowDriverChat()) {
                show()
                var (isClickableCall, chatIconColor) = getIsEnableAndColorIcons(root.context, isEnableChat)
                chatIconColor = if(chatIconColor != Int.ZERO) chatIconColor else null

                isClickable = isClickableCall
                setImage(IconUnify.CHAT, chatIconColor, chatIconColor)

                if (isClickableCall) {
                    setOnClickListener {
                        listener.onClickDriverChat(goFoodOrderNumber)
                    }
                }
            } else {
                hide()
            }
        }
    }


    private fun getIsEnableAndColorIcons(context: Context, isEnable: Boolean): Pair<Boolean, Int?> {
       return if (isEnable) {
            val nn900Color =
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN900
                )
            Pair(true, nn900Color)
        } else {
            val nn300Color =
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN300
                )
            Pair(false, nn300Color)
        }
    }

    private fun ItemTokofoodOrderTrackingDriverSectionBinding.setupDriverInformationAdapter(
        driverInformationList: List<DriverInformationUiModel>
    ) {
        if (driverInformationList.isNotEmpty()) {
            rvDriverInformation.run {
                show()
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = DriverInformationAdapter(driverInformationList)
            }
        } else {
            rvDriverInformation.hide()
        }
    }


    private fun isShowDriverChat(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.KEY_ROLLENCE_TOKOCHAT, ""
            ) == RollenceKey.KEY_ROLLENCE_TOKOCHAT
        } catch (e: Exception) {
            true
        }
    }

    interface Listener {
        fun onClickDriverCall()
        fun onClickDriverChat(goFoodOrderNumber: String)
    }
}
