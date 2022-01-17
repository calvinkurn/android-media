package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.preattach.PreAttachPayloadResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase.Param.Companion.ADDRESS_ID
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase.Param.Companion.DISTRICT_ID
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase.Param.Companion.IDS
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase.Param.Companion.IS_SELLER
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase.Param.Companion.LAT_LON
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase.Param.Companion.POSTAL_CODE
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase.Param.Companion.TYPE
import javax.inject.Inject

class GetChatPreAttachPayloadUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetChatPreAttachPayloadUseCase.Param, PreAttachPayloadResponse>(dispatchers.io) {

    override suspend fun execute(params: Param): PreAttachPayloadResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        query chatPreAttachPayload(
            $$IDS: String, 
            $$IS_SELLER: Boolean,
            $$TYPE: Int, 
            $$ADDRESS_ID: Int, 
            $$DISTRICT_ID: Int, 
            $$POSTAL_CODE: String, 
            $$LAT_LON: String
        ){
          chatPreAttachPayload(
            $IDS: $$IDS, 
            $IS_SELLER: $$IS_SELLER,
            $TYPE: $$TYPE, 
            $ADDRESS_ID: $$ADDRESS_ID, 
            $DISTRICT_ID: $$DISTRICT_ID, 
            $POSTAL_CODE: $$POSTAL_CODE, 
            $LAT_LON: $$LAT_LON
          ) {
            list {
              id
              type
              attributes
              fallback{
                message
                html
              }
            }
          }
        }
        """

    class Param(
        @SerializedName(IDS)
        val ids: String = "",
        @SerializedName(IS_SELLER)
        val isSeller: Boolean = false,
        @SerializedName(TYPE)
        val type: Int = TYPE_PRODUCT,
        @SerializedName(ADDRESS_ID)
        val addressID: Long = 0,
        @SerializedName(DISTRICT_ID)
        val districtID: Long = 0,
        @SerializedName(POSTAL_CODE)
        val postalCode: String = "",
        @SerializedName(LAT_LON)
        val latlon: String = ""
    ) : GqlParam {
        companion object {
            const val IDS = "ids"
            const val IS_SELLER = "isSeller"
            const val TYPE = "type"
            const val ADDRESS_ID = "addressID"
            const val DISTRICT_ID = "districtID"
            const val POSTAL_CODE = "postalCode"
            const val LAT_LON = "latlon"

            const val TYPE_PRODUCT = 3
        }
    }

}