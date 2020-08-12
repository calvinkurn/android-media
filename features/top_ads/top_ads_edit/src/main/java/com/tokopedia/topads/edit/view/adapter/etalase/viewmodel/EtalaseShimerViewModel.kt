package com.tokopedia.topads.edit.view.adapter.etalase.viewmodel

import com.tokopedia.topads.edit.view.adapter.etalase.EtalaseAdapterTypeFactory
import com.tokopedia.topads.edit.view.adapter.etalase.viewmodel.EtalaseViewModel


/**
 * Author errysuprayogi on 11,November,2019
 */
class EtalaseShimerViewModel: EtalaseViewModel() {

    override fun type(typesFactory: EtalaseAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}