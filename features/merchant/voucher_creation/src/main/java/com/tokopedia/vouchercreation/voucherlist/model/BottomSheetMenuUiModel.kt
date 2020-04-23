package com.tokopedia.vouchercreation.voucherlist.model

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.MenuAdapterFactory

/**
 * Created By @ilhamsuaib on 18/04/20
 */

sealed class BottomSheetMenuUiModel(
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
    ) : BottomSheetMenuUiModel(title, icon)

    data class Share(
            override val title: String,
            @DrawableRes
            override val icon: Int
    ) : BottomSheetMenuUiModel(title, icon)

    data class EditPeriod(
            override val title: String,
            @DrawableRes
            override val icon: Int
    ) : BottomSheetMenuUiModel(title, icon)

    data class ViewDetail(
            override val title: String,
            @DrawableRes
            override val icon: Int
    ) : BottomSheetMenuUiModel(title, icon)

    data class Download(
            override val title: String,
            @DrawableRes
            override val icon: Int
    ) : BottomSheetMenuUiModel(title, icon)

    data class Duplicate(
            override val title: String,
            @DrawableRes
            override val icon: Int
    ) : BottomSheetMenuUiModel(title, icon)

    data class CancelVoucher(
            override val title: String,
            @DrawableRes
            override val icon: Int
    ) : BottomSheetMenuUiModel(title, icon)

    data class Stop(
            override val title: String,
            @DrawableRes
            override val icon: Int
    ) : BottomSheetMenuUiModel(title, icon)

    data class ItemDivider(val type: Int = DIVIDER) : BottomSheetMenuUiModel() {

        companion object {
            const val DIVIDER = 1
            const val EMPTY_SPACE = 0
        }
    }
}