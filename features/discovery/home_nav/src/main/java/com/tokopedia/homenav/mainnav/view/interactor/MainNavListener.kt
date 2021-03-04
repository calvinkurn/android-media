package com.tokopedia.homenav.mainnav.view.interactor

import com.tokopedia.homenav.base.diffutil.HomeNavListener

interface MainNavListener : HomeNavListener{

    fun onProfileLoginClicked()

    fun onProfileRegisterClicked()

    fun onProfileSectionClicked()

    fun onErrorProfileRefreshClicked(position: Int)

    fun onErrorShopInfoRefreshClicked(position: Int)

    fun onErrorBuListClicked(position: Int)

    fun onErrorTransactionListClicked(position: Int)
}