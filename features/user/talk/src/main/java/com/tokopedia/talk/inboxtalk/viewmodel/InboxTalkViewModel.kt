package com.tokopedia.talk.inboxtalk.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by nisie on 8/29/18.
 */
data class InboxTalkViewModel(
        var screen: String = "",
        var listTalk: ArrayList<Visitable<*>> = ArrayList()
){
}