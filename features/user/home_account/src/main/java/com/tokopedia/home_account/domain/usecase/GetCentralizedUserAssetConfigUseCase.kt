package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import javax.inject.Inject

open class GetCentralizedUserAssetConfigUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, CentralizedUserAssetDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query get_centralized_user_asset_config(${'$'}entryPoint: String!){
          GetCentralizedUserAssetConfig(entryPoint:${'$'}entryPoint){
            asset_config_horizontal {
              title
              subtitle_color
              subtitle
              applink
              icon
              is_active
              id
              hide_title
            }
            asset_config_vertical {
              title
              subtitle_color
              subtitle
              applink
              icon
              is_active
              id
              hide_title
            }
            asset_config {
              title
              subtitle_color
              subtitle
              applink
              icon
              is_active
              id
              hide_title
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): CentralizedUserAssetDataModel {
        val mapParams = getParams(params)
        return repository.request(graphqlQuery(), mapParams)
    }

    private fun getParams(
        entryPoint: String
    ): Map<String, Any> = mapOf(
        PARAM_ENTRY_POINT to entryPoint
    )

    companion object {
        private const val PARAM_ENTRY_POINT = "entryPoint"
    }
}
