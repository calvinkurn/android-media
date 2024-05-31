package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import android.annotation.SuppressLint


data class AddressUiModel (
    val localCache: String,
    override val position: Int,
): BalanceItemVisitable {
    @SuppressLint("PII Data Exposure")
    override val type: BalanceItemVisitable.Type = BalanceItemVisitable.Type.ADDRESS
    @SuppressLint("PII Data Exposure")
    override val contentType: BalanceItemVisitable.ContentType = BalanceItemVisitable.ContentType.Address
    override fun areContentsTheSame(newItem: BalanceItemVisitable): Boolean {
        return newItem == this
    }

    override fun areItemsTheSame(newItem: BalanceItemVisitable): Boolean {
        return newItem is AddressUiModel && newItem.contentType == contentType
    }

    override fun type(typeFactory: BalanceTypeFactory): Int {
        return typeFactory.type(this)
    }
}
