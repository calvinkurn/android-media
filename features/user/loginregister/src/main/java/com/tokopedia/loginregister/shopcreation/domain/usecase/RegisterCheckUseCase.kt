package com.tokopedia.loginregister.shopcreation.domain.usecase

import android.text.TextUtils
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.loginregister.shopcreation.di.ShopCreationQueryConstant
import com.tokopedia.loginregister.shopcreation.domain.param.RegisterCheckParam
import com.tokopedia.loginregister.shopcreation.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.shopcreation.domain.pojo.RegisterCheckPojo
import com.tokopedia.profilecommon.domain.usecase.BaseUseCaseWithParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-12-27.
 * ade.hadian@tokopedia.com
 */

class RegisterCheckUseCase @Inject constructor(
        @Named(ShopCreationQueryConstant.MUTATION_REGISTER_CHECK)
        private val query: String,
        private val graphqlRepository: GraphqlRepository,
        private val dispatcherProvider: CoroutineDispatchers
) : BaseUseCaseWithParam<RegisterCheckParam, Result<RegisterCheckData>>() {

    override suspend fun getData(parameter: RegisterCheckParam): Result<RegisterCheckData> {
        val response = withContext(dispatcherProvider.io) {
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val request = GraphqlRequest(
                    query,
                    RegisterCheckPojo::class.java,
                    parameter.toMap()
            )

            return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        }

        response.getError(RegisterCheckPojo::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)) {
                    return onFailedRegisterCheck(Throwable(it[0].message))
                }
            }
        }

        return onSuccessRegisterCheck(response.getSuccessData())
    }

    private fun onSuccessRegisterCheck(registerCheck: RegisterCheckPojo)
            : Result<RegisterCheckData> = Success(registerCheck.data)

    private fun onFailedRegisterCheck(throwable: Throwable): Result<RegisterCheckData> =
            Fail(throwable)
}