package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingRestoUserAddressBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.RestaurantUserAddressUiModel

class RestaurantUserAddressViewHolder(view: View):
    AbstractViewHolder<RestaurantUserAddressUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_resto_user_address
    }

    private val binding = ItemTokofoodOrderTrackingRestoUserAddressBinding.bind(itemView)

    override fun bind(element: RestaurantUserAddressUiModel) {
        with(binding) {
            setMerchantName(element.merchantName)
            setMerchantDistance(element.distanceInKm)
            setDestinationName(element.destinationLabel)
            setUserPhoneNumber(element.destinationPhone)
            setDestinationAddress(element.destinationAddress)
        }
    }

    private fun ItemTokofoodOrderTrackingRestoUserAddressBinding.setMerchantName(merchantName: String) {
        tvOrderTrackerMerchantName.text = merchantName
    }

    private fun ItemTokofoodOrderTrackingRestoUserAddressBinding.setMerchantDistance(distanceInKm: String) {
        tvDistanceFromMerchant.text = getString(R.string.order_tracking_distance_restaurant, distanceInKm)
    }

    private fun ItemTokofoodOrderTrackingRestoUserAddressBinding.setDestinationName(destinationName: String) {
        tvOrderTrackingAddressTitle.text = destinationName
    }

    private fun ItemTokofoodOrderTrackingRestoUserAddressBinding.setUserPhoneNumber(phoneNumber: String) {
        tvOrderTrackingPhoneNumber.text = phoneNumber
    }

    private fun ItemTokofoodOrderTrackingRestoUserAddressBinding.setDestinationAddress(address: String) {
        tvOrderTrackingDetailAddress.text = address
    }
}