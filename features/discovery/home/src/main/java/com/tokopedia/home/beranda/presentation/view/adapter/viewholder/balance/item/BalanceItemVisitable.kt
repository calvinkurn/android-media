package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel
 */
interface BalanceItemVisitable : Visitable<BalanceTypeFactory> {
    fun areContentsTheSame(newItem: BalanceItemVisitable): Boolean
    fun areItemsTheSame(newItem: BalanceItemVisitable): Boolean
    override fun type(typeFactory: BalanceTypeFactory): Int

    val type: Type
    val contentType: ContentType
    val position: Int

    enum class Type {
        BALANCE,
        ADDRESS,
    }

    sealed class ContentType {
        class GoPay(val isLinked: Boolean): ContentType()
        object Rewards : ContentType()
        object Address : ContentType()
    }
}
