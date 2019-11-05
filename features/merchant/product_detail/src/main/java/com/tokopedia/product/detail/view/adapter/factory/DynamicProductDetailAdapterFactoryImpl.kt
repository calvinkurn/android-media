package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.dynamicpdp.DynamicProductDetailDataModel

class DynamicProductDetailAdapterFactoryImpl: BaseAdapterTypeFactory(), DynamicProductDetailAdapterFactory  {

    override fun type(data: DynamicProductDetailDataModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return super.createViewHolder(view, type)
    }

}