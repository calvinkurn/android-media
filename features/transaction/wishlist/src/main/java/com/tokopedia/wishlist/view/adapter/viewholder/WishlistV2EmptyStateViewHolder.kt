package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.WishlistV2EmptyStateData
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateBinding
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2LoaderListItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2EmptyStateViewHolder(private val binding: WishlistV2EmptyStateBinding, private val actionListener: WishlistV2Adapter.ActionListener) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData) {
        val itemParam = { view: View, data: Any ->
            val img = view.findViewById<ImageUnify>(R.id.empty_state_img)
            val text = view.findViewById<Typography>(R.id.empty_state_desc)

            img.setImageDrawable(MethodChecker.getDrawable(itemView.context, (data as WishlistV2EmptyStateData).img))
            text.text = data.desc
        }

        val items = ArrayList<Any>().apply {
            add(WishlistV2EmptyStateData(R.drawable.ic_wishlist_empty_state_1, "Mau simpan barang untuk beli nanti? Bandingkan harga dan spesifikasinya? Di sini tempatnya!"))
            add(WishlistV2EmptyStateData(R.drawable.ic_wishlist_empty_state_2, "Lagi lihat-lihat, ada barang yang kamu suka? Klik ikon hati buat simpan di Wishlist."))
            add(WishlistV2EmptyStateData(R.drawable.ic_wishlist_empty_state_3, "Akses Wishlist-mu kapan saja, masuk dari menu navigasi dan halaman Keranjang."))

        }

        binding.carouselEmptyState.apply {
            indicatorPosition = CarouselUnify.INDICATOR_BC
            freeMode = false
            centerMode = true
            slideToScroll = 1
            infinite = false
            addItems(R.layout.wishlist_v2_empty_state_item, items, itemParam)

        }

        binding.buttonEmptyState.setOnClickListener {
            // actionListener.onCariBarangClicked()
        }
    }
}