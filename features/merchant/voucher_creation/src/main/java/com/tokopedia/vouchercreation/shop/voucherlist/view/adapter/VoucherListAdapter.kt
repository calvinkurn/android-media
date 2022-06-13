package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseVoucherListUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.VoucherListAdapterFactoryImpl

class VoucherListAdapter(val typeFactory: VoucherListAdapterFactoryImpl)
    : BaseListAdapter<BaseVoucherListUiModel, VoucherListAdapterFactoryImpl>(typeFactory) {

    override fun addElement(newItems: MutableList<out Visitable<Any>>) {
        changeDiff(visitables, visitables + newItems) {
            visitables.addAll(newItems)
        }
    }

    private fun changeDiff(oldList: List<Visitable<*>>,
                           newList: List<Visitable<*>>,
                           action: () -> Unit) {
        val diffUtilCallback = VoucherListDiffUtilCallback(oldList, newList)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
        action()
        diffUtilResult.dispatchUpdatesTo(this)
    }

    inner class VoucherListDiffUtilCallback(private val oldList: List<Visitable<*>>,
                                            private val newList: List<Visitable<*>>): DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            (oldItem as? VoucherUiModel)?.let { oldVoucher ->
                (newItem as? VoucherUiModel)?.let { newVoucher ->
                    return oldVoucher.id == newVoucher.id
                }
            }
            return true
        }
    }

}