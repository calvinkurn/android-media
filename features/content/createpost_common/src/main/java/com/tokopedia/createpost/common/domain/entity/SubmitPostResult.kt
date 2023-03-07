package com.tokopedia.createpost.common.domain.entity

/**
 * Created By : Jonathan Darwin on October 13, 2022
 */
sealed interface SubmitPostResult {
    object Unknown : SubmitPostResult
    data class Success(val data: SubmitPostData?) : SubmitPostResult
    data class Fail(val throwable: Throwable) : SubmitPostResult
}
