package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.domain.model.GetVariantByIdResponse
import com.tokopedia.topads.common.domain.model.GetVariantByIdInput
import com.tokopedia.topads.common.domain.query.GetVariantById
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetVariantByIdUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetVariantByIdResponse>(graphqlRepository) {

    private val ANDROID_CLIENT_ID = 1

    init {
        setGraphqlQuery(GetVariantById)
        setTypeClass(GetVariantByIdResponse::class.java)
    }

    suspend operator fun invoke(): GetVariantByIdResponse {
        setRequestParams(createRequestParam().parameters)
        return executeOnBackground()
    }

    private fun createRequestParam(): RequestParams {
        val requestParam = RequestParams.create()
        requestParam.putObject(ParamObject.INPUT, GetVariantByIdInput(shopId = userSession.shopId.toIntOrZero(), clientId = ANDROID_CLIENT_ID))
        return requestParam
    }
}

