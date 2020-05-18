package com.tokopedia.managepassword.forgotpassword.domain.usecase

import com.tokopedia.managepassword.common.network.ManagePasswordApi
import com.tokopedia.managepassword.common.network.ManagePasswordApiClient
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
        private val managePasswordApiClient: ManagePasswordApiClient<ManagePasswordApi>
) : UseCase<ForgotPasswordDataModel>() {

    lateinit var params: RequestParams

    override suspend fun executeOnBackground(): ForgotPasswordDataModel {
        return managePasswordApiClient.call().resetPassword(params.parameters)
    }
}