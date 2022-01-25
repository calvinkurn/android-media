package com.tokopedia.vouchercreation.product.moremenu.data.model

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.product.moremenu.adapter.factory.MenuAdapterFactory

sealed class MoreMenuUiModel(
    open val title: String? = null,
    @DrawableRes
    open val icon: Int? = null
) : Visitable<MenuAdapterFactory> {

    override fun type(typeFactory: MenuAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class EditQuota(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class ShareVoucher(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class EditPeriod(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class ViewDetail(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class BroadCast(
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

    object ItemDivider : MoreMenuUiModel()
}