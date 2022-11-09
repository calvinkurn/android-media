package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import javax.inject.Inject

class GetInitiateVoucherPageMapper @Inject constructor() {

    fun map(response: GetInitiateVoucherPageResponse): VoucherCreationMetadata {
        return VoucherCreationMetadata(
            response.getInitiateVoucherPage.data.accessToken,
            response.getInitiateVoucherPage.data.isEligible,
            response.getInitiateVoucherPage.data.maxProduct,
            response.getInitiateVoucherPage.data.prefixVoucherCode,
            response.getInitiateVoucherPage.data.shopId.toLongOrZero(),
            response.getInitiateVoucherPage.data.token,
            response.getInitiateVoucherPage.data.userId.toLongOrZero()
        )

    }
}
