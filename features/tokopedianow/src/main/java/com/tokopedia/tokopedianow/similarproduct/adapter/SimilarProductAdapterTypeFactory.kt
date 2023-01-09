package com.tokopedia.tokopedianow.similarproduct.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import com.tokopedia.tokopedianow.similarproduct.viewholder.SimilarProductViewHolder

class SimilarProductAdapterTypeFactory(
    private val productListener: SimilarProductViewHolder.SimilarProductListener?
) : BaseAdapterTypeFactory(), SimilarProductTypeFactory {

    override fun type(uiModel: SimilarProductUiModel): Int = SimilarProductViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SimilarProductViewHolder.LAYOUT -> SimilarProductViewHolder(parent, productListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
