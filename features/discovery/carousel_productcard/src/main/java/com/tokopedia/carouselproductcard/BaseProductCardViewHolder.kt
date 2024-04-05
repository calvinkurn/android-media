package com.tokopedia.carouselproductcard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.VisibleVH
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardModel

internal abstract class BaseProductCardViewHolder<T>(
    itemView: View,
    protected val internalListener: CarouselProductCardInternalListener?,
): RecyclerView.ViewHolder(itemView), VisibleVH {
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

    override fun getVisiblePercentage(): Int {
        return (visibilityExtent * 100).toInt()
    }

    override fun setVisibilityExtent(visibilityExtent: Float) {
        this.visibilityExtent = visibilityExtent
    }

    private var visibilityExtent = 0f
}
