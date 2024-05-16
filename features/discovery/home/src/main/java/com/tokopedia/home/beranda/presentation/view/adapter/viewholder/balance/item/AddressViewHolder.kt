package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.databinding.LayoutDynamicBalanceAddressBinding
import com.tokopedia.home.R as homeR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class AddressViewHolder(
    v: View,
    val listener: HomeCategoryListener,
): AbstractViewHolder<AddressUiModel>(v) {
    companion object {
        val LAYOUT = homeR.layout.layout_dynamic_balance_address
    }

    private val binding: LayoutDynamicBalanceAddressBinding? by viewBinding()

    @SuppressLint("PII Data Exposure")
    override fun bind(
        model: AddressUiModel,
    ) {
        val binding = binding ?: return
        listener.initializeChooseAddressWidget(binding.chooseAddressWidget, true)
        binding.chooseAddressWidget.updateWidget()
    }
}
