package com.tokopedia.talk.talkdetails.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactory

/**
 * Created by Hendri on 29/08/18.
 */
data class TalkDetailsCommentViewModel(
        var avatar: String? = "",
        var name: String? = "",
        var timestamp: String? = "",
        var comment: String? = "",
        var menu: List<TalkDetailsMenuItem>? = ArrayList<TalkDetailsMenuItem>(),
        var attachedProducts: List<TalkDetailsProductAttachViewModel>? =
                ArrayList<TalkDetailsProductAttachViewModel>()) : Visitable<TalkDetailsTypeFactory> {

    override fun type(typeFactory: TalkDetailsTypeFactory?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}