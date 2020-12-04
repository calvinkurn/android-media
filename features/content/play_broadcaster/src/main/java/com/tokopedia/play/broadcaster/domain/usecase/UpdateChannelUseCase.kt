package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.ChannelId
import com.tokopedia.play.broadcaster.domain.model.UpdateChannelResponse
import com.tokopedia.play.broadcaster.ui.model.PlayChannelStatus
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import javax.inject.Inject


/**
 * Created by mzennis on 26/06/20.
 */
class UpdateChannelUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : BaseUseCase<ChannelId>() {

    private var mQueryParams = QueryParams()

    override suspend fun executeOnBackground(): ChannelId {
        val gqlResponse = configureGqlResponse(graphqlRepository, mQueryParams.query, UpdateChannelResponse::class.java, mQueryParams.params, GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<UpdateChannelResponse>(UpdateChannelResponse::class.java)
        response?.updateChannel?.let {
            return it
        }
        throw DefaultErrorThrowable()
    }

    fun setQueryParams(queryParams: QueryParams) {
        mQueryParams = queryParams
    }

    data class QueryParams(
            val query: String = "",
            val params: Map<String, Any> = emptyMap()
    )

    companion object {

        private const val PARAMS_CHANNEL_ID = "channelId"

        fun createUpdateStatusRequest(
                channelId: String,
                authorId: String,
                status: PlayChannelStatus
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.AuthorID.fieldName to authorId,
                    FieldsToUpdate.Status.fieldName to status.value.toInt()
            )

            val query = buildQueryString(listOf(FieldsToUpdate.Status, FieldsToUpdate.AuthorID))

            return QueryParams(query, params)
        }

        fun createUpdateFullCoverRequest(
                channelId: String,
                authorId: String,
                coverTitle: String,
                coverUrl: String
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.AuthorID.fieldName to authorId,
                    FieldsToUpdate.Title.fieldName to coverTitle,
                    FieldsToUpdate.Cover.fieldName to coverUrl
            )

            val query = buildQueryString(listOf(FieldsToUpdate.Title, FieldsToUpdate.Cover, FieldsToUpdate.AuthorID))

            return QueryParams(query, params)
        }

        fun createUpdateCoverTitleRequest(
                channelId: String,
                authorId: String,
                coverTitle: String
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.AuthorID.fieldName to authorId,
                    FieldsToUpdate.Title.fieldName to coverTitle
            )

            val query = buildQueryString(listOf(FieldsToUpdate.Title, FieldsToUpdate.AuthorID))

            return QueryParams(query, params)
        }

        fun createUpdateBroadcastScheduleRequest(
                channelId: String,
                status: PlayChannelStatus,
                date: String
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.Status.fieldName to status.value.toInt(),
                    FieldsToUpdate.Schedule.fieldName to date,
            )

            val query = buildQueryString(listOf(FieldsToUpdate.Status, FieldsToUpdate.Schedule))

            return QueryParams(query, params)
        }

        fun createDeleteBroadcastScheduleRequest(
                channelId: String
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.Status.fieldName to PlayChannelStatus.Draft.value.toInt()
            )

            val query = buildQueryString(listOf(FieldsToUpdate.Status))

            return QueryParams(query, params)
        }

        private fun buildQueryString(fields: List<FieldsToUpdate>): String {
            return buildString {
                append("mutation UpdateChannel(${'$'}channelId: String!")
                fields.forEach { field ->
                    append(", ${'$'}")
                    append(field.fieldName)
                    append(": ")
                    append(field.gqlType)
                }
                append("){")
                appendLine()
                appendLine("broadcasterUpdateChannel(")
                appendLine(buildRequestString(fields))
                appendLine("""
                        ) {
                    channelID
                  }
                }
                """)
            }
        }

        private fun buildRequestString(fields: List<FieldsToUpdate>): String {
            return buildString {
                appendLine("req : {")

                appendLine("channelID: ${'$'}channelId,")

                append("fieldsToUpdate: [")
                append(fields.joinToString { "\"${it.fieldName}\"" })
                appendLine("],")

                fields.forEach { field ->
                    append(field.fieldName)
                    append(": ")
                    append("${'$'}")
                    append(field.fieldName)
                    appendLine(",")
                }

                appendLine("}")
            }
        }
    }

    private enum class FieldsToUpdate(val fieldName: String, val gqlType: String) {

        Status("status", "Int!"),
        AuthorID("authorID", "String"),
        Title("title", "String"),
        Cover("coverURL", "String"),
        Schedule("publishedAt", "String")
    }
}