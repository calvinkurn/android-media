package com.tokopedia.vouchercreation.shop.voucherlist.model.ui

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.HeaderChipFactory
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.headerchips.ChipType

/**
 * Created By @ilhamsuaib on 20/04/20
 */

sealed class BaseHeaderChipUiModel(
        open var isVisible: Boolean = true
) : Visitable<HeaderChipFactory> {

    override fun type(typeFactory: HeaderChipFactory): Int = typeFactory.type(this)

    data class HeaderChip(
            var text: String = "",
            @ChipType val type: Int,
            var isActive: Boolean = false
    ) : BaseHeaderChipUiModel()

    data class ResetChip(
            override var isVisible: Boolean = false
    ) : BaseHeaderChipUiModel(isVisible)
}
