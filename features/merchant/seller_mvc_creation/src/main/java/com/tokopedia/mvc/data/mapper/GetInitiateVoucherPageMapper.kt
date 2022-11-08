package com.tokopedia.mvc.data.mapper

import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import javax.inject.Inject

class GetInitiateVoucherPageMapper @Inject constructor() {

    fun map(response: GetInitiateVoucherPageResponse): VoucherCreationMetadata {
        return with(response.getInitiateVoucherPage.data) {
            VoucherCreationMetadata(
                accessToken,
                isEligible,
                maxProduct,
                prefixVoucherCode,
                shopId,
                token,
                userId
            )
        }
    }
}
