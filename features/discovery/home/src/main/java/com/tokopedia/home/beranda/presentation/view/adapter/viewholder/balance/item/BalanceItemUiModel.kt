package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item


data class BalanceItemUiModel (
    val applink: String = "",
    val url: String = "",
    val imageUrl: String? = null,
    val text: String = "",
    override val contentType: BalanceItemVisitable.ContentType,
    override val position: Int,
): BalanceItemVisitable {
    override val type: BalanceItemVisitable.Type = BalanceItemVisitable.Type.BALANCE
    override fun areContentsTheSame(newItem: BalanceItemVisitable): Boolean {
        return newItem == this
    }

    override fun areItemsTheSame(newItem: BalanceItemVisitable): Boolean {
        return newItem is BalanceItemUiModel && newItem.contentType == contentType
    }

    override fun type(typeFactory: BalanceTypeFactory): Int {
        return typeFactory.type(this)
    }

    sealed class ContentType {
        class GoPay(val isLinked: Boolean): ContentType()
        object Rewards : ContentType()
        object Address : ContentType()
    }
}
