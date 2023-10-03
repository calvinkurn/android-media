package com.tokopedia.purchase_platform.common.feature.addons.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.SaveAddOnStateRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnStateResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SaveAddOnStateUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository
) : UseCase<SaveAddOnStateResponse>() {

    private var params: Map<String, Any>? = null
    private var isFireAndForget: Boolean = false

    @GqlQuery(SaveAddOnStateFireAndForgetQuery, queryFireAndForget)
    fun setParams(saveAddOnStateRequest: SaveAddOnStateRequest, isFireAndForget: Boolean) {
        params = mapOf("params" to saveAddOnStateRequest)
        this.isFireAndForget = isFireAndForget
    }

    @GqlQuery(SaveAddOnStateQuery, query)
    override suspend fun executeOnBackground(): SaveAddOnStateResponse {
        if (params.isNullOrEmpty()) {
            throw RuntimeException("Parameter can't be null or empty!")
        }

        val requestFireAndForget = GraphqlRequest(
            SaveAddOnStateFireAndForgetQuery(),
            SaveAddOnStateResponse::class.java,
            params
        )

        val request = GraphqlRequest(
            SaveAddOnStateQuery(),
            SaveAddOnStateResponse::class.java,
            params
        )

        return if (isFireAndForget) {
            graphqlRepository.response(listOf(requestFireAndForget))
                .getSuccessData<SaveAddOnStateResponse>()
        } else {
            graphqlRepository.response(listOf(request))
                .getSuccessData<SaveAddOnStateResponse>()
        }
    }

    companion object {
        const val SaveAddOnStateQuery = "SaveAddOnStateQuery"
        const val SaveAddOnStateFireAndForgetQuery = "SaveAddOnStateFireAndForgetQuery"
        const val query = """
            mutation SaveAddOns(${'$'}params: SaveAddOnsParams) {
              save_add_ons(params: ${'$'}params) {
                error_message
                status
                data {
                  add_ons {
                    add_on_key
                    add_on_level
                    status
                    add_on_data {
                      add_on_id
                      add_on_qty
                      add_on_price
                      add_on_unique_id
                      add_on_metadata {
                        add_on_note {
                          is_custom_note
                          to
                          from
                          notes
                        }
                      }
                    }
                    add_on_button {          
                      title
                      description
                      left_icon_url
                      right_icon_url
                      action
                    }
                    add_on_bottomsheet {
                      header_title
                      description
                      products {
                        product_name
                        product_image_url
                      }
                      ticker {
                        text
                      }
                    }
                  }
                }
              }
            }
        """

        const val queryFireAndForget = """
            mutation SaveAddOnsFireAndForget(${'$'}params: SaveAddOnsParams) {
              save_add_ons(params: ${'$'}params) {
                error_message
                status
                data {
                  add_ons {
                    add_on_key
                    add_on_level
                    status
                    add_on_data {
                      add_on_id
                      add_on_qty
                      add_on_price
                      add_on_metadata {
                        add_on_note {
                          is_custom_note
                          to
                          from
                          notes
                        }
                      }
                    }
                    add_on_button {          
                      title
                      description
                      left_icon_url
                      right_icon_url
                      action
                    }
                    add_on_bottomsheet {
                      header_title
                      description
                      products {
                        product_name
                        product_image_url
                      }
                      ticker {
                        text
                      }
                    }
                  }
                }
              }
            }
        """
    }
}
