package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.delegate.HomeAccountRecommendationItemDelegate
import com.tokopedia.home_account.view.delegate.HomeAccountUserAdapterDelegate
import com.tokopedia.home_account.view.delegate.HomeAccountUserSettingDelegate
import com.tokopedia.home_account.view.listener.HomeAccountUserListener

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class HomeAccountUserAdapter(
        listener: HomeAccountUserListener
): BaseCommonAdapter() {
    init {
        delegatesManager.addDelegate(HomeAccountUserAdapterDelegate(listener))
        delegatesManager.addDelegate(HomeAccountUserSettingDelegate(listener))
        delegatesManager.addDelegate(HomeAccountRecommendationItemDelegate(listener))
    }
}