package com.tokopedia.settingbank.banklist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingbank.banklist.view.adapter.viewholder.BankAccountViewHolder
import com.tokopedia.settingbank.banklist.view.adapter.viewholder.EmptyBankAccountViewHolder
import com.tokopedia.settingbank.banklist.view.listener.BankAccountPopupListener
import com.tokopedia.settingbank.banklist.view.listener.EmptyBankAccountListener
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/12/18.
 */
class BankAccountTypeFactoryImpl(private val popupListener: BankAccountPopupListener,
                                 private val emptyBankAccountListener: EmptyBankAccountListener) :
        BaseAdapterTypeFactory(),
        BankAccountTypeFactory {
    override fun type(emptyModel: EmptyModel): Int {
        return EmptyBankAccountViewHolder.LAYOUT
    }

    override fun type(viewModel: BankAccountViewModel): Int {
        return BankAccountViewHolder.LAYOUT
    }


    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            BankAccountViewHolder.LAYOUT -> BankAccountViewHolder(view, popupListener)
            EmptyBankAccountViewHolder.LAYOUT -> EmptyBankAccountViewHolder(view, emptyBankAccountListener)
            else -> super.createViewHolder(view, viewType)
        }
    }

}