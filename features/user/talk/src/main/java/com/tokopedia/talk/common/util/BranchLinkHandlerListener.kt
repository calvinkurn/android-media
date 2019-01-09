package com.tokopedia.talk.common.util

/**
 * @author by nisie on 28/11/18.
 */
interface BranchLinkHandlerListener{

    fun shouldHandleUrlManually(url: String): Boolean

    fun onGoToWebView(url: String, id: String)

    fun handleBranchIOLinkClick(url: String)

    fun isBranchIOLink(url: String): Boolean
}