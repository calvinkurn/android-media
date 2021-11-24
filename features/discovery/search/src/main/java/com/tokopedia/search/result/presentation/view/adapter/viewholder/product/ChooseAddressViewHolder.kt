package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductChooseAddressLayoutBinding
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.view.listener.ChooseAddressListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationClickListener
import com.tokopedia.utils.view.binding.viewBinding

internal class ChooseAddressViewHolder(
        itemView: View,
        private val chooseAddressListener: ChooseAddressListener,
        private val searchNavigationListener: SearchNavigationClickListener,
): AbstractViewHolder<ChooseAddressDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_choose_address_layout

        private const val SOURCE = "search page"
    }

    private var binding: SearchResultProductChooseAddressLayoutBinding? by viewBinding()

    init {
        configureChooseAddress()
    }

    private fun configureChooseAddress() {
        binding?.searchProductChooseAddress?.bindChooseAddress(object: ChooseAddressWidget.ChooseAddressWidgetListener {
            override fun onLocalizingAddressUpdatedFromWidget() {
                chooseAddressListener.onLocalizingAddressSelected()
            }

            override fun onLocalizingAddressUpdatedFromBackground() { }

            override fun onLocalizingAddressServerDown() {
                binding?.searchProductChooseAddressContainer?.hide()
            }

            override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
                if (!isRollOutUser) binding?.searchProductChooseAddressContainer?.hide()
            }

            override fun getLocalizingAddressHostFragment() = chooseAddressListener.getFragment()

            override fun getLocalizingAddressHostSourceData() = SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH

            override fun getLocalizingAddressHostSourceTrackingData() = SOURCE

            override fun onLocalizingAddressLoginSuccess() {

            }
        })
    }

    override fun bind(element: ChooseAddressDataView?) {
        bindChooseAddressWidget()
        bindChangeViewButton()
    }

    private fun bindChooseAddressWidget() {
        binding?.searchProductChooseAddress?.updateWidget()
    }

    private fun bindChangeViewButton() {
        binding?.searchProductChooseAddressChangeViewButton?.setOnClickListener {
            searchNavigationListener.onChangeViewClicked(adapterPosition)
        }
    }

    override fun bind(element: ChooseAddressDataView?, payloads: MutableList<Any>) {
        val payload = payloads.getOrNull(0) ?: return

        when(payload) {
            SearchConstant.RecyclerView.VIEW_LIST -> changeViewButton(IconUnify.VIEW_LIST)
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID -> changeViewButton(IconUnify.VIEW_GRID)
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID -> changeViewButton(IconUnify.VIEW_GRID_BIG)
        }
    }

    private fun changeViewButton(id: Int) {
        binding?.searchProductChooseAddressChangeViewButton?.setImage(newIconId = id)
    }
}