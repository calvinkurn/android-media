package com.tokopedia.vouchercreation.shop.voucherlist.model.ui

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.SortAdapterFactory
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sortbottomsheet.SortBy

/**
 * Created By @ilhamsuaib on 22/04/20
 */

data class SortUiModel(
        val label: String,
        @SortBy val key: String,
        var isSelected: Boolean = false
) : Visitable<SortAdapterFactory> {

    override fun type(typeFactory: SortAdapterFactory): Int {
        return typeFactory.type(this)
    }
}