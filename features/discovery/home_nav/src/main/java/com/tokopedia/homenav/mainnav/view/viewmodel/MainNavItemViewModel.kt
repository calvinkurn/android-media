package com.tokopedia.homenav.mainnav.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class MainNavItemViewModel(
        val id: Int = 0,
        val srcImage: String = "",
        val itemTitle: String = "",
        val applink: String = ""
): Visitable<MainNavTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: MainNavTypeFactory): Int {
        return typeFactory.type(this)
    }
}