package com.tokopedia.wishlist.collection.view.adapter.viewholder

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.wishlist.collection.data.model.WishlistCollectionEmptyStateData
import com.tokopedia.wishlist.databinding.WishlistEmptyStateItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.util.WishlistConsts.ACTION_ADD_ITEM_TO_COLLECTION
import com.tokopedia.wishlist.detail.util.WishlistConsts.ACTION_BACK_TO_HOME
import com.tokopedia.wishlist.detail.util.WishlistConsts.ACTION_OPEN_MY_WISHLIST
import com.tokopedia.wishlist.detail.util.WishlistConsts.ACTION_RESET_FILTER
import com.tokopedia.wishlist.detail.util.WishlistConsts.ACTION_SEARCH_ITEM
import com.tokopedia.wishlist.detail.util.WishlistConsts.ACTION_SHOW_SEARCH_BAR
import com.tokopedia.wishlist.detail.util.WishlistConsts.ACTION_UPDATE_COLLECTION
import com.tokopedia.wishlist.detail.util.WishlistConsts.ACTION_UPDATE_COLLECTION_NAME
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter

class WishlistCollectionEmptyStateViewHolder(private val binding: WishlistEmptyStateItemBinding, private val actionListener: WishlistAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistTypeLayoutData) {
        if (item.dataObject is WishlistCollectionEmptyStateData) {
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 70.toDp(), 0, 0)
            binding.wishlistV2EmptyState.emptyStateCTAID.layoutParams = layoutParams
            binding.wishlistV2EmptyState.apply {
                setImageUrl(item.dataObject.img)
                setTitle(item.dataObject.title)
                setDescription(item.dataObject.desc)
                if (item.dataObject.listButton.isNotEmpty()) {
                    setPrimaryCTAText(item.dataObject.listButton[0].text)

                    when (item.dataObject.listButton[0].action) {
                        ACTION_ADD_ITEM_TO_COLLECTION -> {
                            setPrimaryCTAClickListener { actionListener?.goToWishlistAllToAddCollection() }
                        }
                        ACTION_SEARCH_ITEM -> {
                            setPrimaryCTAClickListener { actionListener?.onNotFoundButtonClicked(item.dataObject.query) }
                        }
                        ACTION_RESET_FILTER -> {
                            setPrimaryCTAClickListener { actionListener?.onResetFilter() }
                        }
                        ACTION_SHOW_SEARCH_BAR -> {
                            setPrimaryCTAClickListener { actionListener?.onCariBarangClicked() }
                        }
                        ACTION_OPEN_MY_WISHLIST -> {
                            setPrimaryCTAClickListener { actionListener?.goToMyWishlist() }
                        }
                    }

                    if (item.dataObject.listButton.size > 1) {
                        setSecondaryCTAText(item.dataObject.listButton[1].text)

                        when (item.dataObject.listButton[1].action) {
                            ACTION_UPDATE_COLLECTION_NAME -> {
                                setSecondaryCTAClickListener { actionListener?.onChangeCollectionName() }
                            }
                            ACTION_BACK_TO_HOME -> {
                                setSecondaryCTAClickListener { actionListener?.goToHome() }
                            }
                            ACTION_UPDATE_COLLECTION -> {
                                setSecondaryCTAClickListener { actionListener?.goToEditWishlistCollectionPage() }
                            }
                        }
                    }
                }
            }
        }
    }
}
