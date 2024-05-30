package com.tokopedia.stories.widget.settings.data.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.widget.settings.data.model.StoriesSettingOption
import javax.inject.Inject

/**
 * @author by astidhiyaa on 3/25/24
 */
class StoriesSettingOptionsUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<StoriesSettingOptionsUseCase.Param, StoriesSettingOption>(dispatchers.io) {

    private val queryObject = StoriesSettingOptionsUseCaseQuery()

    @GqlQuery(QUERY_NAME, QUERY)
    override fun graphqlQuery(): String {
        return queryObject.getQuery()
    }

    override suspend fun execute(params: Param): StoriesSettingOption {
        return graphqlRepository.request(queryObject, params)
    }

    data class Param(
        @SerializedName("req")
        val req: Author,
    ) : GqlParam {
        data class Author(
            @SerializedName("authorID")
            val authorId: String,
            @SerializedName("authorType")
            val authorType: String,
        )
    }

    companion object {
        private const val QUERY_NAME = "StoriesSettingOptionsUseCaseQuery"
        private const val QUERY = """
            query getStoriesSettingOpt(${'$'}req: ContentCreatorStoryGetAuthorOptionsRequest!) {
            contentCreatorStoryGetAuthorOptions(req:${'$'}req){
                options{
                     copy 
                     optionType 
                     isDisabled            
                }
                config{
                    copy
                    applink 
                    weblink
                }
            }
        }
        """
    }
}
