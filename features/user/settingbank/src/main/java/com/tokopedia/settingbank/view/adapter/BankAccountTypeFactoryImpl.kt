package com.tokopedia.settingbank.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingbank.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/12/18.
 */
class BankAccountTypeFactoryImpl : BaseAdapterTypeFactory(), BankAccountTypeFactory{


    override fun type(viewModel: BankAccountViewModel): Int {
        return BankAccountViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        var viewHolder: AbstractViewHolder<*>
        if (viewType == BankAccountViewHolder.LAYOUT) {
            viewHolder = BankAccountViewHolder(view)
        } else {
            viewHolder = super.createViewHolder(view, viewType)
        }
        return viewHolder
    }

}