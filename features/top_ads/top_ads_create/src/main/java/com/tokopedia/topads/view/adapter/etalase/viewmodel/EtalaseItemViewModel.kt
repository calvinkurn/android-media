package com.tokopedia.topads.view.adapter.etalase.viewmodel

import com.tokopedia.topads.data.response.ResponseEtalase
import com.tokopedia.topads.view.adapter.etalase.EtalaseAdapterTypeFactory

/**
 * Author errysuprayogi on 11,November,2019
 */
class EtalaseItemViewModel(var checked: Boolean = false, val result: ResponseEtalase.Data.ShopShowcasesByShopID.Result): EtalaseViewModel() {

    override fun type(typesFactory: EtalaseAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}