package com.tokopedia.talk.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.common.adapter.ProductTalkChildThreadTypeFactory
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel


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
        var isFollowed : Boolean = false,
        var productAttachment : ArrayList<TalkProductAttachmentViewModel>,
        var rawMessage: String = "") :
        Visitable<ProductTalkChildThreadTypeFactory> {

    override fun type(typeFactory: ProductTalkChildThreadTypeFactory): Int {
        return typeFactory.type(this)
    }

}

data class TalkState(
        var allowReport: Boolean = false,
        var allowDelete: Boolean = false,
        var allowFollow: Boolean = false,
        var allowUnmasked: Boolean = false,
        var allowReply: Boolean = false,
        var isReported: Boolean = false,
        var isMasked: Boolean = false,
        var allowUnfollow : Boolean = false
)
