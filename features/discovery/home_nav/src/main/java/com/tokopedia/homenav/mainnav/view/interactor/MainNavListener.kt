package com.tokopedia.homenav.mainnav.view.interactor

import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel

interface MainNavListener : HomeNavListener{

    fun onProfileLoginClicked()

    fun onProfileRegisterClicked()

    fun onProfileSectionClicked()

    fun onErrorProfileNameClicked(element: AccountHeaderViewModel)

    fun onErrorProfileOVOClicked(element: AccountHeaderViewModel)

    fun onErrorProfileShopClicked(element: AccountHeaderViewModel)
}