package com.tokopedia.topads.common.view.adapter.etalase.uimodel

import com.tokopedia.topads.common.view.adapter.etalase.EtalaseAdapterTypeFactory
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseUiModel


/**
 * Author errysuprayogi on 11,November,2019
 */
class EtalaseShimerUiModel: EtalaseUiModel() {

    override fun type(typesFactory: EtalaseAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}