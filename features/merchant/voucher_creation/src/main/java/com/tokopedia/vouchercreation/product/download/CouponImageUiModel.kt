package com.tokopedia.vouchercreation.product.download

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.DownloadVoucherFactory


data class CouponImageUiModel(
    var isSelected: Boolean,
    var isExpanded: Boolean = false,
    val ratioStr: String,
    val description: String,
    val imageType: ImageType,
    val onImageOpened: (Int) -> Unit = { _ -> },
    val onCheckBoxClicked: (ImageType) -> Unit = {},
    val onChevronIconClicked: (ImageType) -> Unit = {}
) : Visitable<DownloadVoucherFactory> {

    override fun type(typeFactory: DownloadVoucherFactory): Int {
        return typeFactory.type(this)
    }
}