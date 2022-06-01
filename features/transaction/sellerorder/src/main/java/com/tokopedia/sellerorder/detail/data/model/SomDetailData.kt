package com.tokopedia.sellerorder.detail.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory

/**
 * Created by fwidjaja on 2019-10-03.
 */
data class SomDetailData(
    val dataObject: Any = Any(),
    val typeLayout: String = ""
): Visitable<SomDetailAdapterFactory> {
    override fun type(typeFactory: SomDetailAdapterFactory): Int {
        return typeFactory.type(typeLayout)
    }
}