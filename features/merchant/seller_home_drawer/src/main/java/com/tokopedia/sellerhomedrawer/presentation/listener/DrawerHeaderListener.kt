package com.tokopedia.sellerhomedrawer.presentation.listener

import com.tokopedia.user.session.UserSessionInterface

interface DrawerHeaderListener {

    fun onGoToDeposit()
    fun onGoToProfile()
    fun onGoToTopPoints(topPointsUrl: String?)
    fun onGoToProfileCompletion()
    fun onWalletBalanceClicked(redirectUrlBalance: String?, appLinkBalance: String?)
    fun onWalletActionButtonClicked(redirectUrlActionButton: String?, appLinkActionButton: String?)
    fun onTokoPointActionClicked(mainPageUrl: String?, title: String?)
    fun onGotoTokoCard()

    val userSession: UserSessionInterface
}