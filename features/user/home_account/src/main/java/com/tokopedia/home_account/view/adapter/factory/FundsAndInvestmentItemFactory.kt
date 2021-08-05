package com.tokopedia.home_account.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_account.view.adapter.uiview.SubtitleUiView
import com.tokopedia.home_account.view.adapter.uiview.TitleUiView
import com.tokopedia.home_account.view.adapter.uiview.WalletUiView
import com.tokopedia.home_account.view.adapter.viewholder.FundsAndInvestmentViewHolder
import com.tokopedia.home_account.view.adapter.viewholder.SubtitleViewHolder
import com.tokopedia.home_account.view.adapter.viewholder.TitleViewHolder
import com.tokopedia.home_account.view.listener.WalletListener

class FundsAndInvestmentItemFactory(
    private val walletListener: WalletListener
) : BaseAdapterTypeFactory(), FundsAndInvestmentTypeFactory {

    override fun type(title: TitleUiView): Int {
        return TitleViewHolder.LAYOUT
    }

    override fun type(title: SubtitleUiView): Int {
        return SubtitleViewHolder.LAYOUT
    }

    override fun type(title: WalletUiView): Int {
        return FundsAndInvestmentViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TitleViewHolder.LAYOUT -> TitleViewHolder(parent)
            SubtitleViewHolder.LAYOUT -> SubtitleViewHolder(parent)
            FundsAndInvestmentViewHolder.LAYOUT -> FundsAndInvestmentViewHolder(walletListener, parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}