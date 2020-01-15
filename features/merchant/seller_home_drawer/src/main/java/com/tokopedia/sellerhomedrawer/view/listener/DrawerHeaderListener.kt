package com.tokopedia.sellerhomedrawer.view.listener

interface DrawerHeaderListener {

    fun onGoToDeposit()
    fun onGoToProfile()
    fun onGoToTopPoints(topPointsUrl: String?)
    fun onGoToProfileCompletion()
    fun onWalletBalanceClicked(redirectUrlBalance: String?, appLinkBalance: String?)
    fun onWalletActionButtonClicked(redirectUrlActionButton: String?, appLinkActionButton: String?)
    fun onTokoPointActionClicked(mainPageUrl: String?, title: String?)
    fun onGotoTokoCard()
}