package com.tokopedia.cart.view.adapter.cart

import android.widget.ImageView
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.view.uimodel.CartItemHolderData

class CartItemAdapter/*(private val actionListener: ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ViewHolderListener*/ {

    /*private val cartItemHolderDataList: MutableList<CartItemHolderData> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return TYPE_VIEW_ITEM_CART
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    override fun onNeedToRefreshBoAffordability(cartItemHolderData: CartItemHolderData) {
        actionListener.onNeedToRefreshWeight(cartItemHolderData)
    }

    override fun onNeedToRefreshAllShop() {
        actionListener.onNeedToRefreshMultipleShop()
        actionListener.onNeedToRecalculate()
    }*/

    interface ActionListener {
        fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData)
        fun onCartItemQuantityPlusButtonClicked()
        fun onCartItemQuantityMinusButtonClicked()

//        fun onCartItemQuantityReseted(position: Int, cartItemHolderData: CartItemHolderData)
        fun onCartItemProductClicked(cartItemHolderData: CartItemHolderData)
        fun onCartItemQuantityInputFormClicked(qty: String)
        fun onCartItemLabelInputRemarkClicked()
        fun onCartItemCheckChanged(position: Int, cartItemHolderData: CartItemHolderData)
        fun onBundleItemCheckChanged(cartItemHolderData: CartItemHolderData)
        fun onWishlistCheckChanged(productId: String, cartId: String, imageView: ImageView, isError: Boolean, errorType: String)
        fun onNeedToRefreshSingleShop(cartItemHolderData: CartItemHolderData, itemPosition: Int)
        fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData)
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
        fun onCartShopNameClicked(shopId: String?, shopName: String?, isTokoNow: Boolean)
    }
}
