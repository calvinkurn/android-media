package com.tokopedia.wishlist.view.adapter.viewholder

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.wishlist.data.model.WishlistCollectionEmptyStateData
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.ACTION_ADD_ITEM_TO_COLLECTION
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

                    if (item.dataObject.listButton[0].action == ACTION_ADD_ITEM_TO_COLLECTION) {
                        setPrimaryCTAClickListener { actionListener?.goToWishlistAll() }
                    }

                    if (item.dataObject.listButton.size > 1) {
                        setSecondaryCTAText(item.dataObject.listButton[1].text)

                        if (item.dataObject.listButton[1].action == ACTION_UPDATE_COLLECTION_NAME) {
                            setSecondaryCTAClickListener { actionListener?.onChangeCollectionName() }
                        }
                    }
                }
            }
        }
    }
}