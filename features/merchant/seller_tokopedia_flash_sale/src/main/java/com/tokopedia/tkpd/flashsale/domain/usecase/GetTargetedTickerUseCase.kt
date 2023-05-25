package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tkpd.flashsale.data.response.GetTargetedTickerResponse
import javax.inject.Inject

class GetTargetedTickerUseCase @Inject constructor(
    val repository: GraphqlRepository,
    val mapper: TargetedTickerMapper
) : GraphqlUseCase<GetTargetedTickerResponse>(repository)
