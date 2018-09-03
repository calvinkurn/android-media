package com.tokopedia.talk

import com.tokopedia.abstraction.base.view.adapter.Visitable


/**
 * @author by Steven.
 */

data class ProductTalkItemViewModel(
        var avatar: String? = "",
        var name: String? = "",
        var timestamp: String? = "",
        var comment: String? = "",
        var menu: List<String>) : Visitable<ProductTalkChildThreadTypeFactory> {

    override fun type(typeFactory: ProductTalkChildThreadTypeFactory): Int {
        return typeFactory.type(this)
    }

}
