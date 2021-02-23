package com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.negkeyword.NegKeywordAdapterTypeFactory

abstract class NegKeywordModel {
    abstract fun type(typesFactory: NegKeywordAdapterTypeFactory): Int
}