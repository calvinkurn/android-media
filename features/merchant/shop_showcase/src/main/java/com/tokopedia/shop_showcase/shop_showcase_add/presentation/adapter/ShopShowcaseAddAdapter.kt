package com.tokopedia.shop_showcase.shop_showcase_add.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.listener.ShopShowcasePreviewListener
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewholder.ShowcaseProductPreviewViewHolder
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct

class ShopShowcaseAddAdapter(private val context: Context, private var listener: ShopShowcasePreviewListener): RecyclerView.Adapter<ShowcaseProductPreviewViewHolder>() {

    private var selectedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()
    private var deletedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()
    private var appendedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowcaseProductPreviewViewHolder {
        return ShowcaseProductPreviewViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_card_horizontal, parent, false), listener)
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
                if(!selectedProductList.contains(showcaseProduct))
                    selectedProductList.add(showcaseProduct)
            }
        }
        notifyDataSetChanged()
    }

    fun updateAppendedDataSet(newAppendedProductList: ArrayList<ShowcaseProduct>?) {
        newAppendedProductList?.let { newAppendedList ->
            appendedProductList.clear()
            newAppendedList.forEach {
                if(it.isNewAppended)
                    appendedProductList.add(it)
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
        if(deletedProductList.size > 0) {
            listener.setupDeleteCounter(deletedProductList[0] as ShowcaseProduct)
            listener.showDeleteCounter()
        }
    }

    fun undoDeleteSelectedProduct() {
        deletedProductList.forEach {
            selectedProductList.add(0, it)
            if((it as ShowcaseProduct).isNewAppended)
                appendedProductList.add(it)
            notifyDataSetChanged()
        }
        deletedProductList.clear()
        if(deletedProductList.size == 0)
            listener.hideDeleteCounter()
    }

    fun getDeletedProductList(): ArrayList<BaseShowcaseProduct> {
        return deletedProductList
    }

    fun getAppendedProductList(): ArrayList<BaseShowcaseProduct> {
        return appendedProductList
    }
}