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

    data class EditQuotaCoupon(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class ShareCoupon(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class EditPeriodCoupon(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class ViewDetailCoupon(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class BroadCastChat(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class DownloadCoupon(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class DuplicateCoupon(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class EditCoupon(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class CancelCoupon(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    data class StopCoupon(
        override val title: String,
        @DrawableRes
        override val icon: Int
    ) : MoreMenuUiModel(title, icon)

    object ItemDivider : MoreMenuUiModel()
}