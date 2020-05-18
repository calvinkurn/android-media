package com.tokopedia.managepassword.addpassword.domain.usecase

import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordDataModel
import com.tokopedia.managepassword.common.network.ManagePasswordApi
import com.tokopedia.managepassword.common.network.ManagePasswordApiClient
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AddPasswordUseCase @Inject constructor(
        private val managePasswordApiClient: ManagePasswordApiClient<ManagePasswordApi>
) : UseCase<AddPasswordDataModel>() {

    lateinit var params: RequestParams

    override suspend fun executeOnBackground(): AddPasswordDataModel {
        return managePasswordApiClient.call().createPassword(params.parameters)
    }
}