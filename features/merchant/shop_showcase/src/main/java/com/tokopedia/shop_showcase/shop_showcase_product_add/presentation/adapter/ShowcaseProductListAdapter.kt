package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.fragment.ShopShowcaseProductAddListener
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.LoadingShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder.ShowcaseProductItemViewHolder
import kotlinx.android.synthetic.main.fragment_shop_showcase_product_add.view.*
import kotlinx.android.synthetic.main.item_add_product_showcase_grid.view.*

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
    private val fragmentView: View = parentFragmentView
    private val loadingModel = LoadingShowcaseProduct()
    private val viewListener: ShopShowcaseProductAddListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowcaseProductItemViewHolder {
        val view = if (viewType == VIEW_SHOW_CASE) {
            LayoutInflater.from(context).inflate(R.layout.item_add_product_showcase_grid, parent, false)
        }
        else {
            LayoutInflater.from(context).inflate(R.layout.item_showcase_product_loading, parent, false)
        }
        return ShowcaseProductItemViewHolder(view, context)
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
            holder.itemView.parent_card_view.setOnClickListener {
                val cardState = !item.ishighlighted
                val appendState = !item.isNewAppended

                if (cardState) {
                    if(!item.isNoNeedToAppend)
                        selectedProduct.add(shopProductList[position] as ShowcaseProduct)
                } else {

                    val deletedProduct = shopProductList[position] as ShowcaseProduct
                    val targetedProduct = selectedProduct.singleOrNull {
                        it.productId == deletedProduct.productId
                    }
                    if(targetedProduct != null) {
                        selectedProduct.run {
                            if(contains(targetedProduct))
                                remove(targetedProduct)
                        }
                    } else {
                        item.isNoNeedToAppend = true
                    }

                }
                item.ishighlighted = cardState
                item.isNewAppended = appendState
                holder.renderCardState(item)
                showProductCounter(getSelectedProductSize())
                viewListener.onCLickProductCardTracking()
            }
        }
    }

    fun updateShopProductList(isLoadNextPage: Boolean, productList: List<ShowcaseProduct>, previouslySelectedProduct: List<ShowcaseProduct>, appendedProductList: List<ShowcaseProduct>, isActionEdit: Boolean) {
        if (!isLoadNextPage) {
            shopProductList = productList.toMutableList()
        } else {
            shopProductList.addAll(productList.toMutableList())
        }
        if(selectedProduct.isEmpty()) {
            selectedProduct = if(!isActionEdit) {
                ArrayList(previouslySelectedProduct)
            } else {
                ArrayList(appendedProductList)
            }
            showProductCounter(getSelectedProductSize())
        }
        notifyDataSetChanged()
    }

    fun isShowCaseProductItem(position: Int): Boolean {
        return shopProductList[position] is ShowcaseProduct
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

    // TODO: Move this to fragment level soon!!
    private fun showProductCounter(totalSelectedProduct: Int) {
        if(totalSelectedProduct > 0) {
            val item = selectedProduct[0]
            ImageHandler.LoadImage(
                    fragmentView.product_choosen_image,
                    item.productImageUrl
            )
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
        return selectedProduct.size
    }

}