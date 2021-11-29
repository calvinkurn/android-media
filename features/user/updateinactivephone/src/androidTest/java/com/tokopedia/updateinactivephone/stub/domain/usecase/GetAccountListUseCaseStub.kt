package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListParam
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase
import javax.inject.Inject

class GetAccountListUseCaseStub @Inject constructor(
    @ApplicationContext repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetAccountListUseCase(repository, dispatcher) {

    var response = AccountListDataModel()

    override suspend fun execute(params: GetAccountListParam): AccountListDataModel {
        return response
    }
}