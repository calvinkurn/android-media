package com.tokopedia.shop_showcase.shop_showcase_add.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.shop_showcase.databinding.ItemProductCardHorizontalBinding
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.listener.ShopShowcasePreviewListener
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewholder.ShowcaseProductPreviewViewHolder
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct

class ShopShowcaseAddAdapter(private val context: Context, private var listener: ShopShowcasePreviewListener) : RecyclerView.Adapter<ShowcaseProductPreviewViewHolder>() {

    private var selectedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()
    private var deletedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()
    private var appendedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowcaseProductPreviewViewHolder {
        val binding = ItemProductCardHorizontalBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ShowcaseProductPreviewViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return selectedProductList.size
    }

    override fun onBindViewHolder(holder: ShowcaseProductPreviewViewHolder, position: Int) {
        holder.bind(selectedProductList[position])
    }

    fun updateSelectedDataSet(newSelectedProductList: ArrayList<ShowcaseProduct>?) {
        selectedProductList.clear()
        newSelectedProductList?.let {
            it.map { showcaseProduct ->
                showcaseProduct.isCloseable = true
                showcaseProduct.ishighlighted = false
                if (!selectedProductList.contains(showcaseProduct)) {
                    selectedProductList.add(showcaseProduct)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun updateAppendedDataSet(newAppendedProductList: ArrayList<ShowcaseProduct>?) {
        newAppendedProductList?.let { newAppendedList ->
            appendedProductList.clear()
            newAppendedList.forEach {
                if (it.isNewAppended) {
                    appendedProductList.add(it)
                }
            }
        }
    }

    fun updateDeletedDataSet(newDeletedProducList: ArrayList<ShowcaseProduct>?) {
        newDeletedProducList?.let { deletedList ->
            deletedProductList.clear()
            deletedProductList.addAll(deletedList)
        }
    }

    fun getSelectedProductList(): ArrayList<BaseShowcaseProduct> {
        return selectedProductList
    }

    fun deleteSelectedProduct(position: Int) {
        deletedProductList.add(selectedProductList[position])
        appendedProductList.remove(selectedProductList[position])
        selectedProductList.removeAt(position)
        notifyDataSetChanged()
        if (deletedProductList.size.isMoreThanZero()) {
            val productToBeDeleted = deletedProductList.getOrNull(Int.ZERO) ?: return
            val product = (productToBeDeleted as? ShowcaseProduct) ?: return
            listener.setupDeleteCounter(product)
            listener.showDeleteCounter()
        }
    }

    fun undoDeleteSelectedProduct() {
        deletedProductList.forEach {
            selectedProductList.add(0, it)
            if ((it as ShowcaseProduct).isNewAppended) {
                appendedProductList.add(it)
            }
            notifyDataSetChanged()
        }
        deletedProductList.clear()
        if (deletedProductList.size.isZero()) {
            listener.hideDeleteCounter()
        }
    }

    fun getDeletedProductList(): ArrayList<BaseShowcaseProduct> {
        return deletedProductList
    }

    fun getAppendedProductList(): ArrayList<BaseShowcaseProduct> {
        return appendedProductList
    }
}
