package com.tokopedia.search.result.shop.chooseaddress

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultShopChooseAddressLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

internal class ChooseAddressViewHolder(
    itemView: View,
    private val chooseAddressListener: ChooseAddressListener
): AbstractViewHolder<ChooseAddressDataView>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_choose_address_layout

        private const val SOURCE = "search page"
    }

    private var binding: SearchResultShopChooseAddressLayoutBinding? by viewBinding()

    init {
        configureChooseAddress()
    }

    private fun configureChooseAddress() {
        binding?.searchShopChooseAddress?.bindChooseAddress(object: ChooseAddressWidget.ChooseAddressWidgetListener {
            override fun onLocalizingAddressUpdatedFromWidget() {
                chooseAddressListener.onLocalizingAddressSelected()
            }

            override fun onLocalizingAddressUpdatedFromBackground() { }

            override fun onLocalizingAddressServerDown() {
                binding?.searchShopChooseAddressContainer?.hide()
            }

            override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
                if (!isRollOutUser) binding?.searchShopChooseAddressContainer?.hide()
            }

            override fun getLocalizingAddressHostFragment(): Fragment = chooseAddressListener.getFragment()

            override fun getLocalizingAddressHostSourceData(): String = SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH

            override fun getLocalizingAddressHostSourceTrackingData() = SOURCE

            override fun onLocalizingAddressLoginSuccess() { }

        })
    }

    override fun bind(element: ChooseAddressDataView?) {
        bindChooseAddressWidget()
    }

    private fun bindChooseAddressWidget() {
        binding?.searchShopChooseAddress?.updateWidget()
    }
}