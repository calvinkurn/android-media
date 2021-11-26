package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop_showcase.databinding.ItemProductCardHorizontalBinding
import com.tokopedia.shop_showcase.databinding.ItemShowcaseProductLoadingBinding
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.listener.ShopShowcaseProductAddListener
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.LoadingShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder.ShowcaseProductItemViewHolder

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShowcaseProductListAdapter(
        listener: ShopShowcaseProductAddListener
) : RecyclerView.Adapter<ShowcaseProductItemViewHolder>() {

    companion object {
        private const val VIEW_SHOW_CASE = 0
        private const val VIEW_LOADING = 1
    }

    private var shopProductList: MutableList<BaseShowcaseProduct> = mutableListOf()
    private var selectedProduct: ArrayList<ShowcaseProduct> = arrayListOf()
    private var excludedProduct: ArrayList<ShowcaseProduct> = arrayListOf()
    private var deletedProduct: ArrayList<ShowcaseProduct> = arrayListOf()
    private val loadingModel = LoadingShowcaseProduct()
    private val viewListener: ShopShowcaseProductAddListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowcaseProductItemViewHolder {
        val binding = if (viewType == VIEW_SHOW_CASE) {
            ItemProductCardHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            ItemShowcaseProductLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return ShowcaseProductItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return shopProductList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopProductList[position] is ShowcaseProduct)
            VIEW_SHOW_CASE
        else
            VIEW_LOADING
    }

    override fun onBindViewHolder(holder: ShowcaseProductItemViewHolder, position: Int) {
        val item = shopProductList[position]
        holder.bind(item)
        if (item is ShowcaseProduct) {
            holder.productContainer?.setOnClickListener {
                chooseProduct(holder, item)
            }

            holder.productCheckBox?.setOnClickListener {
                chooseProduct(holder, item)
            }
        }
    }

    private fun chooseProduct(holder: ShowcaseProductItemViewHolder, item: ShowcaseProduct) {
        val cardState = !item.ishighlighted
        if (cardState) {
            item.isNewAppended = excludedProduct.none { excludedProduct ->
                excludedProduct.productId == item.productId
            }
            if(deletedProduct.size > 0)
                deletedProduct.remove(item)
            selectedProduct.add(item)
        } else {
            item.isNewAppended = false
            val targetedProduct = selectedProduct.singleOrNull {
                it.productId == item.productId
            }
            if(targetedProduct != null) {
                selectedProduct.remove(targetedProduct)
            }
        }
        item.ishighlighted = cardState
        holder.renderCardState(item)
        viewListener.showProductCounter(getSelectedProductSize(), excludedProduct, selectedProduct)
        viewListener.onCLickProductCardTracking()
    }

    fun updateShopProductList(isLoadNextPage: Boolean,
                              productList: List<ShowcaseProduct>,
                              excludedProductList: ArrayList<ShowcaseProduct>,
                              previouslySelected: List<ShowcaseProduct>) {
        excludedProduct = excludedProductList

        if (!isLoadNextPage) {
            shopProductList = productList.toMutableList()
        } else {
            shopProductList.addAll(productList.toMutableList())
        }
        if(selectedProduct.isEmpty()) {
            selectedProduct = ArrayList(previouslySelected)
            viewListener.showProductCounter(getSelectedProductSize(), excludedProduct, selectedProduct)
        }
        notifyDataSetChanged()
    }

    fun showLoadingProgress() {
        shopProductList.add(loadingModel)
        notifyItemChanged(shopProductList.size)
    }

    fun hideLoadingProgress() {
        val position = shopProductList.indexOf(loadingModel)
        if (position > 0) {
            shopProductList.remove(loadingModel)
            notifyItemRemoved(position)
        }
    }

    fun getSelectedProduct(): ArrayList<ShowcaseProduct> {
        return selectedProduct
    }

    fun getDeletedProduct(): ArrayList<ShowcaseProduct> {
        return deletedProduct
    }

    private fun getSelectedProductSize(): Int {
        val appendedProduct = selectedProduct.filter {
            it.isNewAppended
        }
        return appendedProduct.size
    }

}