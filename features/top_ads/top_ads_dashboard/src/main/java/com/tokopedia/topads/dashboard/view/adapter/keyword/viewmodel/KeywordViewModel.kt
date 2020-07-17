package com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapterTypeFactory


abstract class KeywordViewModel {
    abstract fun type(typesFactory: KeywordAdapterTypeFactory): Int
}