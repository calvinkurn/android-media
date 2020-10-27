package com.tokopedia.updateinactivephone.revamp.domain.usecase

import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_EMAIL
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_ID
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_NEW_ADD
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_PATH_ID_CARD
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_PATH_SELFIE
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_PHONE
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_RESOLUTION
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_SERVER_ID
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_TOKEN
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_USER_ID
import com.tokopedia.updateinactivephone.revamp.domain.api.InactivePhoneApi
import com.tokopedia.updateinactivephone.revamp.domain.api.InactivePhoneApiClient
import com.tokopedia.updateinactivephone.revamp.domain.data.UploadHostDataModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetUploadHostUseCase @Inject constructor(
        private val inactivePhoneApi: InactivePhoneApiClient<InactivePhoneApi>
) : UseCase<UploadHostDataModel>() {

    override suspend fun executeOnBackground(): UploadHostDataModel {
        useCaseRequestParams.putString(PARAM_NEW_ADD, "2")
        return inactivePhoneApi.call().getUploadHost(useCaseRequestParams.parameters)
    }
}