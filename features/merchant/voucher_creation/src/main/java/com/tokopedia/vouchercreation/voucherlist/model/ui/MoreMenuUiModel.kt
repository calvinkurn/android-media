package com.tokopedia.vouchercreation.voucherlist.model.ui

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.MenuAdapterFactory

/**
 * Created By @ilhamsuaib on 18/04/20
 */

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

    data class Duplicate(
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

    object InformationTicker : MoreMenuUiModel()
}