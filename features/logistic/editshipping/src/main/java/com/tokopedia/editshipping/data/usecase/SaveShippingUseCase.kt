package com.tokopedia.editshipping.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.editshipping.domain.param.OngkirShippingEditorSaveInput
import com.tokopedia.editshipping.domain.param.SaveShipperParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.shippingeditor.SaveShippingEditorResponse
import javax.inject.Inject

class SaveShippingUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<OngkirShippingEditorSaveInput, SaveShippingEditorResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: OngkirShippingEditorSaveInput): SaveShippingEditorResponse {
        return gql.request(graphqlQuery(), SaveShipperParam(params))
    }

    companion object {
        private const val QUERY =
            """mutation OngkirShippingEditorSave(${'$'}input : OngkirShippingEditorSaveInput!)
        {
          ongkirShippingEditorSave(input:${'$'}input) {
            status
            message
            data {
              message
              is_success
            }
            errors {
              id
              status
              title
            }
          }
        }"""
    }
}
