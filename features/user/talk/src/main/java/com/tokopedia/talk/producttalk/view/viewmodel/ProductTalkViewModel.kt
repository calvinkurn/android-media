package com.tokopedia.talk.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkListTypeFactory


/**
 * @author by Steven.
 */

data class ProductTalkViewModel(
        var screen: String = "",
        var listThread: ArrayList<Visitable<ProductTalkListTypeFactory>>,
        var hasNextPage: Boolean = false,
        var page_id: Int = 0
){

}
