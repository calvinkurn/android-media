package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.fragment.ShopShowcaseProductAddListener
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.LoadingShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder.ShowcaseProductItemViewHolder
import kotlinx.android.synthetic.main.fragment_shop_showcase_product_add.view.*
import kotlinx.android.synthetic.main.item_product_card_horizontal.view.*

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShowcaseProductListAdapter(
        private val context: Context,
        parentFragmentView: View,
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
    private val fragmentView: View = parentFragmentView
    private val loadingModel = LoadingShowcaseProduct()
    private val viewListener: ShopShowcaseProductAddListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowcaseProductItemViewHolder {
        val view = if (viewType == VIEW_SHOW_CASE) {
            LayoutInflater.from(context).inflate(R.layout.item_product_card_horizontal, parent, false)
        }
        else {
            LayoutInflater.from(context).inflate(R.layout.item_showcase_product_loading, parent, false)
        }
        return ShowcaseProductItemViewHolder(view)
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
            holder.itemView.parent_item_product_card.setOnClickListener {
                chooseProduct(holder, item)
            }

            holder.itemView.card_checkbox.setOnClickListener {
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
        showProductCounter(getSelectedProductSize())
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
            showProductCounter(getSelectedProductSize())
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

    // TODO: Move this to fragment level soon!!
    private fun showProductCounter(totalSelectedProduct: Int) {
        if(totalSelectedProduct > 0) {
            var idx = 0
            val item = if(excludedProduct.size > 0) {
                // if in edit mode, find first appended product to show product image on counter
                for(i in 0 until selectedProduct.size) {
                    if(selectedProduct[i].isNewAppended) {
                        idx = i
                        break
                    }
                }
                selectedProduct[idx]
            } else {
                // if in create mode, just use first selected product image
                selectedProduct[idx]
            }

            // try catch to avoid ImageUnify crash on set image to product counter image
            try {
                if(fragmentView.product_choosen_image?.context?.isValidGlideContext() == true) {
                    ImageHandler.LoadImage(
                            fragmentView.product_choosen_image,
                            item.productImageUrl
                    )
                }
            } catch (e : Throwable) {}

            fragmentView.product_choosen_counter.visibility = View.VISIBLE
            fragmentView.total_selected_product_counter.text = context.resources.getString(
                    R.string.chosen_product_counter_text,
                    getSelectedProductSize().toString()
            )
        } else {
            fragmentView.product_choosen_counter.visibility = View.GONE
        }
    }

    private fun getSelectedProductSize(): Int {
        val appendedProduct = selectedProduct.filter {
            it.isNewAppended
        }
        return appendedProduct.size
    }

}