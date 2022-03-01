package com.tokopedia.vouchercreation.product.download


data class CouponImageUiModel(
    var isSelected: Boolean,
    var isExpanded: Boolean = false,
    val ratioStr: String,
    val description: String,
    val imageType: ImageType,
    val onImageOpened: (Int) -> Unit = { _ -> },
    val onCheckBoxClicked: (ImageType) -> Unit = {},
    val onChevronIconClicked: (ImageType) -> Unit = {}
)