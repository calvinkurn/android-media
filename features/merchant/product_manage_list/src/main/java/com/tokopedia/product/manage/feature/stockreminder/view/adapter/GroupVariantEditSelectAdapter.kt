package com.tokopedia.product.manage.feature.stockreminder.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.product.manage.databinding.ItemVariantEditStockReminderSelectBinding
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.REMINDER_ACTIVE
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.REMINDER_INACTIVE
import com.tokopedia.product.manage.feature.stockreminder.view.data.GroupVariantProductStockReminderUiModel
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify

class GroupVariantEditSelectAdapter(val onSelectionListener: OnSelectionListener) :
    RecyclerView.Adapter<GroupVariantEditSelectAdapter.GroupVariantEditSelectViewHolder>() {

    private var groupVariant: List<GroupVariantProductStockReminderUiModel> = emptyList()

    private var isCheckAll = false

    private val productSelection = mutableListOf<ProductStockReminderUiModel>()
    private val dataProducts = mutableListOf<ProductStockReminderUiModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupVariantEditSelectViewHolder {
        val binding = ItemVariantEditStockReminderSelectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GroupVariantEditSelectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupVariantEditSelectViewHolder, position: Int) {
        dataProducts.addAll(groupVariant[position].variants)
        holder.bind(groupVariant[position], isCheckAll)
    }

    override fun getItemCount() = groupVariant.size

    fun setItems(data: List<GroupVariantProductStockReminderUiModel>) {
        this.groupVariant = data
        productSelection.clear()
        notifyItemRangeChanged(Int.ZERO, data.size)
    }

    fun setCheckAll(isCheckAll: Boolean) {
        this.isCheckAll = isCheckAll
        productSelection.clear()
        notifyItemRangeChanged(Int.ZERO, groupVariant.size)
    }

    inner class GroupVariantEditSelectViewHolder(private val binding: ItemVariantEditStockReminderSelectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(groupVariant: GroupVariantProductStockReminderUiModel, isCheckAll: Boolean) {
            if (groupVariant.variants.size == Int.ONE) {
                binding.textSelection.gone()
            } else {
                binding.textSelection.text = groupVariant.groupVariantName
            }
            val dataList = mapToItemListUnify(groupVariant.variants)
            binding.listUnifySelection.setData(dataList)
            binding.listUnifySelection.onLoadFinish {
                dataList.forEachIndexed { position, listItemUnify ->
                    listItemUnify.listRightCheckbox?.setOnCheckedChangeListener { compoundButton, isCheck ->
                        if (isCheck) {
                            checkProductSelection(groupVariant.variants[position],REMINDER_ACTIVE)
                        } else {
                            checkProductSelection(groupVariant.variants[position],REMINDER_INACTIVE)
                        }
                        onSelectionListener.onSelection(productSelection)
                    }
                    listItemUnify.listRightCheckbox?.isChecked = isCheckAll
                }
            }

        }

        private fun checkProductSelection(
            product: ProductStockReminderUiModel,
            status: Int
        ) {
            val isFound = productSelection.firstOrNull() { it.id == product.id }
            if (isFound != null) {
                if (status == REMINDER_INACTIVE) {
                    val index = productSelection.indexOf(isFound)
                    productSelection.removeAt(index)
                } else {
                    updateProductWareHouseList(product.id) {
                        it.copy(stockAlertStatus = status)
                    }
                }

            } else {
                productSelection.add(
                    ProductStockReminderUiModel(
                        id = product.id,
                        productName = product.productName,
                        stockAlertCount = product.stockAlertCount,
                        stockAlertStatus = product.stockAlertStatus,
                        stock = product.stock,
                        variantFirst = product.variantFirst,
                        variantSecond = product.variantSecond,
                        maxStock = product.maxStock,
                        productParentName = product.productParentName
                    )
                )
            }
        }

        private fun getProductWareHouseList(): List<ProductStockReminderUiModel> {
            return productSelection.toList()
        }

        private fun updateProductWareHouseList(
            productId: String,
            block: (ProductStockReminderUiModel) -> ProductStockReminderUiModel
        ) {
            getProductWareHouseList().firstOrNull { it.id == productId }?.let {
                val index = productSelection.indexOf(it)
                productSelection[index] = block.invoke(it)
            }
        }

        private fun mapToItemListUnify(
            data: List<ProductStockReminderUiModel>
        ) =
            ArrayList(data.mapIndexed { index, _ ->
                val variantSecond = data[index].variantSecond.ifEmpty {
                    data[index].variantFirst
                }
                val listItem = ListItemUnify(variantSecond, "")
                listItem.setVariant(null, ListItemUnify.CHECKBOX, data[index].variantFirst)
                listItem.isBold = false
                return@mapIndexed listItem
            })


    }

    interface OnSelectionListener {
        fun onSelection(productSelection: List<ProductStockReminderUiModel>)
    }
}
