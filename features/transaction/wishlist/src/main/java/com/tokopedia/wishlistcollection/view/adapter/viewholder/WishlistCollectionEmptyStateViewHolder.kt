package com.tokopedia.wishlistcollection.view.adapter.viewholder

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.wishlist.data.model.WishlistCollectionEmptyStateData
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.ACTION_ADD_ITEM_TO_COLLECTION
import com.tokopedia.wishlist.util.WishlistV2Consts.ACTION_BACK_TO_HOME
import com.tokopedia.wishlist.util.WishlistV2Consts.ACTION_OPEN_MY_WISHLIST
import com.tokopedia.wishlist.util.WishlistV2Consts.ACTION_RESET_FILTER
import com.tokopedia.wishlist.util.WishlistV2Consts.ACTION_SEARCH_ITEM
import com.tokopedia.wishlist.util.WishlistV2Consts.ACTION_SHOW_SEARCH_BAR
import com.tokopedia.wishlist.util.WishlistV2Consts.ACTION_UPDATE_COLLECTION
import com.tokopedia.wishlist.util.WishlistV2Consts.ACTION_UPDATE_COLLECTION_NAME
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistCollectionEmptyStateViewHolder(private val binding: WishlistV2EmptyStateItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistV2TypeLayoutData) {
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
