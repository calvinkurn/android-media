package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.state.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AffiliateEducationArticleUseCase @Inject constructor(
    private val gqlUseCase: GraphqlUseCase<AffiliateEducationArticleResponse>
) {

    fun getEducationArticles() = flow {
        emit(Resource.loading(null))
        val response = gqlUseCase.apply {
            setTypeClass(AffiliateEducationArticleResponse::class.java)
            setGraphqlQuery(query)
        }.executeOnBackground()
        emit(Resource.success(response))
    }

    companion object {
        private val query = """
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
        """.trimIndent()
    }
}
