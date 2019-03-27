package com.tokopedia.kol.feature.postdetail.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.kol.common.util.TimeConverter
import com.tokopedia.kol.feature.comment.data.pojo.get.*
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel
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

                    val kolPostDetailViewModelList: KolPostViewModel = convertToKolPostViewModel(postComment.postKol)
                    domain.kolPostViewModel = kolPostDetailViewModelList

                    if (postComment.postKol.isShowComment) {
                        domain.dynamicPostViewModel.postList.add(addSeeAll(postComment.postKol))

                        val kolCommentViewModels = convertToKolCommentViewModelList(postComment
                                .comments).asReversed()
                        domain.dynamicPostViewModel.postList.addAll(kolCommentViewModels)
                    }

                    Observable.just(domain)
                }

    }

    private fun createParamPostComment(requestParams: RequestParams): RequestParams {
        val params = RequestParams.create()
        params.putAll(GetKolPostDetailUseCase.getVariables(requestParams.getInt
        (GetKolPostDetailUseCase.PARAM_ID, 0)))
        return params
    }


    private fun convertToKolPostViewModel(postKol: PostKol): KolPostViewModel {
        val content = getContent(postKol)
        val tag = getKolTag(content)
        val imageList = ArrayList<String>()
        imageList.add(getImageUrl(content))

        return KolPostViewModel(
                postKol.userId,
                "",
                "",
                if (postKol.userName == null) "" else postKol.userName,
                if (postKol.userPhoto == null) "" else postKol.userPhoto,
                if (postKol.userInfo == null) "" else postKol.userInfo,
                if (postKol.userUrl == null) "" else postKol.userUrl,
                postKol.isFollowed,
                if (postKol.description == null) "" else postKol.description,
                postKol.isLiked,
                postKol.likeCount,
                postKol.commentCount,
                0,
                postKol.id,
                if (postKol.createTime == null) "" else generateTime(postKol.createTime),
                true,
                true,
                imageList,
                getTagId(tag),
                "",
                getTagType(tag),
                getTagCaption(tag),
                getTagLink(tag)
        )
    }


    private fun getContent(postKol: PostKol): Content {
        return try {
            postKol.content[0]
        } catch (e: NullPointerException) {
            Content("", listOf())
        } catch (e: IndexOutOfBoundsException) {
            Content("", listOf())
        }

    }

    private fun getKolTag(content: Content): Tag {
        return try {
            content.tags[0]
        } catch (e: NullPointerException) {
            Tag(0, "", "", "", "", "")
        } catch (e: IndexOutOfBoundsException) {
            Tag(0, "", "", "", "", "")
        }

    }

    private fun getImageUrl(content: Content?): String {
        return if (content != null && content.imageurl != null) {
            content.imageurl
        } else {
            ""
        }
    }

    private fun getTagCaption(tag: Tag?): String {
        return if (tag != null && tag.caption != null) {
            tag.caption
        } else {
            ""
        }
    }

    private fun getTagId(tag: Tag?): Int {
        return tag?.id ?: 0
    }

    private fun getTagType(tag: Tag?): String {
        return if (tag != null && tag.type != null) {
            tag.type
        } else {
            ""
        }
    }

    private fun getTagLink(tag: Tag?): String {
        return if (tag != null && tag.link != null) {
            tag.link
        } else {
            ""
        }
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

    private fun addSeeAll(postKol: PostKol): Visitable<*> {
        return SeeAllCommentsViewModel(
                postKol.id,
                postKol.commentCount,
                postKol.commentCount > GetKolPostDetailUseCase.DEFAULT_LIMIT
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
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_CURSOR = "cursor"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_SOURCE_ID = "sourceID"

        private val PARAM_ID = "idPost"

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