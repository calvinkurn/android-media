package com.tokopedia.feedplus.domain.usecase

import android.content.Context

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.createpost.common.data.pojo.getcontentform.FeedContentResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Subscriber

/**
 * @author by yfsx on 20/06/18.
 */
class GetContentFormForFeedUseCase @Inject
constructor(@ApplicationContext private val context: Context) : GraphqlUseCase() {


    fun execute(variables: HashMap<String, Any>,
                subscriber: Subscriber<GraphqlResponse>) {
        this.clearRequest()

        val query = GQL_QUERY_CONTENT_FORM
        val request = GraphqlRequest(query, FeedContentResponse::class.java, variables)

        this.addRequest(request)
        this.execute(subscriber)
    }

    companion object {
        const val GQL_QUERY_CONTENT_FORM: String = """query ContentForm(${'$'}relatedID: [String], ${'$'}type: String, ${'$'}id: String, ${'$'}token: String) {
  feed_content_form(relatedID: ${'$'}relatedID, type: ${'$'}type, ID: ${'$'}id, token: ${'$'}token) {
    token
    type
    defaultPlaceholder
    authors {
      type
      id
      name
      thumbnail
      badge
    }
    media {
      multiple_media
      max_media
      allow_image
      allow_video
      media {
        id
        type
        removable
        media_url
      }
    }
    relatedItems {
      image
      id
      price
      title
    }
    maxTag
    defaultCaptions
    caption
    error
  }
}"""
        private const val PARAM_TYPE = "type"
        private const val PARAM_RELATED_ID = "relatedID"
        private const val PARAM_POST_ID = "id"

        fun createRequestParams(relatedIds: MutableList<String>, type: String, postId: String): HashMap<String, Any> {
            val variables = HashMap<String, Any>()
            variables[PARAM_RELATED_ID] = relatedIds
            variables[PARAM_TYPE] = type
            variables[PARAM_POST_ID] = postId
            return variables
        }
    }
}
