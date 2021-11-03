package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailNonProductBundleCardViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailProductBundleCardViewHolder
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class ProductAdapterFactoryImpl(
        private val actionListener: SomDetailAdapter.ActionListener?
) : BaseAdapterTypeFactory(), ProductAdapterFactory {

    override fun type(model: NonProductBundleUiModel): Int {
        return SomDetailNonProductBundleCardViewHolder.RES_LAYOUT
    }

    override fun type(model: ProductBundleUiModel): Int {
        return SomDetailProductBundleCardViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SomDetailNonProductBundleCardViewHolder.RES_LAYOUT -> SomDetailNonProductBundleCardViewHolder(actionListener, parent)
            SomDetailProductBundleCardViewHolder.RES_LAYOUT -> SomDetailProductBundleCardViewHolder(actionListener, parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}