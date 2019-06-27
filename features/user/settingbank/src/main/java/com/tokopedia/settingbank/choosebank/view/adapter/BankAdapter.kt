package com.tokopedia.settingbank.choosebank.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.EmptySearchBankViewModel

/**
 * @author by nisie on 6/22/18.
 */
class BankAdapter(adapterTypeFactory: BankTypeFactoryImpl,
                  listBank: ArrayList<Visitable<*>>)
    : BaseAdapter<BankTypeFactoryImpl>(adapterTypeFactory, listBank) {

    var emptyModel = EmptySearchBankViewModel()

    fun setSelected(adapterPosition: Int) {
        if(adapterPosition >= 0){
            for (visitable in visitables) {
                (visitable as BankViewModel).isSelected = false
            }
            (this.visitables[adapterPosition] as BankViewModel).isSelected = true
            this.notifyDataSetChanged()
        }
    }

    fun setList(listBank: ArrayList<BankViewModel>) {
        this.visitables.clear()
        this.visitables.addAll(listBank)
        this.notifyDataSetChanged()
    }

    fun showSearchNotFound() {
        this.visitables.clear()
        this.visitables.add(emptyModel)
        this.notifyDataSetChanged()
    }

    fun hideSearchNotFound() {
        this.visitables.clear()
        this.notifyDataSetChanged()
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