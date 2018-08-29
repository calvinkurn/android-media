package com.tokopedia.talk

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * @author by nisie on 6/12/18.
 */
class ProductTalkTypeFactoryImpl() :
        BaseAdapterTypeFactory(),
        ProductTalkThreadTypeFactory {
    override fun type(viewModel: ProductTalkViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: ProductTalkThreadViewModel): Int {
        return ProductTalkThreadViewHolder.LAYOUT
    }


    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            ProductTalkThreadViewHolder.LAYOUT -> ProductTalkThreadViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }
    }

}