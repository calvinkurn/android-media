package com.tokopedia.people.model.userprofile

import com.tokopedia.feedcomponent.people.model.MutationUiModel

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */
class MutationUiModelBuilder {

    fun buildSuccess(
        message: String = "Yeay",
    ) = MutationUiModel.Success(message)

    fun buildError(
        message: String = "Terjadi kesalahan",
    ) = MutationUiModel.Error(message)
}
