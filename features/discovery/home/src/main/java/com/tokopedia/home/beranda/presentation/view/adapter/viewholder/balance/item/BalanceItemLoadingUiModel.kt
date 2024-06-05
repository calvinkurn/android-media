package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

class BalanceItemLoadingUiModel(
    override val contentType: BalanceItemVisitable.ContentType,
    override val position: Int
): BalanceItemVisitable {
    override val type: BalanceItemVisitable.Type = BalanceItemVisitable.Type.BALANCE
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
