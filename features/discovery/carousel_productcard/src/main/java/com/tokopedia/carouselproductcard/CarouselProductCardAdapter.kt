package com.tokopedia.carouselproductcard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid

internal class CarouselProductCardAdapter(
        private val productCardModelList: List<ProductCardModel>,
        private val isScrollable: Boolean,
        private val carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
        private val productCardHeight: Int = 0
): RecyclerView.Adapter<CarouselProductCardViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CarouselProductCardViewHolder {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(CarouselProductCardViewHolder.LAYOUT, viewGroup, false)
        if (!isScrollable) {
            val layoutParams = view.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            view.layoutParams = layoutParams
            val productCardView = view.findViewById<ProductCardViewSmallGrid>(R.id.carouselProductCardItem)
            productCardView.setCardHeight(productCardHeight)
        } else {
            val productCardView = view.findViewById<ProductCardViewSmallGrid>(R.id.carouselProductCardItem)
            productCardView.setCardHeight(productCardHeight)
        }

        return CarouselProductCardViewHolder(view, carouselProductCardListenerInfo)
    }

    override fun getItemCount(): Int {
        return productCardModelList.size
    }

    override fun onBindViewHolder(carouselProductCardViewHolder: CarouselProductCardViewHolder, position: Int) {
        carouselProductCardViewHolder.bind(this.productCardModelList[position])
    }

    fun updateWishlist(position: Int, wishlist: Boolean) {
        productCardModelList[position].isWishlisted = wishlist
        notifyItemChanged(position)
    }
}