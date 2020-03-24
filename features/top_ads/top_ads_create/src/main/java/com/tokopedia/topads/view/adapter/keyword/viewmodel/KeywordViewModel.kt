package com.tokopedia.topads.view.adapter.keyword.viewmodel

import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapterTypeFactory

/**
 * Author errysuprayogi on 12,November,2019
 */
abstract class KeywordViewModel {
    abstract fun type(typesFactory: KeywordListAdapterTypeFactory): Int
}



