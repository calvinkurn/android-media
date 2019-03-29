package com.tokopedia.settingbank.choosebank.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingbank.choosebank.view.adapter.viewholder.BankViewHolder
import com.tokopedia.settingbank.choosebank.view.adapter.viewholder.EmptySearchBankViewHolder
import com.tokopedia.settingbank.choosebank.view.listener.BankListener
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.EmptySearchBankViewModel

/**
 * @author by nisie on 6/22/18.
 */

class BankTypeFactoryImpl(private val bankListener: BankListener) :
        BaseAdapterTypeFactory(),
        BankTypeFactory {

    override fun type(viewModel: BankViewModel): Int {
        return BankViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptySearchBankViewModel): Int {
        return EmptySearchBankViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            BankViewHolder.LAYOUT -> BankViewHolder(view, bankListener)
            EmptySearchBankViewHolder.LAYOUT -> EmptySearchBankViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }
    }

}