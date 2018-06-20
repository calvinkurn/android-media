package com.tokopedia.settingbank.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingbank.view.listener.BankAccountPopupListener
import com.tokopedia.settingbank.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/12/18.
 */
class BankAccountTypeFactoryImpl(private val popupListener: BankAccountPopupListener) : BaseAdapterTypeFactory(),
        BankAccountTypeFactory {

    override fun type(viewModel: BankAccountViewModel): Int {
        return BankAccountViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        if (viewType == BankAccountViewHolder.LAYOUT) {
            viewHolder = BankAccountViewHolder(view, popupListener)
        } else {
            viewHolder = super.createViewHolder(view, viewType)
        }
        return viewHolder
    }

}