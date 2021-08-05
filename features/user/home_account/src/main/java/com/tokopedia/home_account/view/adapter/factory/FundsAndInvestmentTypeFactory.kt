package com.tokopedia.home_account.view.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.home_account.view.adapter.uiview.SubtitleUiView
import com.tokopedia.home_account.view.adapter.uiview.TitleUiView
import com.tokopedia.home_account.view.adapter.uiview.WalletUiView

interface FundsAndInvestmentTypeFactory : AdapterTypeFactory {
    fun type(title: TitleUiView): Int
    fun type(title: SubtitleUiView): Int
    fun type(title: WalletUiView): Int
}