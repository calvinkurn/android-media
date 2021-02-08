package com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapterTypeFactory

/**
 * Created by Pika on 7/6/20.
 */
class KeywordEmptyModel : KeywordModel() {
    override fun type(typesFactory: KeywordAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}