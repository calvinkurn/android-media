package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartChooseAddressHolderData
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget

class CartChooseAddressViewHolder(val view: View, val listener: ActionListener?) : RecyclerView.ViewHolder(view) {

    private val chooseAddressWidget by lazy { view.findViewById<ChooseAddressWidget>(R.id.cart_choose_address_widget) }

    companion object {
        val LAYOUT = R.layout.item_cart_choose_address
    }

    fun bind(data: CartChooseAddressHolderData) {
        chooseAddressWidget.bindChooseAddress(object : ChooseAddressWidget.ChooseAddressWidgetListener {
            override fun onLocalizingAddressUpdatedFromWidget() {

            }

            override fun onLocalizingAddressUpdatedFromBackground() {

            }

            override fun onLocalizingAddressServerDown() {

            }

            override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {

            }

            override fun getLocalizingAddressHostFragment(): Fragment {
                return listener?.getFragment() ?: Fragment()
            }

            override fun getLocalizingAddressHostSourceData(): String {
                return data.hostFragment
            }
        })
    }

}