package com.tokopedia.vouchercreation.voucherlist.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.HeaderChipFactory
import com.tokopedia.vouchercreation.voucherlist.view.widget.headerchips.ChipType

/**
 * Created By @ilhamsuaib on 20/04/20
 */

data class HeaderChipUiModel(
        var text: String = "",
        @ChipType
        val type: Int,
        var isActive: Boolean = false,
        var isVisible: Boolean = true
) : Visitable<HeaderChipFactory> {

    override fun type(typeFactory: HeaderChipFactory): Int = typeFactory.type(this)
}