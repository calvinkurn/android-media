package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.wishlist.data.model.CollectionWishlistTypeLayoutData
import com.tokopedia.wishlist.data.model.response.CollectionWishlistResponse
import com.tokopedia.wishlist.databinding.CollectionWishlistTickerItemBinding
import com.tokopedia.wishlist.view.adapter.CollectionWishlistAdapter

class CollectionWishlistTickerItemViewHolder(
    private val binding: CollectionWishlistTickerItemBinding,
    private val actionListener: CollectionWishlistAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CollectionWishlistTypeLayoutData, isTickerClosed: Boolean) {
            if (item.dataObject is CollectionWishlistResponse.Data.GetWishlistCollections.WishlistCollectionResponseData.Ticker) {
                if (item.dataObject.title.isNotEmpty() && !isTickerClosed) {
                    binding.root.visible()
                    val params = (binding.root.layoutParams as GridLayoutManager.LayoutParams).apply {
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        width = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                    binding.root.layoutParams = params
                    binding.wishlistCollectionTickerTitle.text = item.dataObject.title
                    binding.wishlistCollectionTickerDesc.text = item.dataObject.description
                    binding.icCloseTickerCollectionWishlist.bringToFront()
                    binding.icCloseTickerCollectionWishlist.setOnClickListener { actionListener?.onCloseTicker() }
                } else {
                    binding.root.gone()
                    val params = (binding.root.layoutParams as GridLayoutManager.LayoutParams).apply {
                        height = 0
                        width = 0
                    }
                    binding.root.layoutParams = params
                }
            }
        }
}