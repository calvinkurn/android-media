package com.tokopedia.shop_nib.data.mapper

import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.shop_nib.data.response.SellerSubmitNIBStatusResponse
import com.tokopedia.shop_nib.domain.entity.NibSubmissionResult
import javax.inject.Inject

class SellerSubmitNibStatusMapper @Inject constructor() {

    companion object {
        private const val STATUS_ID_SUBMITTED = 1
    }
    fun map(response: SellerSubmitNIBStatusResponse) : NibSubmissionResult {
        val isSuccess = response.sellerSubmitNIBStatus.error?.message?.isEmpty().orTrue()
        val hasPreviousSubmission = response.sellerSubmitNIBStatus.result?.status == STATUS_ID_SUBMITTED
        val errorMessage = response.sellerSubmitNIBStatus.error?.message.orEmpty()
        return NibSubmissionResult(isSuccess, hasPreviousSubmission, errorMessage)
    }

}
