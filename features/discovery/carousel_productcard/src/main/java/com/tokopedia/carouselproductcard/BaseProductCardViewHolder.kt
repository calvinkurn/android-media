package com.tokopedia.carouselproductcard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.listener.IAdsViewHolderTrackListener
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardModel

internal abstract class BaseProductCardViewHolder<T>(
    itemView: View,
    protected val internalListener: CarouselProductCardInternalListener?,
): RecyclerView.ViewHolder(itemView), IAdsViewHolderTrackListener {
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

    override fun setVisiblePercentage(visiblePercentage: Int) {
        this.visibleViewPercentage = visiblePercentage
    }

    override val visiblePercentage: Int
        get() = visibleViewPercentage

    private var visibleViewPercentage: Int = 0

}
