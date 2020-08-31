package com.tokopedia.topads.view.adapter.etalase.viewmodel

import com.tokopedia.topads.view.adapter.etalase.EtalaseAdapterTypeFactory

/**
 * Author errysuprayogi on 11,November,2019
 */
abstract class EtalaseViewModel {
    abstract fun type(typesFactory: EtalaseAdapterTypeFactory): Int
}