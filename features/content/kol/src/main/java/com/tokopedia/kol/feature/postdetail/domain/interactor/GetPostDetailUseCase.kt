package com.tokopedia.kol.feature.postdetail.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.PostDetailFooterModel
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 26/03/19.
 */
class GetPostDetailUseCase @Inject constructor(
        @ApplicationContext val context: Context,
        private val getDynamicFeedUseCase: GetDynamicFeedUseCase) : UseCase<PostDetailViewModel>() {

    companion object {
        private const val LIMIT_3 = 3

        @JvmOverloads
        fun createRequestParams(userId: String, cursor: String = "", source: String, sourceId:
        String = ""):
                RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(GetDynamicFeedUseCase.PARAM_USER_ID, userId)
            requestParams.putInt(GetDynamicFeedUseCase.PARAM_LIMIT, LIMIT_3)
            requestParams.putString(GetDynamicFeedUseCase.PARAM_CURSOR, cursor)
            requestParams.putString(GetDynamicFeedUseCase.PARAM_SOURCE, source)
            requestParams.putString(GetDynamicFeedUseCase.PARAM_SOURCE_ID, sourceId)
            requestParams.putInt(GetKolPostDetailUseCase.PARAM_ID, sourceId.toIntOrZero())
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<PostDetailViewModel> {
        val domain = PostDetailViewModel()
        return getPostDetail(domain, createParamDynamicFeed(requestParams))
                .flatMap { addFooter(domain) }
    }

    private fun createParamDynamicFeed(requestParams: RequestParams): RequestParams {
        return GetDynamicFeedUseCase.createRequestParams(
                userId = requestParams.getString(GetDynamicFeedUseCase.PARAM_USER_ID, ""),
                cursor = requestParams.getString(GetDynamicFeedUseCase.PARAM_CURSOR, ""),
                source = requestParams.getString(GetDynamicFeedUseCase.PARAM_SOURCE, ""),
                sourceId = requestParams.getString(GetDynamicFeedUseCase.PARAM_SOURCE_ID, "")
        )
    }

    private fun getPostDetail(domain: PostDetailViewModel, requestParams: RequestParams?):
            Observable<PostDetailViewModel> {
        return getDynamicFeedUseCase.createObservable(requestParams)
                .flatMap {
                    domain.dynamicPostViewModel.cursor = it.cursor
                    domain.dynamicPostViewModel.hasNext = it.hasNext
                    domain.dynamicPostViewModel.postList = it.postList
                    Observable.just(domain)
                }
                .flatMap { addFooter(domain) }

    }

    private fun addFooter(domain: PostDetailViewModel): Observable<PostDetailViewModel> {
        val footerModel: PostDetailFooterModel =
                convertToPostDetailFooterModel(domain.dynamicPostViewModel)
        domain.footerModel = footerModel
        return Observable.just(domain)

    }

    private fun convertToPostDetailFooterModel(dynamicFeedDomainModel: DynamicFeedDomainModel):
            PostDetailFooterModel {
        val footerModel = PostDetailFooterModel()
        if (dynamicFeedDomainModel.postList.isNotEmpty()) {
            val dynamicPostViewModel = dynamicFeedDomainModel.postList[0]
            if (dynamicPostViewModel is DynamicPostViewModel) {
                footerModel.contentId = dynamicPostViewModel.id
                footerModel.isLiked = dynamicPostViewModel.footer.like.isChecked
                footerModel.totalLike = dynamicPostViewModel.footer.like.value
                footerModel.totalComment = dynamicPostViewModel.footer.comment.value
                footerModel.shareData = dynamicPostViewModel.footer.share
            }
        }
        return footerModel
    }
}
