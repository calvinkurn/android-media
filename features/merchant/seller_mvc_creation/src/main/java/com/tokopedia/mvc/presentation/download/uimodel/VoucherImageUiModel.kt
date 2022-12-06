package com.tokopedia.mvc.presentation.download.uimodel

import com.tokopedia.mvc.domain.entity.enums.ImageRatio


data class VoucherImageUiModel(
    val ratio: String,
    val description: String,
    val isSelected: Boolean,
    val isExpanded: Boolean,
    val imageRatio: ImageRatio,
    val imageUrl: String
)
