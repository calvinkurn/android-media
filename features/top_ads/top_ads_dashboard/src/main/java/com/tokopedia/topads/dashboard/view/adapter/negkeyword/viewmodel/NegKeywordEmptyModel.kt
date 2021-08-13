package com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.negkeyword.NegKeywordAdapterTypeFactory

/**
 * Created by Pika on 7/6/20.
 */
class NegKeywordEmptyModel : NegKeywordModel() {
    override fun type(typesFactory: NegKeywordAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}