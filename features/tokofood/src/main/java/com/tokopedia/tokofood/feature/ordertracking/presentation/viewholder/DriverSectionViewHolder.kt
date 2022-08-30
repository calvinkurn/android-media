package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingDriverSectionBinding
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.DriverInformationAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverInformationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel

class DriverSectionViewHolder(
    view: View,
    private val listener: Listener
) : CustomPayloadViewHolder<DriverSectionUiModel>(view) {

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
            setupDriverCall()
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
                    binding.setupDriverCall()
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

    private fun ItemTokofoodOrderTrackingDriverSectionBinding.setupDriverCall() {
        icDriverCall.run {
            isClickable = true
            val nn900Color =
                ContextCompat.getColor(
                    root.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN900
                )
            setImage(IconUnify.CALL, nn900Color, nn900Color)
            setOnClickListener {
                listener.onClickDriverCall()
            }
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

    interface Listener {
        fun onClickDriverCall()
    }
}