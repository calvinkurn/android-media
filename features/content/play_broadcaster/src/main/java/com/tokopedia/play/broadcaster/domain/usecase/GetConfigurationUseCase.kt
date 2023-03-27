package com.tokopedia.play.broadcaster.domain.usecase

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.types.ContentCommonUserType.VALUE_TYPE_ID_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.VALUE_TYPE_ID_USER
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.GetBroadcasterAuthorConfigResponse
import com.tokopedia.play_common.util.device.PlayDeviceSpec
import javax.inject.Inject

/**
 * Created by mzennis on 14/06/20.
 */
@GqlQuery(GetConfigurationUseCase.QUERY_NAME, GetConfigurationUseCase.QUERY_BROADCASTER_GET_AUTHOR_CONFIG)
class GetConfigurationUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val deviceSpec: PlayDeviceSpec,
    private val gson: Gson,
) : GraphqlUseCase<GetBroadcasterAuthorConfigResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetConfigurationUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetBroadcasterAuthorConfigResponse::class.java)
    }

    private fun createRequestParams(authorId: String, authorType: String) {
        val request = mapOf(
            PARAMS_AUTHOR_ID to authorId.toLong(),
            PARAMS_AUTHOR_TYPE to when (authorType) {
                TYPE_USER -> VALUE_TYPE_ID_USER
                TYPE_SHOP -> VALUE_TYPE_ID_SHOP
                else -> 0
            },
            PARAMS_WITH_CHANNEL_STATE to VALUE_WITH_CHANNEL_STATE,
            PARAMS_DEVICE_SPECS to generateDeviceSpecJson()
        )
        setRequestParams(request)
    }

    suspend fun execute(authorId: String, authorType: String): GetBroadcasterAuthorConfigResponse {
        this.createRequestParams(authorId, authorType)
        return executeOnBackground()
    }

    private fun generateDeviceSpecJson(): String {
        val deviceSpec = DeviceSpecModel(
            deviceType = deviceSpec.deviceType,
            androidSpecs = DeviceSpecModel.Info(
                apiLevel = deviceSpec.api,
                totalRam = deviceSpec.totalRam,
                chipset = deviceSpec.chipset,
                manufacturer = deviceSpec.manufacturer,
            )
        )

        return gson.toJson(deviceSpec)
    }

    data class DeviceSpecModel(
        @SerializedName("device_type")
        val deviceType: String,
        @SerializedName("android_specs")
        val androidSpecs: Info,
    ) {
        data class Info(
            @SerializedName("api_level")
            val apiLevel: Int,
            @SerializedName("total_ram")
            val totalRam: Long,
            @SerializedName("chipset")
            val chipset: String,
            @SerializedName("manufacturer")
            val manufacturer: String,
        )
    }

    companion object {
        private const val PARAMS_AUTHOR_ID = "authorID"
        private const val PARAMS_AUTHOR_TYPE = "authorType"
        private const val PARAMS_WITH_CHANNEL_STATE = "withChannelState"
        private const val PARAMS_DEVICE_SPECS = "deviceSpecs"

        private const val VALUE_WITH_CHANNEL_STATE = true

        const val QUERY_NAME = "GetConfigurationUseCaseQuery"
        /** TODO: dont forget to add PARAMS_DEVICE_SPECS in this QUERY PARAM */
        /** TODO: dont forget to add beautificationConfig in this QUERY BODY */
        const val QUERY_BROADCASTER_GET_AUTHOR_CONFIG = """
            query BroadcasterGetAuthorConfig(
                ${"$$PARAMS_AUTHOR_ID"}: Int64!, 
                ${"$$PARAMS_AUTHOR_TYPE"}: Int!, 
                ${"$$PARAMS_WITH_CHANNEL_STATE"}: Boolean
            ) {
              broadcasterGetAuthorConfig(
                 $PARAMS_AUTHOR_ID: ${"$$PARAMS_AUTHOR_ID"}, 
                 $PARAMS_AUTHOR_TYPE: ${"$$PARAMS_AUTHOR_TYPE"}, 
                 $PARAMS_WITH_CHANNEL_STATE: ${"$$PARAMS_WITH_CHANNEL_STATE"}
              ) {
                streamAllowed
                shortVideoAllowed
                isBanned
                config
                tnc {
                  description
                }
              }
            }
        """
    }
}
