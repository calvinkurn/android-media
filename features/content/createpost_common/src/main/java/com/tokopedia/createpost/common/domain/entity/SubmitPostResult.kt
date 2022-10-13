package com.tokopedia.createpost.common.domain.entity

import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData

/**
 * Created By : Jonathan Darwin on October 13, 2022
 */
sealed interface SubmitPostResult {
    object Unknown : SubmitPostResult
    data class Success(val data: SubmitPostData?) : SubmitPostResult
}
