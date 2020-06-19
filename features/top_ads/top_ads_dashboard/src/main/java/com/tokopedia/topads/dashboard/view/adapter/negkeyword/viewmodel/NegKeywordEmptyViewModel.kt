package com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.negkeyword.NegKeywordAdapterTypeFactory
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordViewModel

/**
 * Created by Pika on 7/6/20.
 */
class NegKeywordEmptyViewModel : NegKeywordViewModel(){
    override fun type(typesFactory: NegKeywordAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}