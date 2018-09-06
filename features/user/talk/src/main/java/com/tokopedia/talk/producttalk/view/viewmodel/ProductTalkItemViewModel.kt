package com.tokopedia.talk

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.common.adapter.ProductTalkChildThreadTypeFactory


/**
 * @author by Steven.
 */

data class ProductTalkItemViewModel(
        var avatar: String? = "",
        var name: String? = "",
        var timestamp: String? = "",
        var comment: String? = "",
        var menu: TalkState,
        var isRead : Boolean = false,
        var isFollowed : Boolean = false) : Visitable<ProductTalkChildThreadTypeFactory> {

    override fun type(typeFactory: ProductTalkChildThreadTypeFactory): Int {
        return typeFactory.type(this)
    }

}

data class TalkState(
        val allowReport: Boolean = false,
        val allowDelete: Boolean = false,
        val allowFollow: Boolean = false,
        val allowUnmasked: Boolean = false,
        val allowReply: Boolean = false,
        val isReported: Boolean = false,
        val isMasked: Boolean = false,
        val allowUnfollow : Boolean = false
)
