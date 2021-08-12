package com.tokopedia.attachproduct.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel
import java.util.*

class NewAttachProductListAdapter(baseListAdapterTypeFactoryNew: NewAttachProductListAdapterTypeFactory?)
    : BaseListAdapter<NewAttachProductItemUiModel,
        NewAttachProductListAdapterTypeFactory>(baseListAdapterTypeFactoryNew) {
    private var productIds: HashSet<String> = HashSet()
    private var checkedList: ArrayList<NewAttachProductItemUiModel> = ArrayList()

    override fun getData(): List<NewAttachProductItemUiModel> {
        return super.getData()
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is BaseCheckableViewHolder<*>) {
            holder.checkable.isChecked = isChecked(position)
            holder.itemView.isSelected = isChecked(position)
        }
    }

    private fun getDataRow(position: Int): NewAttachProductItemUiModel? {
        return if (position < 0 || position >= visitables.size) {
            null
        } else {
            visitables[position] as NewAttachProductItemUiModel
        }
    }

    fun itemChecked(isChecked: Boolean, position: Int) {
        val product = getDataRow(position)
        if (product != null) {
            val productId = product.productId
            if (isChecked) {
                productIds.add(productId)
                addToCheckedDataList(product)
            } else {
                productIds.remove(productId)
                removeFromCheckedDataList(productId)
            }
        }
    }

    private fun addToCheckedDataList(productItemViewModelNew: NewAttachProductItemUiModel) {
        checkedList.add(productItemViewModelNew)
    }

    private fun removeFromCheckedDataList(productId: String) {
        val iterator = checkedList.iterator()
        while (iterator.hasNext()) {
            val itemViewModel = iterator.next()
            if (itemViewModel.productId == productId) {
                iterator.remove()
                return
            }
        }
    }

    val checkedCount: Int
        get() = productIds.size

    fun isChecked(position: Int): Boolean {
        var productId = "0"
        val attachProductItemUiModel = getDataRow(position)
        if (attachProductItemUiModel != null) {
            productId = attachProductItemUiModel.productId
        }
        return productIds.contains(productId)
    }

    val checkedDataList: List<NewAttachProductItemUiModel>
        get() = checkedList
}
