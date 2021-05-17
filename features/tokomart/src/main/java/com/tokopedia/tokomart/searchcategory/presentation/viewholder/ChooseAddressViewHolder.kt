package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView

class ChooseAddressViewHolder(
        itemView: View,
        chooseAddressListener: ChooseAddressListener
): AbstractViewHolder<ChooseAddressDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_search_category_choose_address
    }

    private var chooseAddressWidget: ChooseAddressWidget? = null

    init {
        chooseAddressWidget = itemView.findViewById<ChooseAddressWidget?>(R.id.tokomartSearchCategoryChooseAddress)
        chooseAddressWidget?.bindChooseAddress(object: ChooseAddressWidget.ChooseAddressWidgetListener {
            override fun onLocalizingAddressUpdatedFromWidget() {
                chooseAddressListener.onLocalizingAddressSelected()
            }

            override fun onLocalizingAddressUpdatedFromBackground() { }

            override fun onLocalizingAddressServerDown() {
                chooseAddressWidget?.hide()
            }

            override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
                if (!isRollOutUser) chooseAddressWidget?.hide()
            }

            override fun getLocalizingAddressHostFragment() = chooseAddressListener.getFragment()

            override fun getLocalizingAddressHostSourceData() = ""

            override fun getLocalizingAddressHostSourceTrackingData() = ""

            override fun onLocalizingAddressLoginSuccess() { }
        })
    }

    override fun bind(element: ChooseAddressDataView?) {
        chooseAddressWidget?.updateWidget()
    }
}