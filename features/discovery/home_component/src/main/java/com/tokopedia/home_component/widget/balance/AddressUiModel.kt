package com.tokopedia.home_component.widget.balance

import android.annotation.SuppressLint


data class AddressUiModel (
    override val position: Int,
): BalanceItemVisitable {
    @SuppressLint("PII Data Exposure")
    override val type: BalanceItemVisitable.Type = BalanceItemVisitable.Type.ADDRESS
    override val contentType: BalanceItemVisitable.ContentType = BalanceItemVisitable.ContentType.ADDRESS
    override fun areContentsTheSame(newItem: BalanceItemVisitable): Boolean {
        return newItem == this
    }

    override fun areItemsTheSame(newItem: BalanceItemVisitable): Boolean {
        return newItem.contentType == contentType
    }

    override fun type(typeFactory: BalanceTypeFactory): Int {
        return typeFactory.type(this)
    }
}
