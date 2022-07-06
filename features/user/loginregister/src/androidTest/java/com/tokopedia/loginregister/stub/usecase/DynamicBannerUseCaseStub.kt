package com.tokopedia.loginregister.stub.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase

class DynamicBannerUseCaseStub(graphqlUseCase: MultiRequestGraphqlUseCase): DynamicBannerUseCase(graphqlUseCase) {

    var response = DynamicBannerDataModel()

    override suspend fun executeOnBackground(): DynamicBannerDataModel {
        return response
    }
}