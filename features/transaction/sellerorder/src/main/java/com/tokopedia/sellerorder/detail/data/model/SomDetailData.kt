package com.tokopedia.sellerorder.detail.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl

/**
 * Created by fwidjaja on 2019-10-03.
 */
data class SomDetailData(
    val dataObject: Any = Any(),
    val typeLayout: String = ""
): Visitable<SomDetailAdapterFactoryImpl> {
    override fun type(typeFactory: SomDetailAdapterFactoryImpl): Int {
        return typeFactory.type(typeLayout)
    }
}
