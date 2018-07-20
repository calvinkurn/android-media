package com.tokopedia.settingbank.choosebank.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel

/**
 * @author by nisie on 6/22/18.
 */
class BankAdapter(adapterTypeFactory: BankTypeFactoryImpl,
                  listBank: ArrayList<Visitable<*>>)
    : BaseAdapter<BankTypeFactoryImpl>(adapterTypeFactory, listBank) {

    fun setSelected(adapterPosition: Int) {
        for (visitable in visitables) {
            (visitable as BankViewModel).isSelected = false
        }
        (this.visitables[adapterPosition] as BankViewModel).isSelected = true
        this.notifyItemChanged(adapterPosition)
    }

    fun setList(listBank: ArrayList<BankViewModel>) {
        this.visitables.clear()
        this.visitables.addAll(listBank)
        this.notifyDataSetChanged()
    }

    fun showSearchNotFound() {
        //TODO
    }

    fun hideSearchNotFound() {
        //TODO
    }

    fun setSelectedFromBankId(bankId: String?) {
        if (!bankId.isNullOrEmpty()) {
            for (visitable in visitables) {
                (visitable as BankViewModel).isSelected = bankId.equals(visitable.bankId)
            }
            notifyDataSetChanged()
        }
    }


}