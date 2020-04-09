package com.tokopedia.talk_old.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk_old.common.adapter.ProductTalkChildThreadTypeFactory
import com.tokopedia.talk_old.common.adapter.viewmodel.TalkProductAttachmentViewModel


/**
 * @author by Steven.
 */

data class ProductTalkItemViewModel(
        var avatar: String = "",
        var name: String = "",
        var timestamp: String = "",
        var comment: String = "",
        var menu: TalkState = TalkState(),
        var isRead : Boolean = false,
        var isFollowed : Boolean = false,
        var productAttachment : ArrayList<TalkProductAttachmentViewModel> = ArrayList(),
        var rawMessage: String = "",
        var isOwner : Boolean = false,
        var shopId : String = "",
        var talkId : String = "",
        var commentId : String = "",
        var productId : String = "",
        var labelId : Int = 0,
        var labelString : String = "",
        var userId : String = "",
        var isSending : Boolean = false,
        val hasSeparator : Boolean = false):
        Visitable<ProductTalkChildThreadTypeFactory>{

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
