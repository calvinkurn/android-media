package com.tokopedia.settingbank.choosebank.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel

/**
 * @author by nisie on 6/22/18.
 */
interface BankTypeFactory{

    fun type(viewModel: BankViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}