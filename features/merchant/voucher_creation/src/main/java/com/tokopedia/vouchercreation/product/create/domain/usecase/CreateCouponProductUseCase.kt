package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.consts.GqlRequestConstant
import com.tokopedia.vouchercreation.product.create.data.CreateCouponProductParams
import com.tokopedia.vouchercreation.product.create.data.CreateCouponProductResponse
import javax.inject.Inject

class CreateCouponProductUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<CreateCouponProductResponse>
) {

    private val params = RequestParams.create()

    companion object {
        private const val PARAM_KEY = "merchantVoucherData"
    }

    init {
        useCase.setGraphqlQuery(GqlRequestConstant.createCouponProductMutation)
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.setTypeClass(CreateCouponProductResponse::class.java)
    }


    suspend fun execute(data : CreateCouponProductParams): CreateCouponProductResponse {
        params.putObject(PARAM_KEY, data)
        useCase.setRequestParams(params.parameters)
        return useCase.executeOnBackground()
    }

}