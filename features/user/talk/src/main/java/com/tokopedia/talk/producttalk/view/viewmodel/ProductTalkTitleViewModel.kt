package com.tokopedia.talk.producttalk.view.viewmodel

import com.tokopedia.talk.producttalk.view.adapter.ProductTalkListTypeFactory


/**
 * @author by Steven.
 */

data class ProductTalkTitleViewModel(var avatar : String, var name : String, var price : String) : ProductTalkListViewModel(){

    override fun type(typeFactory: ProductTalkListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
