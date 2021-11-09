package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.SaldoBalanceDataModel
import com.tokopedia.home_account.domain.usecase.GetSaldoBalanceUseCase
import javax.inject.Inject

class GetSaldoBalanceUseCaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetSaldoBalanceUseCase(graphqlRepository, dispatcher) {

    var response = SaldoBalanceDataModel()

    override suspend fun execute(params: Unit): SaldoBalanceDataModel {
        return response
    }
}