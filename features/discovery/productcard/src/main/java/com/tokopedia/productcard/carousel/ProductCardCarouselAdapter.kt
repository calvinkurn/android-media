package com.tokopedia.productcard.carousel

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel

internal class ProductCardCarouselAdapter(
        private val productCardModelList: List<ProductCardModel>,
        private val isScrollable: Boolean,
        private val productCardCarouselListenerInfo: ProductCardCarouselListenerInfo,
        private val blankSpaceConfig: BlankSpaceConfig
): RecyclerView.Adapter<ProductCardCarouselViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProductCardCarouselViewHolder {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(ProductCardCarouselViewHolder.LAYOUT, viewGroup, false)

        if (!isScrollable) {
            val layoutParams = view.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            view.layoutParams = layoutParams
        }

        return ProductCardCarouselViewHolder(view, productCardCarouselListenerInfo, blankSpaceConfig)
    }

    override fun getItemCount(): Int {
        return productCardModelList.size
    }

    override fun onBindViewHolder(productCardCarouselViewHolder: ProductCardCarouselViewHolder, position: Int) {
        productCardCarouselViewHolder.bind(this.productCardModelList[position])
    }
}