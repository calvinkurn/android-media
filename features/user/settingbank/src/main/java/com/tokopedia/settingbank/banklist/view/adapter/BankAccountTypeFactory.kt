package com.tokopedia.settingbank.banklist.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/12/18.
 */
interface BankAccountTypeFactory{

    fun type(viewModel: BankAccountViewModel): Int

    fun type(emptyModel: EmptyModel):Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}