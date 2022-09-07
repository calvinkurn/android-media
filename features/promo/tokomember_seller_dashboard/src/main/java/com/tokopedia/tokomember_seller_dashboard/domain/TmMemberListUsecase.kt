package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TmMemberListResponse
import javax.inject.Inject

class TmMemberListUsecase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TmMemberListResponse>(graphqlRepository) {
}