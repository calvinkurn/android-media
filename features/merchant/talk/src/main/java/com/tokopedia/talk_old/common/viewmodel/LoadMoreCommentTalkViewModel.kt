package com.tokopedia.talk_old.common.viewmodel


import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk_old.common.adapter.ProductTalkChildThreadTypeFactory

class LoadMoreCommentTalkViewModel(var counter: Int = 0,
                                   val talkId : String = "",
                                   val shopId : String = "",
                                   val allowReply : Boolean = false,
                                   val productId: String) :
        Visitable<ProductTalkChildThreadTypeFactory> {

    override fun type(typeFactory: ProductTalkChildThreadTypeFactory): Int {
        return typeFactory.type(this)
    }


}
