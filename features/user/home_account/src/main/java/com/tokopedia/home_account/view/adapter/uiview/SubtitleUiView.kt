package com.tokopedia.home_account.view.adapter.uiview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_account.view.adapter.factory.FundsAndInvestmentTypeFactory

data class SubtitleUiView(
    val subtitle: String
) : Visitable<FundsAndInvestmentTypeFactory> {

    override fun type(typeFactory: FundsAndInvestmentTypeFactory): Int {
        return typeFactory.type(this)
    }
}