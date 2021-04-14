package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class ChatAttachmentUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatAttachmentResponse>,
        private val mapper: ChatAttachmentMapper,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun safeCancel() {
        if (coroutineContext.isActive) {
            gqlUseCase.cancelJobs()
            cancel()
        }
    }

    fun getAttachments(
            msgId: Long,
            attachmentId: String,
            userLocation: LocalCacheModel,
            onSuccess: (ArrayMap<String, Attachment>) -> Unit,
            onError: (Throwable, ArrayMap<String, Attachment>) -> Unit
    ) {
        launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(msgId, attachmentId, userLocation)
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatAttachmentResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val mapAttachment = mapper.map(response)
                    withContext(dispatchers.Main) {
                        onSuccess(mapAttachment)
                    }
                },
                {
                    val mapErrorAttachment = mapper.mapError(attachmentId)
                    withContext(dispatchers.Main) {
                        onError(it, mapErrorAttachment)
                    }
                }
        )
    }

    private fun generateParams(
            msgId: Long,
            attachmentId: String,
            userLocation: LocalCacheModel
    ): Map<String, Any> {
        val addressId = userLocation.address_id.toLongOrZero()
        val districtId = userLocation.district_id.toLongOrZero()
        val postalCode = userLocation.postal_code
        val latlon = if (userLocation.lat.isEmpty() || userLocation.long.isEmpty()) {
            ""
        } else {
            "${userLocation.lat},${userLocation.long}"
        }
        return mapOf(
                paramMsgId to msgId,
                paramLimit to attachmentId,
                paramAddressId to addressId,
                paramDistrictId to districtId,
                paramPostalCode to postalCode,
                paramLatLon to latlon,
        )
    }

    val query = """
        query chatAttachments(
            $$paramMsgId: Int!, $$paramLimit: String, $$paramAddressId: Int,
            $$paramDistrictId: Int, $$paramPostalCode: String, $$paramLatLon: String
        ) {
          chatAttachments(
            msgId: $$paramMsgId, AttachmentIDs: $$paramLimit, addressID: $$paramAddressId,
            districtID: $$paramDistrictId, postalCode: $$paramPostalCode, latlon: $$paramLatLon
          ) {
            list {
             id
              type
              attributes
              fallback {
                message
              	html
              }
              isActual
            }
          }
        }
    """.trimIndent()

    companion object {
        private val paramMsgId = "msgId"
        private val paramLimit = "AttachmentIDs"
        private val paramAddressId = "addressID"
        private val paramDistrictId = "districtID"
        private val paramPostalCode = "postalCode"
        private val paramLatLon = "latlon"
    }
}