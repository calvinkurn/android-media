package com.tokopedia.home.account.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

class TdnBannerViewModel(val topAdsImageViewModel: TopAdsImageViewModel) :
    Visitable<AccountTypeFactory> {

    override fun type(typeFactory: AccountTypeFactory): Int {
        return typeFactory.type(this)
    }

}
