package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel
 */
interface BalanceVisitable : Visitable<BalanceTypeFactory> {
    fun areContentsTheSame(newItem: BalanceVisitable): Boolean
    fun areItemsTheSame(newItem: BalanceVisitable): Boolean
    fun getChangePayloadFrom(newItem: BalanceVisitable): Bundle? = null
    override fun type(typeFactory: BalanceTypeFactory): Int
}
