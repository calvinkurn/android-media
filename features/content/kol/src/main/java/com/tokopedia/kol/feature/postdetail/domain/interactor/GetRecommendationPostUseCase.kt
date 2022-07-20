package com.tokopedia.kol.feature.postdetail.domain.interactor

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendationData
import javax.inject.Inject

const val GQL_QUERY : String = """
query feedXPostRecommendation(${'$'}req: FeedXPostRecommendationRequest!){
   feedXPostRecommendation(req:${'$'}req) {
    posts {
      __typename
      ... on FeedXCardPost {
        id
        author {
          id
          type
          name
          description
          badgeURL
          logoURL
          webLink
          appLink
        }
        title
        subTitle
        text
        appLink
        webLink
        actionButtonLabel
        actionButtonOperationWeb
        actionButtonOperationApp
        media {
          id
          type
          coverURL
          mediaURL
          appLink
          webLink
          tagging {
            tagIndex
            posX
            posY
          }
          mods
        }
        mediaRatio {
          width
          height
        }
        tags {
          id
          name
          coverURL
          webLink
          appLink
          star
          price
          priceFmt
          mods
        }
        hashtagAppLinkFmt
        hashtagWebLinkFmt
        like {
          label
          count
          countFmt
          likedBy
          isLiked
          mods
        }
        comm: comments {
          label
          count
          countFmt
          items {
            id
            author {
              id
              type
              name
              description
              badgeURL
              logoURL
              webLink
              appLink
            }
            text
            mods
          }
          mods
        }
        sh: share {
          label
          operation
          mods
        }
        fol: followers {
          label
          isFollowed
          count
          countFmt
          mods
        }
        publishedAt
        mods
      }
      ... on FeedXCardPlay {
        id
        author {
          id
          type
          name
          description
          badgeURL
          logoURL
          webLink
          appLink
        }
        title
        subTitle
        text
        appLink
        webLink
        actionButtonLabel
        actionButtonOperationWeb
        actionButtonOperationApp
        views {
          label
          count
          countFmt
          mods
        }
        media {
          id
          type
          coverURL
          mediaURL
          appLink
          webLink
          tagging {
            tagIndex
            posX
            posY
          }
          mods
        }
        mediaRatio {
          width
          height
        }
        tags {
          id
          name
          coverURL
          webLink
          appLink
          star
          price
          priceFmt
          mods
        }
        hashtagAppLinkFmt
        hashtagWebLinkFmt
        like {
          label
          count
          countFmt
          likedBy
          isLiked
          mods
        }
        comm: comments {
          label
          count
          countFmt
          items {
            id
            author {
              id
              type
              name
              description
              badgeURL
              logoURL
              webLink
              appLink
            }
            text
            mods
          }
          mods
        }
        sh: share {
          label
          operation
          mods
        }
        fol: followers {
          label
          isFollowed
          count
          countFmt
          mods
        }
        publishedAt
        reportable
        deletable
        playChannelID
        mods
      }
    }
    nextCursor
  }
}

"""
private const val CURSOR: String = "cursor"
private const val LIMIT = "limit"
private const val ACTIVITY_ID = "activityID"


class GetRecommendationPostUseCase @Inject constructor(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<FeedXPostRecommendationData>(graphqlRepository) {

    init {
        setTypeClass(FeedXPostRecommendationData::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GQL_QUERY)
    }
    private fun setParams(cursor: String, limit: Int, activityId: String = "") {
        val queryMap = mutableMapOf(
            CURSOR to cursor,
            LIMIT to limit
        )
        if (!TextUtils.isEmpty(activityId)) {
            queryMap[ACTIVITY_ID] = activityId
        }
        val map = mutableMapOf("req" to queryMap)
        setRequestParams(map)
    }
    suspend fun execute(cursor: String = "", limit: Int = 5, activityId: String):
            FeedXPostRecommendationData {
        this.setParams(cursor, limit, activityId)
        return executeOnBackground()
    }

}