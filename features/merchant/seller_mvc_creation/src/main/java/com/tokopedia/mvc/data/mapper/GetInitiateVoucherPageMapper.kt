package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import javax.inject.Inject

class GetInitiateVoucherPageMapper @Inject constructor() {

    fun map(response: GetInitiateVoucherPageResponse): VoucherCreationMetadata {
        return with(response.getInitiateVoucherPage) {
            VoucherCreationMetadata(
                data.accessToken,
                data.isEligible,
                data.maxProduct,
                data.prefixVoucherCode,
                data.shopId.toLongOrZero(),
                data.token,
                data.userId.toLongOrZero(),
                data.discountActive,
                header.messages.firstOrNull()
            )
        }
    }
}
