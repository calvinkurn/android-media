package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.databinding.ItemEmptyViewPaymentBinding
import com.tokopedia.payment.setting.list.view.listener.SettingListActionListener

class SettingListEmptyViewHolder(
    view: View?,
    private val listenerEmptyViewHolder: SettingListActionListener?
) : AbstractViewHolder<Visitable<SettingListPaymentAdapterTypeFactory>>(view) {

    private val binding = ItemEmptyViewPaymentBinding.bind(itemView)

    override fun bind(element: Visitable<SettingListPaymentAdapterTypeFactory>?) {
        binding.buttonAddCard?.setOnClickListener {
            listenerEmptyViewHolder?.onClickAddCard()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_empty_view_payment
    }
}
