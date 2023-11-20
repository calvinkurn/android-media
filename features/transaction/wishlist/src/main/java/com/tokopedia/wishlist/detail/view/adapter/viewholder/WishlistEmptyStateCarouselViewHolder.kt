package com.tokopedia.wishlist.detail.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.WishlistEmptyStateCarouselBinding
import com.tokopedia.wishlist.detail.data.model.WishlistEmptyStateData
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter

class WishlistEmptyStateCarouselViewHolder(private val binding: WishlistEmptyStateCarouselBinding, private val actionListener: WishlistAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    private val items = ArrayList<Any>().apply {
        add(WishlistEmptyStateData(R.string.empty_state_img_1, R.string.empty_state_desc_1))
        add(WishlistEmptyStateData(R.string.empty_state_img_2, R.string.empty_state_desc_2))
        add(WishlistEmptyStateData(R.string.empty_state_img_3, R.string.empty_state_desc_3))
    }

    private val itemParam = { view: View, data: Any ->
        val img = view.findViewById<ImageUnify>(R.id.empty_state_img)
        val text = view.findViewById<Typography>(R.id.empty_state_desc)
        img.setImageUrl(url = view.context.getString((data as WishlistEmptyStateData).img))
        text.text = view.context.getString(data.desc)
    }

    fun bind() {
        binding.carouselEmptyState.apply {
            indicatorPosition = CarouselUnify.INDICATOR_BC
            freeMode = false
            centerMode = true
            slideToScroll = 1
            infinite = true
            addItems(R.layout.wishlist_empty_state_custom_item, items, itemParam)
        }

        binding.buttonEmptyState.setOnClickListener {
            actionListener?.onCariBarangClicked()
        }
    }
}
