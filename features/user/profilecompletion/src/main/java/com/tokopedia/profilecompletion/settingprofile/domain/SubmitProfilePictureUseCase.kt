package com.tokopedia.profilecompletion.settingprofile.domain

import com.tokopedia.profilecompletion.settingprofile.data.SubmitProfilePictureData
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SubmitProfilePictureUseCase @Inject constructor(): UseCase<SubmitProfilePictureData>() {

    override suspend fun executeOnBackground(): SubmitProfilePictureData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}