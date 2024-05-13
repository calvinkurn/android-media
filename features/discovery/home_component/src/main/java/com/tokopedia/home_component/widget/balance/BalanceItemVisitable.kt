package com.tokopedia.home_component.widget.balance

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel
 */
interface BalanceItemVisitable : Visitable<BalanceTypeFactory> {
    fun areContentsTheSame(newItem: BalanceItemVisitable): Boolean
    fun areItemsTheSame(newItem: BalanceItemVisitable): Boolean
    override fun type(typeFactory: BalanceTypeFactory): Int

    val type: BalanceItemVisitable.Type
    val contentType: BalanceItemVisitable.ContentType
    val position: Int

    enum class Type {
        BALANCE,
        ADDRESS,
    }

    enum class ContentType(val value: String) {
        GOPAY("gopay"),
        REWARDS("rewards"),
        ADDRESS("address"),
        UNKNOWN("unknown")
    }
}
