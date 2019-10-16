package com.tokopedia.carouselproductcard

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel

internal class CarouselProductCardAdapter(
        private val productCardModelList: List<ProductCardModel>,
        private val isScrollable: Boolean,
        private val carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
        private val blankSpaceConfig: BlankSpaceConfig
): RecyclerView.Adapter<CarouselProductCardViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CarouselProductCardViewHolder {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(CarouselProductCardViewHolder.LAYOUT, viewGroup, false)

        if (!isScrollable) {
            val layoutParams = view.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            view.layoutParams = layoutParams
        }

        return CarouselProductCardViewHolder(view, carouselProductCardListenerInfo, blankSpaceConfig)
    }

    override fun getItemCount(): Int {
        return productCardModelList.size
    }

    override fun onBindViewHolder(carouselProductCardViewHolder: CarouselProductCardViewHolder, position: Int) {
        carouselProductCardViewHolder.bind(this.productCardModelList[position])
    }
}