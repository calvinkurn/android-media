package com.tokopedia.mvc.presentation.list.model

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.mvc.presentation.bottomsheet.adapter.factory.MenuAdapterFactory

sealed class MoreMenuUiModel(
    open val title: String? = null,
    @DrawableRes
    open val icon: Int? = null
) : Visitable<MenuAdapterFactory> {

    override fun type(typeFactory: MenuAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class Coupon(
        override val title: String,
        @DrawableRes
        override val icon: Int = IconUnify.COUPON
    ): MoreMenuUiModel(title,icon)


    data class Calendar(
        override val title: String,
        @DrawableRes
        override val icon: Int = IconUnify.CALENDAR
    ): MoreMenuUiModel(title,icon)

    data class Edit(
        override val title: String,
        @DrawableRes
        override val icon: Int = IconUnify.EDIT
    ): MoreMenuUiModel(title,icon)

    data class Clipboard(
        override val title: String,
        @DrawableRes
        override val icon: Int = IconUnify.CLIPBOARD
    ): MoreMenuUiModel(title,icon)

    data class Broadcast(
        override val title: String,
        @DrawableRes
        override val icon: Int = IconUnify.BROADCAST
    ): MoreMenuUiModel(title,icon)

    data class Download(
        override val title: String,
        @DrawableRes
        override val icon: Int = IconUnify.DOWNLOAD
    ): MoreMenuUiModel(title,icon)

    data class Clear(
        override val title: String,
        @DrawableRes
        override val icon: Int = IconUnify.CLEAR
    ): MoreMenuUiModel(title,icon)

    object ItemDivider : MoreMenuUiModel()
}
