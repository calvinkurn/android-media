package com.tokopedia.topchat.chatroom.view.listener

/**
 * @author by nisie on 10/01/19.
 */
interface HeaderMenuListener {

    fun onGoToShop()

    fun followUnfollowShop(b: Boolean)

    fun onDeleteConversation()

    fun onGoToReportUser()

    fun onClickBlockPromo()

    fun onClickAllowPromo()

    fun blockChat()

    fun unBlockChat()

    fun onGoToChatSetting()

    fun onClickHeaderMenu()

    fun onClickHeaderMenuItem(menuItemTitle: String)

}
