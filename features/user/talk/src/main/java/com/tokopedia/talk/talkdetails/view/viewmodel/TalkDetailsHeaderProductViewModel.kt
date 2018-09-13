package com.tokopedia.talk.talkdetails.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactory

/**
 * Created by Hendri on 03/09/18.
 */
data class TalkDetailsHeaderProductViewModel(
        var id:String = "",
        var image:String = "",
        var name: String = "") : Visitable<TalkDetailsTypeFactory> {

        override fun type(typeFactory: TalkDetailsTypeFactory): Int {
            return typeFactory.type(this);
        }
}