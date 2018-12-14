package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by nisie on 14/12/18.
 */
class ChatroomViewModel(val listChat: ArrayList<Visitable<*>> = ArrayList(),
                        val title: String = "",
                        val avatar: String = "",
                        val onlineStatus: String = "",
                        val isOnline: Boolean = false,
                        val canLoadMore : Boolean = false) {

}