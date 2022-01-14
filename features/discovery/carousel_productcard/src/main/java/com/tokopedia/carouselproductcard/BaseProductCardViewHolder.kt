package com.tokopedia.carouselproductcard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardModel

internal abstract class BaseProductCardViewHolder<T>(
    itemView: View,
    protected val internalListener: CarouselProductCardInternalListener?,
): RecyclerView.ViewHolder(itemView) {
    abstract fun bind(model: T)
    abstract fun recycle()

    protected fun registerProductCardLifecycleObserver(
        productCardView: IProductCardView?,
        productCardModel: ProductCardModel,
    ) {
        productCardView ?: return

        internalListener
            ?.productCardLifecycleObserver
            ?.register(productCardView, productCardModel)
    }

    protected fun unregisterProductCardLifecycleObserver(
        productCardView: IProductCardView?,
    ) {
        productCardView ?: return

        internalListener
            ?.productCardLifecycleObserver
            ?.unregister(productCardView)
    }
}