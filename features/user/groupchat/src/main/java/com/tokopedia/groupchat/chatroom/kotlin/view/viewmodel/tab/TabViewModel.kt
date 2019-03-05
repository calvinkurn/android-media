package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.tab

/**
 * @author by nisie on 3/21/18.
 */

class TabViewModel(var title: String?) {
    var isActive: Boolean = false
    var isUpdated: Boolean = false

    init {
        this.isActive = false
        this.isUpdated = false
    }
}
