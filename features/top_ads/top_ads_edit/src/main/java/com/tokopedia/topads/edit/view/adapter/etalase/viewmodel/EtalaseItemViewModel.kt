package com.tokopedia.topads.edit.view.adapter.etalase.viewmodel

import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.edit.view.adapter.etalase.EtalaseAdapterTypeFactory
import com.tokopedia.topads.edit.view.adapter.etalase.viewmodel.EtalaseViewModel

/**
 * Author errysuprayogi on 11,November,2019
 */
class EtalaseItemViewModel(var checked: Boolean = false, val result: ResponseEtalase.Data.ShopShowcasesByShopID.Result): EtalaseViewModel() {

    override fun type(typesFactory: EtalaseAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}