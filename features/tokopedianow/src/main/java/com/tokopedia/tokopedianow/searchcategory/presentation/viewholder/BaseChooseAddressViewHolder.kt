package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView

abstract class BaseChooseAddressViewHolder(
        itemView: View,
        chooseAddressListener: ChooseAddressListener
): AbstractViewHolder<ChooseAddressDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_choose_address
    }

    private var chooseAddressWidget: ChooseAddressWidget? = null

    protected abstract val trackingSource: String

    init {
        chooseAddressWidget = itemView.findViewById(R.id.tokoNowSearchCategoryChooseAddress)
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

            override fun getLocalizingAddressHostSourceData() = SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH

            override fun getLocalizingAddressHostSourceTrackingData() = trackingSource

            override fun onLocalizingAddressLoginSuccess() { }
        })
    }

    override fun bind(element: ChooseAddressDataView?) {
        chooseAddressWidget?.updateWidget()
    }
}