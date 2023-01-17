package com.tokopedia.topads.common.view.adapter.etalase.uimodel

import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.view.adapter.etalase.EtalaseAdapterTypeFactory
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseUiModel

/**
 * Author errysuprayogi on 11,November,2019
 */
class EtalaseItemUiModel(var checked: Boolean = false, val result: ResponseEtalase.Data.ShopShowcasesByShopID.Result): EtalaseUiModel() {

    override fun type(typesFactory: EtalaseAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}