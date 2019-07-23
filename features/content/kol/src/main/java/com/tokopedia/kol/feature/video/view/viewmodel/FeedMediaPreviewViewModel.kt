package com.tokopedia.kol.feature.video.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kol.feature.post.view.viewmodel.PostDetailFooterModel
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject

class FeedMediaPreviewViewModel @Inject constructor(baseDispatcher: CoroutineDispatcher,
                                                    private val userSession: UserSessionInterface,
                                                    private val getPostDetailUseCase: GetPostDetailUseCase,
                                                    private val likeKolPostUseCase: LikeKolPostUseCase)
    : BaseViewModel(baseDispatcher){

    val isSessionActive: Boolean
        get() = userSession.isLoggedIn
    var postId = "0"

    companion object{
        private const val POST_NOT_FOUND = "Post tidak ditemukan"
    }

    val postDetailLive = MutableLiveData<Result<PostDetailViewModel>>()

    val postFooterLive = MutableLiveData<Pair<PostDetailFooterModel, TemplateFooter?>>()

    fun getPostDetail(){
        getPostDetailUseCase.execute(GetPostDetailUseCase
                .createRequestParams(userSession.userId, "",
                        GetDynamicFeedUseCase.SOURCE_DETAIL, postId),
                object : Subscriber<PostDetailViewModel>() {
                    override fun onNext(t: PostDetailViewModel?) {
                        if (t == null) {
                            onError(Throwable(POST_NOT_FOUND))
                            return
                        }
                        postDetailLive.value = Success(t)

                        val dynamicPost = t.dynamicPostViewModel.postList.firstOrNull() as DynamicPostViewModel?
                        postFooterLive.value = t.footerModel to dynamicPost?.template?.cardpost?.footer
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        e?.let { postDetailLive.value = Fail(it) }
                    }

                })
    }

    override fun onCleared() {
        super.onCleared()
        getPostDetailUseCase.unsubscribe()
        likeKolPostUseCase.unsubscribe()
    }

    fun isMyOwnPost(authorID: String): Boolean = authorID == userSession.userId || authorID == userSession.shopId

    fun doLikePost(isLikeAction: Boolean, onFail: (Throwable) -> Unit) {
        likeKolPostUseCase.execute(LikeKolPostUseCase.getParam(postId.toInt(),
                if (isLikeAction) LikeKolPostUseCase.ACTION_LIKE else LikeKolPostUseCase.ACTION_UNLIKE),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        if (t == true){
                            val mapFooter = postFooterLive.value ?:
                                    (postDetailLive.value as? Success)?.data?.let {
                                        val dynamicPost = it.dynamicPostViewModel.postList.firstOrNull() as DynamicPostViewModel?
                                        it.footerModel to dynamicPost?.template?.cardpost?.footer
                                    }
                            if (mapFooter == null)
                                onError(Throwable())
                            else {
                                val footer = mapFooter.first.copy()
                                footer.isLiked = isLikeAction
                                if (isLikeAction){
                                    footer.totalLike += 1
                                } else {
                                    footer.totalLike -= 1
                                }
                                postFooterLive.value = footer to mapFooter.second
                            }
                        } else
                            onError(Throwable())
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        onFail(e ?: Throwable())
                    }

                })
    }
}