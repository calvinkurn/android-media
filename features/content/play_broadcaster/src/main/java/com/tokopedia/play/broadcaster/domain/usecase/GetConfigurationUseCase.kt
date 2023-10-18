package com.tokopedia.play.broadcaster.domain.usecase

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.types.ContentCommonUserType.VALUE_TYPE_ID_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.VALUE_TYPE_ID_USER
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.play.broadcaster.domain.model.GetBroadcasterAuthorConfigResponse
import com.tokopedia.play_common.util.device.PlayDeviceSpec
import javax.inject.Inject

/**
 * Created by mzennis on 14/06/20.
 */
@GqlQuery(GetConfigurationUseCase.QUERY_NAME, GetConfigurationUseCase.QUERY_BROADCASTER_GET_AUTHOR_CONFIG)
class GetConfigurationUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
    private val deviceSpec: PlayDeviceSpec,
    private val gson: Gson,
) : CoroutineUseCase<GetConfigurationUseCase.RequestParam, GetBroadcasterAuthorConfigResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = GetConfigurationUseCaseQuery().getQuery()

    override suspend fun execute(params: RequestParam): GetBroadcasterAuthorConfigResponse {
        val param = mapOf(
            PARAMS_AUTHOR_ID to params.authorId.toLong(),
            PARAMS_AUTHOR_TYPE to when (params.authorType) {
                TYPE_USER -> VALUE_TYPE_ID_USER
                TYPE_SHOP -> VALUE_TYPE_ID_SHOP
                else -> 0
            },
            PARAMS_WITH_CHANNEL_STATE to VALUE_WITH_CHANNEL_STATE,
            PARAMS_DEVICE_SPECS to generateDeviceSpecJson()
        )

        return repository.request(graphqlQuery(), param)
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

    data class RequestParam(
        val authorId: String,
        val authorType: String,
    )

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
        const val QUERY_BROADCASTER_GET_AUTHOR_CONFIG = """
            query BroadcasterGetAuthorConfig(
                ${"$$PARAMS_AUTHOR_ID"}: Int64!, 
                ${"$$PARAMS_AUTHOR_TYPE"}: Int!, 
                ${"$$PARAMS_WITH_CHANNEL_STATE"}: Boolean,
                ${"$$PARAMS_DEVICE_SPECS"}: String
            ) {
              broadcasterGetAuthorConfig(
                 $PARAMS_AUTHOR_ID: ${"$$PARAMS_AUTHOR_ID"}, 
                 $PARAMS_AUTHOR_TYPE: ${"$$PARAMS_AUTHOR_TYPE"}, 
                 $PARAMS_WITH_CHANNEL_STATE: ${"$$PARAMS_WITH_CHANNEL_STATE"},
                 $PARAMS_DEVICE_SPECS: ${"$$PARAMS_DEVICE_SPECS"}
              ) {
                streamAllowed
                shortVideoAllowed
                isBanned
                hasContent
                config
                beautificationConfig
                tnc {
                  description
                }
              }
            }
        """
    }
}
