package com.tokopedia.home_component.widget.balance


data class BalanceItemUiModel (
    val applink: String = "",
    val url: String = "",
    val imageUrl: String? = null,
    val text: String = "",
    override val position: Int,
    override val contentType: BalanceItemVisitable.ContentType,
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
