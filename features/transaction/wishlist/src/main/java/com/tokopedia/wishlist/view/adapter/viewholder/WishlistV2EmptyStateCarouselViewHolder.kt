package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.WishlistV2EmptyStateData
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateCarouselBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2EmptyStateCarouselViewHolder(private val binding: WishlistV2EmptyStateCarouselBinding, private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    private val items = ArrayList<Any>().apply {
        add(WishlistV2EmptyStateData(R.string.empty_state_img_1, R.string.empty_state_desc_1))
        add(WishlistV2EmptyStateData(R.string.empty_state_img_2, R.string.empty_state_desc_2))
        add(WishlistV2EmptyStateData(R.string.empty_state_img_3, R.string.empty_state_desc_3))
    }

    private val itemParam = { view: View, data: Any ->
        val img = view.findViewById<ImageUnify>(R.id.empty_state_img)
        val text = view.findViewById<Typography>(R.id.empty_state_desc)
        img.setImageUrl(url = view.context.getString((data as WishlistV2EmptyStateData).img))
        text.text = view.context.getString(data.desc)
    }

    fun bind() {
        binding.carouselEmptyState.apply {
            indicatorPosition = CarouselUnify.INDICATOR_BC
            freeMode = false
            centerMode = true
            slideToScroll = 1
            infinite = true
            addItems(R.layout.wishlist_v2_empty_state_custom_item, items, itemParam)
        }

        binding.buttonEmptyState.setOnClickListener {
            actionListener?.onCariBarangClicked()
        }
    }
}
