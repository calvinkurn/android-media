package com.tokopedia.play_common.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play_common.domain.model.ChannelId
import com.tokopedia.play_common.domain.model.UpdateChannelResponse
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.usecase.coroutines.UseCase


/**
 * Created by mzennis on 26/06/20.
 */
open class UpdateChannelUseCase(private val graphqlRepository: GraphqlRepository) : UseCase<ChannelId>() {

    protected var mQueryParams = QueryParams()

    override suspend fun executeOnBackground(): ChannelId {
        val gqlRequest = GraphqlRequest(
                mQueryParams.query,
                UpdateChannelResponse::class.java,
                mQueryParams.params
        )
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<UpdateChannelResponse>(UpdateChannelResponse::class.java)
        return response?.updateChannel ?: throw Throwable()
    }

    fun setQueryParams(queryParams: QueryParams) {
        mQueryParams = queryParams
    }

    data class QueryParams(
            val query: String = "",
            val params: Map<String, Any> = emptyMap()
    )

    companion object {

        const val PARAMS_CHANNEL_ID = "channelId"

        fun createUpdateStatusRequest(
                channelId: String,
                authorId: String,
                status: PlayChannelStatusType
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.AuthorID.fieldName to authorId,
                    FieldsToUpdate.Status.fieldName to status.value.toInt()
            )

            val query = buildQueryString(listOf(FieldsToUpdate.Status, FieldsToUpdate.AuthorID))

            return QueryParams(query, params)
        }

        fun buildQueryString(fields: List<FieldsToUpdate>): String {
            return buildString {
                append("mutation UpdateChannel(${'$'}channelId: String!")
                fields.forEach { field ->
                    append(", ${'$'}")
                    append(field.fieldName)
                    append(": ")
                    append(field.gqlType)
                }
                append("){")
                appendln()
                appendln("broadcasterUpdateChannel(")
                appendln(buildRequestString(fields))
                appendln("""
                        ) {
                    channelID
                  }
                }
                """)
            }
        }

        fun buildRequestString(fields: List<FieldsToUpdate>): String {
            return buildString {
                appendln("req : {")

                appendln("channelID: ${'$'}channelId,")

                append("fieldsToUpdate: [")
                append(fields.joinToString { "\"${it.fieldName}\"" })
                appendln("],")

                fields.forEach { field ->
                    append(field.fieldName)
                    append(": ")
                    append("${'$'}")
                    append(field.fieldName)
                    appendln(",")
                }

                appendln("}")
            }
        }
    }

    enum class FieldsToUpdate(val fieldName: String, val gqlType: String) {

        Status("status", "Int!"),
        AuthorID("authorID", "String"),
        Title("title", "String"),
        Cover("coverURL", "String")
    }
}