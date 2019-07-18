package com.tokopedia.kol.feature.video.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
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
                                                    private val getPostDetailUseCase: GetPostDetailUseCase)
    : BaseViewModel(baseDispatcher){

    var postId = "0"

    companion object{
        private const val POST_NOT_FOUND = "Post tidak ditemukan"
    }

    val postDetailLive = MutableLiveData<Result<PostDetailViewModel>>()

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
    }

    fun isMyOwnPost(authorID: String): Boolean = authorID == userSession.userId || authorID == userSession.shopId
}