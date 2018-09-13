package com.tokopedia.talk.talkdetails.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactory

/**
 * Created by Hendri on 29/08/18.
 */
data class TalkDetailsCommentViewModel(
    var avatar: String? = "",
    var name: String? = "",
    var timestamp: String? = "",
    var comment: String? = "",
    var menu: TalkState,
    var isRead : Boolean = false,
    var isFollowed : Boolean = false,
    var productAttachment : ArrayList<TalkProductAttachmentViewModel>,
    var rawMessage: String = "",
    var isOwner : Boolean = false,
    var shopId : String = "",
    var talkId : String = "",
    var commentId : String = "",
    var productId : String = "") : Visitable<TalkDetailsTypeFactory> {

    override fun type(typeFactory: TalkDetailsTypeFactory): Int {
        return typeFactory.type(this)
    }
}