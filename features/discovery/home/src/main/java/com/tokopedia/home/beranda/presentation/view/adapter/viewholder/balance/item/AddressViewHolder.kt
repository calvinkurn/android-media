package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.databinding.LayoutDynamicBalanceAddressBinding
import com.tokopedia.home.R as homeR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class AddressViewHolder(
    v: View,
): AbstractViewHolder<BalanceItemUiModel>(v) {
    companion object {
        @SuppressLint("PII Data Exposure")
        val LAYOUT = homeR.layout.layout_dynamic_balance_address
    }

    private val binding: LayoutDynamicBalanceAddressBinding? by viewBinding()

    override fun bind(
        model: BalanceItemUiModel,
    ) {
//        if (needToShowChooseAddress) {
//            it.setPadding(Int.ZERO, Int.ZERO, Int.ZERO, paddingBottomChooseAddress)
//            listener.initializeChooseAddressWidget(it, needToShowChooseAddress)
//        } else {
//            it.gone()
//        }
    }

    override fun bind(
        model: BalanceItemUiModel,
        payloads: MutableList<Any>
    ) {
        if(payloads.isNotEmpty()) {
        } else {
            bind(model)
        }
    }
}
