package com.tokopedia.settingbank.choosebank.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel

/**
 * @author by nisie on 6/22/18.
 */
class BankAdapter(adapterTypeFactory: BankTypeFactoryImpl,
                  listBank: ArrayList<Visitable<*>>)
    : BaseAdapter<BankTypeFactoryImpl>(adapterTypeFactory, listBank) {


    fun checkLoadMore(index: Int): Boolean {
        return if (index == itemCount - 1) {
            this.visitables[index] is LoadingMoreModel
        } else false
    }

    fun setSelected(adapterPosition: Int) {
        //TODO REMOVE LAST SELECTED ITEM
        (this.visitables[adapterPosition] as BankViewModel).isSelected = true
    }

    fun addList(listBank: BankListViewModel) {
        val lastPosition = this.visitables.size
        this.visitables.addAll(listBank.list!!)
        this.notifyItemRangeInserted(lastPosition, listBank.list.size)
    }

}