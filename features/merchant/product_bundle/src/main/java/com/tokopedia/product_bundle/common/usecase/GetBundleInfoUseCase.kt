package com.tokopedia.product_bundle.common.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import javax.inject.Inject

class GetBundleInfoUseCase @Inject constructor(
        repository: GraphqlRepository) : GraphqlUseCase<GetBundleInfoResponse>(repository) {

    companion object {

    }
}