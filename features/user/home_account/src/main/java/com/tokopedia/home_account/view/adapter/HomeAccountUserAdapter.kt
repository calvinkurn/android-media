package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.delegate.*
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class HomeAccountUserAdapter(
        listener: HomeAccountUserListener,
        financialAdapter: HomeAccountFinancialAdapter?,
        memberAdapter: HomeAccountMemberAdapter?,
        userSession: UserSessionInterface
): BaseCommonAdapter() {
    init {
        delegatesManager.addDelegate(HomeAccountUserAdapterDelegate(listener, financialAdapter, memberAdapter))
        delegatesManager.addDelegate(HomeAccountUserSettingDelegate(listener))
        delegatesManager.addDelegate(HomeAccountRecommendationTitleDelegate())
        delegatesManager.addDelegate(TopAdsHeadlineViewDelegate(userSession))
        delegatesManager.addDelegate(HomeAccountRecommendationItemDelegate(listener))
        delegatesManager.addDelegate(HomeAccountSeparatorDelegate())
        delegatesManager.addDelegate(HomeAccountRecommendationLoaderDelegate())
    }
}