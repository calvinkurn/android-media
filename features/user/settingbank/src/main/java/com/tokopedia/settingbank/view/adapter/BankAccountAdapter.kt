package com.tokopedia.settingbank.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.settingbank.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/12/18.
 */

class BankAccountAdapter(adapterTypeFactory: BankAccountTypeFactoryImpl,
                         listBank: ArrayList<Visitable<*>>)
    : BaseAdapter<BankAccountTypeFactoryImpl>(adapterTypeFactory, listBank) {

    val emptyModel: EmptyModel = EmptyModel()

    fun addList(list: ArrayList<BankAccountViewModel>) {
        val lastPosition = this.visitables.size
        this.visitables.addAll(list)
        notifyItemRangeInserted(lastPosition, list.size)
    }

    fun setMain(adapterPosition: Int) {
        val tempAccount: BankAccountViewModel = this.visitables[adapterPosition] as BankAccountViewModel
        tempAccount.isDefaultBank = true

        if (this.visitables[0] is BankAccountViewModel) {
            (this.visitables[0] as BankAccountViewModel).isDefaultBank = false
        }
        notifyItemChanged(0)

        this.visitables.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)

        this.visitables.add(0, tempAccount)
        notifyItemInserted(0)

    }

    fun remove(adapterPosition: Int) {
        this.visitables.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    fun showEmpty() {
        this.visitables.add(emptyModel)
        notifyItemInserted(0)
    }

    fun checkLoadMore(index: Int): Boolean {
        return if (index == itemCount - 1) {
            this.visitables[index] is LoadingMoreModel
        } else false
    }
}


