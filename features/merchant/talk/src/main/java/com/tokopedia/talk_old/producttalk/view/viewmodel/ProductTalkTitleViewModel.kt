package com.tokopedia.talk_old.producttalk.view.viewmodel

import com.tokopedia.talk_old.producttalk.view.adapter.ProductTalkListTypeFactory


/**
 * @author by Steven.
 */

data class ProductTalkTitleViewModel(var avatar : String, var name : String, var price : String) : ProductTalkListViewModel(){

    override fun type(typeFactory: ProductTalkListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
