package com.tokopedia.mvc.domain.entity

data class VoucherDetailWithVoucherCreationMetadata(
    val voucherDetail: VoucherDetailData,
    val creationMetadata: VoucherCreationMetadata
)
