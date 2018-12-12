package com.tokopedia.chat_common.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by nisie on 10/12/18.
 */
interface BaseChatViewState {

    fun onShowStartTyping()

    fun onShowStopTyping()

    fun onReceiveMessageEvent(visitable: Visitable<*>)
}