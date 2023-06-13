package com.tokopedia.wishlistcollection.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.WishlistCollectionEmptyStateCarouselBinding
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionCarouselEmptyStateData
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

class WishlistCollectionEmptyStateCarouselViewHolder(private val binding: WishlistCollectionEmptyStateCarouselBinding, private val actionListener: WishlistCollectionAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(items: ArrayList<Any>) {
        val itemParam = { view: View, data: Any ->
            val img = view.findViewById<ImageUnify>(R.id.empty_state_img)
            val text = view.findViewById<Typography>(R.id.empty_state_desc)
            img.setImageUrl(url = (data as WishlistCollectionCarouselEmptyStateData).img)
            text.text = data.desc
        }

        binding.carouselEmptyState.apply {
            indicatorPosition = CarouselUnify.INDICATOR_BC
            freeMode = false
            centerMode = true
            slideToScroll = 1
            infinite = true
            addItems(R.layout.wishlist_v2_empty_state_custom_item, items, itemParam)
        }

        binding.buttonSearch.setOnClickListener {
            actionListener?.onCariBarangClicked()
            WishlistCollectionAnalytics.sendClickCariBarangButtonOnEmptyStateNoWishlistItemsEvent()
        }

        binding.buttonCreateCollection.setOnClickListener {
            actionListener?.onCreateNewCollectionClicked()
            WishlistCollectionAnalytics.sendClickBuatKoleksiButtonOnEmptyStateNoWishlistItemsEvent()
        }
    }
}
