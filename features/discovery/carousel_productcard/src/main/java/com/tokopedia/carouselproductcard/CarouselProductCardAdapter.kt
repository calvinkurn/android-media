package com.tokopedia.carouselproductcard

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid

internal class CarouselProductCardAdapter(
        private val productCardModelList: List<ProductCardModel>,
        private val isScrollable: Boolean,
        private val carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
        private val productCardHeight: Int = 0
): RecyclerView.Adapter<CarouselProductCardViewHolder>() {

    private var recyclerView: RecyclerView? = null


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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
}