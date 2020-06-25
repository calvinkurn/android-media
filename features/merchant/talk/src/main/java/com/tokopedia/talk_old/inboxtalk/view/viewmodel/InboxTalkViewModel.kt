package com.tokopedia.talk_old.inboxtalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk_old.common.domain.pojo.UnreadCount

/**
 * @author by nisie on 8/29/18.
 */
data class InboxTalkViewModel(
        var screen: String = "",
        var listTalk: ArrayList<Visitable<*>> = ArrayList(),
        var hasNextPage: Boolean = false,
        var page_id: Int = 0,
        var unreadNotification: UnreadCount = UnreadCount()
)