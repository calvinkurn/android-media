package com.tokopedia.chat_common.view.adapter.viewholder.listener

/**
 * @author by nisie on 28/11/18.
 */
interface ChatLinkHandlerListener{

    fun shouldHandleUrlManually(url: String): Boolean

    fun onGoToWebView(url: String, id: String)

    fun handleBranchIOLinkClick(url: String)

    fun isBranchIOLink(url: String): Boolean
}