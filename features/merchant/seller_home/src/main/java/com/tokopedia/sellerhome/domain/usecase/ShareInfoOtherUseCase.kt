package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhome.domain.gqlquery.GqlShareInfoOther
import com.tokopedia.sellerhome.domain.mapper.OtherMenuShopShareMapper
import com.tokopedia.sellerhome.domain.model.ShopShareOtherResponse
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.OtherMenuShopShareData
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ShareInfoOtherUseCase @Inject constructor(
    val graphqlRepository: GraphqlRepository,
    private val mapper: OtherMenuShopShareMapper,
): GraphqlUseCase<ShopShareOtherResponse>(graphqlRepository) {

    companion object {
        private const val SHOP_ID_KEY = "shopId"
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GqlShareInfoOther)
        setTypeClass(ShopShareOtherResponse::class.java)
    }

    suspend fun execute(shopId: String): OtherMenuShopShareData? {
        val requestParams = RequestParams.create().apply {
            putLong(SHOP_ID_KEY, shopId.toLongOrZero())
        }

        setRequestParams(requestParams.parameters)

        return mapper.mapToOtherMenuShopShareData(executeOnBackground())
    }

}