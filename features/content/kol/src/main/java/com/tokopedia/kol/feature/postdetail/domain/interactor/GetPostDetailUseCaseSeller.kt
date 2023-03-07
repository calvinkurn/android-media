package com.tokopedia.kol.feature.postdetail.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostModel
import com.tokopedia.kol.feature.post.view.viewmodel.PostDetailFooterModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.PostDetailUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by shruti on 26/03/19.
 */
class GetPostDetailUseCaseSeller @Inject constructor(
        @ApplicationContext val context: Context,
        private val getDynamicFeedUseCase: GetDynamicFeedUseCase,
) : UseCase<PostDetailUiModel>() {

    companion object {

        private const val LIMIT_3 = 3

        @JvmOverloads
        fun createRequestParams(userId: String, cursor: String = "", source: GetDynamicFeedUseCase.FeedV2Source, sourceId:
        String = ""):
                RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(GetDynamicFeedUseCase.PARAM_USER_ID, userId)
            requestParams.putInt(GetDynamicFeedUseCase.PARAM_LIMIT, LIMIT_3)
            requestParams.putString(GetDynamicFeedUseCase.PARAM_CURSOR, cursor)
            requestParams.putString(GetDynamicFeedUseCase.PARAM_SOURCE, source.sourceString)
            requestParams.putString(GetDynamicFeedUseCase.PARAM_SOURCE_ID, sourceId)
            requestParams.putInt(GetDynamicFeedUseCase.PARAM_ID, sourceId.toIntOrZero())
            return requestParams
        }

    }

    override fun createObservable(requestParams: RequestParams): Observable<PostDetailUiModel> {
        val domain = PostDetailUiModel.Empty
        return getPostDetail(domain, createParamDynamicFeed(requestParams))
                .flatMap { addFooter(domain) }
    }

    private fun createParamDynamicFeed(requestParams: RequestParams): RequestParams {
        return GetDynamicFeedUseCase.createRequestParams(
                userId = requestParams.getString(GetDynamicFeedUseCase.PARAM_USER_ID, ""),
                cursor = requestParams.getString(GetDynamicFeedUseCase.PARAM_CURSOR, ""),
                source = GetDynamicFeedUseCase.FeedV2Source.getSourceByString(
                        requestParams.getString(GetDynamicFeedUseCase.PARAM_SOURCE, "")
                ),
                sourceId = requestParams.getString(GetDynamicFeedUseCase.PARAM_SOURCE_ID, "")
        )
    }

    private fun getPostDetail(domain: PostDetailUiModel, requestParams: RequestParams):
            Observable<PostDetailUiModel> {
        return getDynamicFeedUseCase.createObservable(requestParams).flatMap { dynamicFeed ->
            domain.dynamicPostViewModel.cursor = dynamicFeed.cursor
            domain.dynamicPostViewModel.hasNext = dynamicFeed.hasNext
            domain.dynamicPostViewModel.postList = dynamicFeed.postList
            Observable.just(domain)
        }.flatMap { addFooter(domain) }
    }

    private fun addFooter(domain: PostDetailUiModel): Observable<PostDetailUiModel> {
        return Observable.just(domain.copy(
            footerModel = convertToPostDetailFooterModel(domain.dynamicPostViewModel)
        ))

    }

    private fun convertToPostDetailFooterModel(dynamicFeedDomainModel: DynamicFeedDomainModel):
            PostDetailFooterModel {
        val footerModel = PostDetailFooterModel()
        if (dynamicFeedDomainModel.postList.isNotEmpty()) {
            val dynamicPostViewModel = dynamicFeedDomainModel.postList[0]
            if (dynamicPostViewModel is DynamicPostModel) {
                footerModel.contentId = dynamicPostViewModel.id.toString()
                footerModel.isLiked = dynamicPostViewModel.footer.like.isChecked
                footerModel.totalLike = dynamicPostViewModel.footer.like.value
                footerModel.totalComment = dynamicPostViewModel.footer.comment.value
                footerModel.shareData = dynamicPostViewModel.footer.share
            }
        }
        return footerModel
    }
}
