package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetMembershipUseCaseNew @Inject constructor(
        @Named(ShopCommonParamApiConstant.QUERY_STAMP_PROGRESS) private val gqlQuery: String,
        private val gqlUseCase: GraphqlUseCase<MembershipStampProgress>
) : UseCase<MembershipStampProgress>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopId"

        @JvmStatic
        fun createRequestParams(shopId: Int): RequestParams = RequestParams.create().apply {
            putInt(PARAM_SHOP_ID, shopId)
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): MembershipStampProgress {
        gqlUseCase.setGraphqlQuery(gqlQuery)
        gqlUseCase.setTypeClass(MembershipStampProgress::class.java)
        gqlUseCase.setRequestParams(params.parameters)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        return gqlUseCase.executeOnBackground()
    }


}