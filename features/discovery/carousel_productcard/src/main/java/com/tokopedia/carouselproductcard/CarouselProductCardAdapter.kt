package com.tokopedia.carouselproductcard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.carouselproductcard.model.CarouselProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid

internal class CarouselProductCardAdapter(
        private val isScrollable: Boolean,
        private val productCardHeight: Int = 0
): ListAdapter<CarouselProductCardModel, CarouselProductCardViewHolder>(ProductModelDiffUtil()) {

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

        return CarouselProductCardViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return CarouselProductCardViewHolder.LAYOUT
    }

    override fun onBindViewHolder(carouselProductCardViewHolder: CarouselProductCardViewHolder, position: Int) {
        carouselProductCardViewHolder.bind(getItem(position))
    }

    fun updateWishlist(index: Int, isWishlist: Boolean){
        getItem(index).productCardModel.isWishlisted = isWishlist
        notifyItemChanged(index)
    }

    class ProductModelDiffUtil : DiffUtil.ItemCallback<CarouselProductCardModel>() {
        override fun areItemsTheSame(oldItem: CarouselProductCardModel, newItem: CarouselProductCardModel): Boolean {
            return oldItem.productCardModel.productName == newItem.productCardModel.productName
        }

        override fun areContentsTheSame(oldItem: CarouselProductCardModel, newItem: CarouselProductCardModel): Boolean {
            return oldItem.productCardModel == newItem.productCardModel
        }
    }
}