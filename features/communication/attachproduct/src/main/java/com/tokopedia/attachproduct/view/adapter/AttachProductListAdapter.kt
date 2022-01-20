package com.tokopedia.attachproduct.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachproduct.databinding.ItemProductAttachBinding
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel
import com.tokopedia.attachproduct.view.viewholder.AttachProductListItemViewHolder
import java.util.*

class AttachProductListAdapter(private val baseListAdapterTypeFactory: AttachProductListAdapterTypeFactory)
    : BaseListAdapter<AttachProductItemUiModel,
        AttachProductListAdapterTypeFactory>(baseListAdapterTypeFactory) {
    private var productIds: HashSet<String> = HashSet()
    private var checkedList: ArrayList<AttachProductItemUiModel> = ArrayList()

    override fun getData(): List<AttachProductItemUiModel> {
        return super.getData()
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is BaseCheckableViewHolder<*>) {
            holder.checkable.isChecked = isChecked(position)
            holder.itemView.isSelected = isChecked(position)
        }
    }

    private fun getDataRow(position: Int): AttachProductItemUiModel? {
        return if (position < 0 || position >= visitables.size) {
            null
        } else {
            visitables[position] as AttachProductItemUiModel
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

    private fun addToCheckedDataList(productItemViewModel: AttachProductItemUiModel) {
        checkedList.add(productItemViewModel)
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

    val checkedDataList: List<AttachProductItemUiModel>
        get() = checkedList
}
