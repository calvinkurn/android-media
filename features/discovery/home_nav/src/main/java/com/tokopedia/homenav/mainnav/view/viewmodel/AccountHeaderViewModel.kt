package com.tokopedia.homenav.mainnav.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class AccountHeaderViewModel(
        val id: Int = 0
): Visitable<MainNavTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: MainNavTypeFactory): Int {
        return typeFactory.type(this)
    }
}