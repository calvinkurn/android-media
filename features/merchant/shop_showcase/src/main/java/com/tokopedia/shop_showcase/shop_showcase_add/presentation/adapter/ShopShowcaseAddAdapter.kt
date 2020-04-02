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
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder.ShowcaseProductItemViewHolder

class ShopShowcaseAddAdapter(private val context: Context, private var listener: ShopShowcasePreviewListener): RecyclerView.Adapter<ShowcaseProductPreviewViewHolder>() {

    private var selectedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()
    private var deletedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()
    private var appendedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowcaseProductPreviewViewHolder {
        return ShowcaseProductPreviewViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_product_showcase_grid, parent, false), listener)
    }

    override fun getItemCount(): Int {
        return selectedProductList.size
    }

    override fun onBindViewHolder(holder: ShowcaseProductPreviewViewHolder, position: Int) {
        holder.bind(selectedProductList[position])
    }

    fun updateSelectedDataSet(newSelectedProductList: ArrayList<ShowcaseProduct>?, isActionEdit: Boolean?) {
        newSelectedProductList?.let {
            it.map { showcaseProduct ->
                showcaseProduct.isCloseable = true
                showcaseProduct.ishighlighted = false
            }
            selectedProductList.clear()
            selectedProductList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun updateAppendedDataSet(newAppendedProductList: ArrayList<ShowcaseProduct>?) {
        newAppendedProductList?.let {
            appendedProductList.addAll(it)
        }
    }

    fun getSelectedProductList(): ArrayList<BaseShowcaseProduct> {
        return selectedProductList
    }

    fun deleteSelectedProduct(position: Int) {
        deletedProductList.add(selectedProductList[position])
        appendedProductList.remove(selectedProductList[position])
        selectedProductList.removeAt(position)
        notifyItemRemoved(position)
        if(deletedProductList.size > 0) {
            listener.showDeleteCounter(deletedProductList[0] as ShowcaseProduct)
        }
    }

    fun undoDeleteSelectedProduct() {
        deletedProductList.forEach {
            selectedProductList.add(0, it)
            appendedProductList.add(it)
            notifyItemInserted(0)
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