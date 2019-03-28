package com.tokopedia.kol.feature.postdetail.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.kol.common.util.TimeConverter
import com.tokopedia.kol.feature.comment.data.pojo.get.Comment
import com.tokopedia.kol.feature.comment.data.pojo.get.GetKolCommentData
import com.tokopedia.kol.feature.comment.data.pojo.get.GetUserPostComment
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.PostDetailFooterModel
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel
import com.tokopedia.kol.feature.postdetail.view.viewmodel.SeeAllCommentsViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 26/03/19.
 */
class GetPostDetailUseCase @Inject constructor(
        @ApplicationContext val context: Context,
        private val getDynamicFeedUseCase: GetDynamicFeedUseCase,
        private val getKolPostDetailUseCase: GetKolPostDetailUseCase) : UseCase<PostDetailViewModel>() {


    override fun createObservable(requestParams: RequestParams): Observable<PostDetailViewModel> {
        val domain = PostDetailViewModel()
        return getPostDetail(domain, createParamDynamicFeed(requestParams))
                .flatMap { getKolComments(domain, requestParams) }
    }

    private fun createParamDynamicFeed(requestParams: RequestParams): RequestParams {
        return GetDynamicFeedUseCase.createRequestParams(
                requestParams.getString(GetDynamicFeedUseCase.PARAM_USER_ID, ""),
                requestParams.getString(GetDynamicFeedUseCase.PARAM_CURSOR, ""),
                requestParams.getString(GetDynamicFeedUseCase.PARAM_SOURCE, ""),
                requestParams.getString(GetDynamicFeedUseCase.PARAM_SOURCE_ID, "")
        )
    }

    private fun getKolComments(domain: PostDetailViewModel, requestParams: RequestParams): Observable<PostDetailViewModel> {
        return getKolPostDetailUseCase.createObservable(createParamPostComment(requestParams))
                .flatMap {

                    val data: GetKolCommentData = it.getData(GetKolCommentData::class.java)
                    val postComment: GetUserPostComment = data.getUserPostComment

                    val footerModel: PostDetailFooterModel =
                            convertToPostDetailFooterModel(domain.dynamicPostViewModel)
                    domain.footerModel = footerModel

                    if (domain.footerModel.totalComment > 0) {
                        domain.dynamicPostViewModel.postList.add(addSeeAll(domain.footerModel,
                                postComment))

                        val kolCommentViewModels = convertToKolCommentViewModelList(postComment
                                .comments).asReversed()
                        domain.dynamicPostViewModel.postList.addAll(kolCommentViewModels)
                    }

                    Observable.just(domain)
                }

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

    private fun createParamPostComment(requestParams: RequestParams): RequestParams {
        val params = RequestParams.create()
        params.putAll(GetKolPostDetailUseCase.getVariables(requestParams.getInt
        (GetKolPostDetailUseCase.PARAM_ID, 0)))
        return params
    }

    private fun convertToKolCommentViewModelList(commentList: List<Comment>):
            List<KolCommentViewModel> {
        val kolPostDetailViewModelList = ArrayList<KolCommentViewModel>()
        for (comment in commentList) {
            kolPostDetailViewModelList.add(convertToKolCommentViewModel(comment))
        }
        return kolPostDetailViewModelList
    }

    private fun convertToKolCommentViewModel(comment: Comment): KolCommentViewModel {
        return KolCommentViewModel(
                comment.id.toString(),
                comment.userID.toString(),
                comment.userPhoto,
                comment.userName,
                comment.comment,
                generateTime(comment.createTime),
                comment.isKol,
                comment.isCommentOwner
        )
    }

    private fun generateTime(rawTime: String): String {
        return TimeConverter.generateTime(context, rawTime)
    }

    private fun addSeeAll(footerModel: PostDetailFooterModel, postComment: GetUserPostComment):
            Visitable<*> {
        return SeeAllCommentsViewModel(
                postComment.postKol.id,
                footerModel.totalComment,
                footerModel.totalComment > GetKolPostDetailUseCase.DEFAULT_LIMIT
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

    }


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
}