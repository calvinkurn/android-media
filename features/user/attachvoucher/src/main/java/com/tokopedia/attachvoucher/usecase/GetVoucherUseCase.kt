package com.tokopedia.attachvoucher.usecase

import com.tokopedia.attachvoucher.data.GetVoucherResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

class GetVoucherUseCase(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GetVoucherResponse>(graphqlRepository) {

}