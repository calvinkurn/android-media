package com.tokopedia.notifcenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AffiliateEducationArticleUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : FlowUseCase<Unit, Resource<AffiliateEducationArticleResponse>>(dispatchers.io) {

    override fun graphqlQuery(): String = """
            {
              cardsArticle(filter: "latest",  limit: 5, offset: 0, source: "affiliate", sort_by: "start_datetime") {
                data {
                  status
                  cards {
                    id
                    has_more
                    title
                    items {
                      id
                      title
                      description
                      slug
                      modified_date
                      publish_time
                      categories {
                        id
                        title
                        level
                      }
                      thumbnail {
                        desktop
                        mobile
                        android
                        ios
                      }
                      attributes {
                        read_time
                      }
                      youtube_url
                      duration
                      upload_datetime
                      modified_date
                      publish_time
                    }
                    action_title
                    action_link
                    app_action_link
                    limit
                    offset
                    total_count
                  }
                }
              }
            }
        """

    override suspend fun execute(
        params: Unit
    ): Flow<Resource<AffiliateEducationArticleResponse>> = flow {
        emit(Resource.loading(null))
        val response = repository.request<Unit, AffiliateEducationArticleResponse>(
            graphqlQuery(),
            params
        )
        emit(Resource.success(response))
    }
}
