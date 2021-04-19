package com.tokopedia.loginregister.shopcreation.domain.usecase

import android.text.TextUtils
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.loginregister.shopcreation.di.ShopCreationQueryConstant
import com.tokopedia.loginregister.shopcreation.domain.param.ShopInfoParam
import com.tokopedia.loginregister.shopcreation.domain.pojo.ShopInfoByID
import com.tokopedia.loginregister.shopcreation.domain.pojo.ShopInfoPojo
import com.tokopedia.profilecommon.domain.usecase.BaseUseCaseWithParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ade Fulki on 2020-02-06.
 * ade.hadian@tokopedia.com
 */

class ShopInfoUseCase @Inject constructor(
        @Named(ShopCreationQueryConstant.QUERY_SHOP_INFO)
        private val query: String,
        private val graphqlRepository: GraphqlRepository,
        private val dispatcherProvider: CoroutineDispatchers
) : BaseUseCaseWithParam<ShopInfoParam, Result<ShopInfoByID>>() {
    override suspend fun getData(parameter: ShopInfoParam): Result<ShopInfoByID> {
        val response = withContext(dispatcherProvider.io) {
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val request = GraphqlRequest(
                    query,
                    ShopInfoPojo::class.java,
                    parameter.toMap()
            )

            return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        }

        response.getError(ShopInfoPojo::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)) {
                    return onFailedGetShopInfo(Throwable(it[0].message))
                }
            }
        }

        return onSuccessGetShopInfo(response.getSuccessData())
    }

    private fun onSuccessGetShopInfo(shopInfoPojo: ShopInfoPojo)
            : Result<ShopInfoByID> = Success(shopInfoPojo.data)

    private fun onFailedGetShopInfo(throwable: Throwable): Result<ShopInfoByID> =
            Fail(throwable)
}