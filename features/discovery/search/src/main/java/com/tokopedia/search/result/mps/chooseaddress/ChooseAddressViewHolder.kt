package com.tokopedia.search.result.mps.chooseaddress

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchMpsChooseAddressLayoutBinding
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.utils.view.binding.viewBinding

class ChooseAddressViewHolder(
    itemView: View,
    private val fragmentProvider: FragmentProvider,
    private val chooseAddressListener: ChooseAddressListener,
): AbstractViewHolder<ChooseAddressDataView>(itemView) {

    private var binding: SearchMpsChooseAddressLayoutBinding? by viewBinding()

    init {
        configureChooseAddress()
    }

    private fun configureChooseAddress() {
        binding?.searchChooseAddress?.bindChooseAddress(object: ChooseAddressWidget.ChooseAddressWidgetListener {
            override fun onLocalizingAddressUpdatedFromWidget() {
                chooseAddressListener.onLocalizingAddressSelected()
            }

            override fun onLocalizingAddressUpdatedFromBackground() { }

            override fun onLocalizingAddressServerDown() {
                binding?.searchChooseAddress?.hide()
            }

            override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
                if (!isRollOutUser) binding?.searchChooseAddress?.hide()
            }

            override fun getLocalizingAddressHostFragment() = fragmentProvider.getFragment()

            override fun getLocalizingAddressHostSourceData() = SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH

            override fun getLocalizingAddressHostSourceTrackingData() = SOURCE

            override fun onLocalizingAddressLoginSuccess() {

            }
        })
    }

    override fun bind(element: ChooseAddressDataView) {
        binding?.searchChooseAddress?.updateWidget()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_mps_choose_address_layout

        private const val SOURCE = "search page"
    }
}
