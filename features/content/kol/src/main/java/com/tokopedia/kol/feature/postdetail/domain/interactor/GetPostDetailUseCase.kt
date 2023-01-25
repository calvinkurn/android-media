package com.tokopedia.kol.feature.postdetail.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Share
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kol.feature.post.view.viewmodel.PostDetailFooterModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailUiModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.PostDetailUiModel
import timber.log.Timber
import javax.inject.Inject

/**
 * @author by nisie on 26/03/19.
 */
class GetPostDetailUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val getDynamicFeedUseCase: GetDynamicFeedNewUseCase,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DynamicFeedDomainModel>(graphqlRepository) {

    suspend fun execute(cursor: String = "", limit: Int = 5, detailId: String = ""): PostDetailUiModel {
        try {
            val dynamicFeedDomainModel = getDynamicFeedUseCase.execute(cursor, limit, detailId)
            return PostDetailUiModel(
                dynamicFeedDomainModel,
                convertToPostDetailFooterModel(dynamicFeedDomainModel)
            )
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
    }

    suspend fun executeForCDPRevamp(cursor: String = "", limit: Int = 5, detailId: String = ""): ContentDetailUiModel {
        try {
            val feedXData = getDynamicFeedUseCase.executeForCDP(cursor, limit, detailId)
            return ContentDetailUiModel(
                postList = feedXData.feedXHome.items.map { it.copyPostData() },
                cursor = ""
            )
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
    }

    private fun convertToPostDetailFooterModel(dynamicFeedDomainModel: DynamicFeedDomainModel):
        PostDetailFooterModel {
        val footerModel = PostDetailFooterModel()
        if (dynamicFeedDomainModel.postList.isNotEmpty()) {
            val dynamicPostViewModel = dynamicFeedDomainModel.postList[0]
            if (dynamicPostViewModel is DynamicPostUiModel) {
                val feedXCard = dynamicPostViewModel.feedXCard
                footerModel.contentId = feedXCard.id
                footerModel.isLiked = feedXCard.like.isLiked
                footerModel.totalLike = feedXCard.like.count
                footerModel.totalComment = feedXCard.comments.count
                val text = context?.getString(com.tokopedia.feedcomponent.R.string.feed_share_default_text)
                val desc = text.replace("%s", feedXCard.author.name)
                val title = feedXCard.author.name + " post"
                footerModel.shareData = Share(text = text, description = desc, title = title, url = feedXCard.webLink)
            }
        }
        return footerModel
    }
}
