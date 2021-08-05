package com.tokopedia.home_account.view.adapter.uiview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_account.view.adapter.factory.FundsAndInvestmentTypeFactory

data class WalletUiView(
    val title: String,
    val subtitle: String,
    val urlImage: String,
    val isShowActionImage: Boolean,
    val actionText: String,
    val type: String
) : Visitable<FundsAndInvestmentTypeFactory> {

    override fun type(typeFactory: FundsAndInvestmentTypeFactory): Int {
        return typeFactory.type(this)
    }
}