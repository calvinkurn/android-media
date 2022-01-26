package com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.adapter.factory.MoreMenuAdapterFactory

sealed class MoreMenuUiModel(
    open val title: String? = null,
    @DrawableRes
    open val icon: Int? = null
) : Visitable<MoreMenuAdapterFactory> {

    override fun type(typeFactoryMore: MoreMenuAdapterFactory): Int {
        return typeFactoryMore.type(this)
    }

    data class EditQuotaVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class ShareVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class EditPeriodVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class ViewDetailVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class BroadCastChat(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class DownloadVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class DuplicateVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class EditVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class CancelVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class StopVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    object ItemDivider : MoreMenuUiModel()
}