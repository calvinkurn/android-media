package com.tokopedia.cart.view.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.HolderItemCartNewBinding
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.viewholder.CartItemViewHolder
import com.tokopedia.cart.view.viewholder.CartItemViewHolder.Companion.TYPE_VIEW_ITEM_CART
import com.tokopedia.cart.view.viewholder.CartItemViewHolder.ViewHolderListener
import rx.subscriptions.CompositeSubscription

class CartItemAdapter(private val actionListener: ActionListener,
                      private val compositeSubscription: CompositeSubscription,
                      private val parentPosition: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ViewHolderListener {

    private val cartItemHolderDataList: MutableList<CartItemHolderData> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return TYPE_VIEW_ITEM_CART
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = HolderItemCartNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding, compositeSubscription, actionListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderView = holder as CartItemViewHolder
        val data = cartItemHolderDataList[position]
        holderView.bindData(data, parentPosition, this, cartItemHolderDataList.size)
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

    override fun onNeedToRefreshSingleShop(parentPosition: Int) {
        actionListener.onNeedToRefreshSingleShop(parentPosition)
        actionListener.onNeedToRecalculate()
    }

    override fun onNeedToRefreshAllShop() {
        actionListener.onNeedToRefreshMultipleShop()
        actionListener.onNeedToRecalculate()
    }

    interface ActionListener {
        fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData?)
        fun onCartItemQuantityPlusButtonClicked()
        fun onCartItemQuantityMinusButtonClicked()
        fun onCartItemQuantityReseted(position: Int, parentPosition: Int)
        fun onCartItemProductClicked(cartItemData: CartItemData?)
        fun onCartItemQuantityInputFormClicked(qty: String?)
        fun onCartItemLabelInputRemarkClicked()
        fun onCartItemCheckChanged(position: Int, parentPosition: Int, checked: Boolean)
        fun onWishlistCheckChanged(productId: String?, cartId: Long?, imageView: ImageView?)
        fun onNeedToRefreshSingleShop(parentPosition: Int)
        fun onNeedToRefreshMultipleShop()
        fun onNeedToRecalculate()
        fun onCartItemQuantityChangedThenHitUpdateCartAndValidateUse()
        fun onEditNoteDone(position: Int)
        fun onCartItemShowRemainingQty(productId: String?)
        fun onCartItemShowInformationLabel(productId: String?, informationLabel: String?)
    }
}