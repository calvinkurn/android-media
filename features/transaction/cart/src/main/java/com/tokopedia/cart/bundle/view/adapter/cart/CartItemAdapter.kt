package com.tokopedia.cart.bundle.view.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import com.tokopedia.cart.bundle.view.viewholder.CartItemViewHolder
import com.tokopedia.cart.bundle.view.viewholder.CartItemViewHolder.Companion.TYPE_VIEW_ITEM_CART
import com.tokopedia.cart.bundle.view.viewholder.CartItemViewHolder.ViewHolderListener
import com.tokopedia.cart.databinding.ItemCartProductBundleBinding

class CartItemAdapter(private val actionListener: ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ViewHolderListener {

    private val cartItemHolderDataList: MutableList<CartItemHolderData> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return TYPE_VIEW_ITEM_CART
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCartProductBundleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding, actionListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderView = holder as CartItemViewHolder
        val data = cartItemHolderDataList[position]
        holderView.bindData(data, this, cartItemHolderDataList.size)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as CartItemViewHolder).clear()
    }

    override fun getItemCount(): Int {
        return cartItemHolderDataList.size
    }

    fun addDataList(cartItemHolderDataList: MutableList<CartItemHolderData>?) {
        this.cartItemHolderDataList.clear()
        this.cartItemHolderDataList.addAll(cartItemHolderDataList ?: emptyList())
        notifyDataSetChanged()
    }

    override fun onNeedToRefreshSingleProduct(childPosition: Int) {
        notifyItemChanged(childPosition)
        actionListener.onNeedToRecalculate()
    }

    override fun onNeedToRefreshSingleShop(cartItemHolderData: CartItemHolderData) {
        actionListener.onNeedToRecalculate()
        actionListener.onNeedToRefreshSingleShop(cartItemHolderData)
    }

    override fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData) {
        actionListener.onNeedToRecalculate()
        actionListener.onNeedToRefreshWeight(cartItemHolderData)
    }

    override fun onNeedToRefreshAllShop() {
        actionListener.onNeedToRefreshMultipleShop()
        actionListener.onNeedToRecalculate()
    }

    interface ActionListener {
        fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData)
        fun onCartItemQuantityPlusButtonClicked()
        fun onCartItemQuantityMinusButtonClicked()
        fun onCartItemQuantityReseted(position: Int, cartItemHolderData: CartItemHolderData)
        fun onCartItemProductClicked(cartItemHolderData: CartItemHolderData)
        fun onCartItemQuantityInputFormClicked(qty: String)
        fun onCartItemLabelInputRemarkClicked()
        fun onCartItemCheckChanged(position: Int, cartItemHolderData: CartItemHolderData)
        fun onBundleItemCheckChanged(cartItemHolderData: CartItemHolderData)
        fun onWishlistCheckChanged(productId: String, cartId: String, imageView: ImageView)
        fun onNeedToRefreshSingleShop(cartItemHolderData: CartItemHolderData)
        fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData)
        fun onNeedToRefreshMultipleShop()
        fun onNeedToRecalculate()
        fun onCartItemQuantityChanged(cartItemHolderData: CartItemHolderData, newQuantity: Int)
        fun onCartItemShowRemainingQty(productId: String)
        fun onCartItemShowInformationLabel(productId: String, informationLabel: String)
        fun onEditBundleClicked(cartItemHolderData: CartItemHolderData)
        fun onTobaccoLiteUrlClicked(url: String, data: CartItemHolderData, action: Action)
        fun onShowTickerTobacco()
        fun onSimilarProductUrlClicked(data: CartItemHolderData)
        fun onShowActionSeeOtherProduct(productId: String, errorType: String)
        fun onFollowShopClicked(shopId: String, errorType: String)
        fun onVerificationClicked(applink: String)
    }
}