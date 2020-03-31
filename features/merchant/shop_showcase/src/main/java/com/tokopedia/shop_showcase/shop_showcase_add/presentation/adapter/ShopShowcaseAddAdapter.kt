package com.tokopedia.shop_showcase.shop_showcase_add.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.listener.ShopShowcasePreviewListener
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder.ShowcaseProductItemViewHolder

class ShopShowcaseAddAdapter(private val context: Context, private var listener: ShopShowcasePreviewListener): RecyclerView.Adapter<ShowcaseProductItemViewHolder>() {

    private var selectedProductList: ArrayList<BaseShowcaseProduct> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowcaseProductItemViewHolder {
        return ShowcaseProductItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_product_showcase_grid, parent, false), listener)
    }

    override fun getItemCount(): Int {
        return selectedProductList.size
    }

    override fun onBindViewHolder(holder: ShowcaseProductItemViewHolder, position: Int) {
        holder.bind(selectedProductList[position])
    }

    fun updateSelectedDataSet(newSelectedProductList: ArrayList<ShowcaseProduct>?) {
        newSelectedProductList?.let {
            it.map { showcaseProduct ->
                showcaseProduct.isCloseable = true
                showcaseProduct.ishighlighted = false
            }
            selectedProductList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun getSelectedProductList(): ArrayList<BaseShowcaseProduct> {
        return selectedProductList
    }

    fun deleteSelectedProduct(position: Int) {
        selectedProductList.removeAt(position)
        notifyItemRemoved(position)
    }
}