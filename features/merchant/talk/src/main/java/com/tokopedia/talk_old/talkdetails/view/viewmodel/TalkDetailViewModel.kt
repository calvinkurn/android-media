package com.tokopedia.talk_old.talkdetails.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by nisie on 9/16/18.
 */
data class TalkDetailViewModel(
        var listTalk: ArrayList<Visitable<*>> = ArrayList()
)