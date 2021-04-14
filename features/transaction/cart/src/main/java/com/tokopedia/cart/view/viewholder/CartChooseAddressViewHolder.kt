package com.tokopedia.cart.view.viewholder

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartChooseAddressBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartChooseAddressHolderData
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget

class CartChooseAddressViewHolder(private val binding: ItemCartChooseAddressBinding, val listener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_choose_address
        private const val SOURCE = "cart page"
    }

    fun bind(data: CartChooseAddressHolderData) {
        binding.cartChooseAddressWidget.bindChooseAddress(object : ChooseAddressWidget.ChooseAddressWidgetListener {
            override fun onLocalizingAddressUpdatedFromWidget() {
                listener?.onLocalizingAddressUpdatedFromWidget()
            }

            override fun onLocalizingAddressUpdatedFromBackground() {
                // No-op
            }

            override fun onLocalizingAddressServerDown() {
                listener?.onNeedToGoneLocalizingAddressWidget()
            }

            override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
                if (!isRollOutUser) {
                    listener?.onNeedToGoneLocalizingAddressWidget()
                }
            }

            override fun getLocalizingAddressHostFragment(): Fragment {
                return listener?.getFragment() ?: Fragment()
            }

            override fun getLocalizingAddressHostSourceData(): String {
                return data.hostFragment
            }

            override fun getLocalizingAddressHostSourceTrackingData(): String {
                return SOURCE
            }

            override fun onLocalizingAddressLoginSuccess() {
                // No-op
            }
        })
    }

}