package com.tokopedia.createpost.uprofile.domains

import com.tokopedia.createpost.uprofile.model.FeedXProfileHeader
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.model.UserPostModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import javax.inject.Inject

class PlayPostContentUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun getPlayPost(group: String, cursor: String, sourceType: String, sourceId: String): UserPostModel {
        val request = GraphqlRequest(
            getPlayQuery(),
            UserPostModel::class.java,
            getRequestParams(group,cursor,sourceType,sourceId)
        )

        useCase.clearRequest()
        useCase.addRequest(request)
        val response = useCase.executeOnBackground()
        return response.getData(UserPostModel::class.java)
    }

    private fun getRequestParams(group: String, cursor: String, sourceType: String, sourceId: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_GROUP] = group
        requestMap[KEY_CURSOR] = cursor
        requestMap[KEY_SOURCE_TYPE] = sourceType
        requestMap[KEY_SOURCE_ID] = sourceId
        return requestMap
    }

    companion object {
        const val KEY_GROUP = "group"
        const val KEY_CURSOR = "cursor"
        const val KEY_SOURCE_TYPE = "source_type"
        const val KEY_SOURCE_ID = "source_id"
    }

    private fun getPlayQuery(): String {
        return "query playGetContentSlot(\$group: String, \$cursor: String, \$source_type: String, \$source_id: String) {\n" +
                "  playGetContentSlot(req: {group:\$group, cursor: \$cursor, source_type: \$source_type, source_id: \$source_id}) {\n" +
                "    data {\n" +
                "      id\n" +
                "      title\n" +
                "      type\n" +
                "      items {\n" +
                "        ... on PlayChannelDetails {\n" +
                "          id\n" +
                "          title\n" +
                "          description\n" +
                "          is_live\n" +
                "          cover_url\n" +
                "          video {\n" +
                "            id\n" +
                "            orientation\n" +
                "            type\n" +
                "            cover_url\n" +
                "            autoplay\n" +
                "          }\n" +
                "          stats {\n" +
                "            view {\n" +
                "              value\n" +
                "              formatted\n" +
                "            }\n" +
                "          }\n" +
                "          configurations {\n" +
                "            has_promo\n" +
                "            reminder {\n" +
                "              is_set\n" +
                "            }\n" +
                "            promo_labels {\n" +
                "              text\n" +
                "            }\n" +
                "          }\n" +
                "          app_link\n" +
                "          web_link\n" +
                "          display_type\n" +
                "        }\n" +
                "      }\n" +
                "      hash\n" +
                "    }\n" +
                "    meta {\n" +
                "      next_cursor\n" +
                "      is_autoplay\n" +
                "      max_autoplay_in_cell\n" +
                "    }\n" +
                "  }\n" +
                "}\n"
    }
}