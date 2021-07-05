package com.tokopedia.topads.common.view.adapter.etalase.viewmodel

import com.tokopedia.topads.common.view.adapter.etalase.EtalaseAdapterTypeFactory


/**
 * Author errysuprayogi on 11,November,2019
 */
class EtalaseShimerViewModel: EtalaseViewModel() {

    override fun type(typesFactory: EtalaseAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}