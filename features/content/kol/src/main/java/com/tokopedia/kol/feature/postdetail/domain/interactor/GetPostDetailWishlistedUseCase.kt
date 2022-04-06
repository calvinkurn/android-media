package com.tokopedia.kol.feature.postdetail.domain.interactor

import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.wishlist.common.domain.interactor.GetProductIsWishlistedUseCase
import rx.Observable
import javax.inject.Inject

class GetPostDetailWishlistedUseCase @Inject constructor(private val detailUseCase: GetPostDetailUseCaseSeller,
                                                         private val isProductWishlistedUseCase: GetProductIsWishlistedUseCase)
    : UseCase<PostDetailViewModel>() {


    override fun createObservable(requestParams: RequestParams?): Observable<PostDetailViewModel> {
        return detailUseCase.createObservable(requestParams ?: RequestParams.EMPTY)
                .flatMap { checkWishListObservable(it) }
    }

    private fun checkWishListObservable(postDetailViewModel: PostDetailViewModel): Observable<PostDetailViewModel> {
        val dynamicPost = postDetailViewModel.dynamicPostViewModel.postList.firstOrNull() as? DynamicPostViewModel?
        return if (dynamicPost == null)
            Observable.just(dynamicPost)
        else {
            Observable.from(dynamicPost.postTag.items)
                    .flatMap { tagItem ->
                        isProductWishlistedUseCase.createObservable(GetProductIsWishlistedUseCase.createParams(tagItem.id))
                                .onErrorReturn { tagItem.isWishlisted }
                                .doOnNext { tagItem.isWishlisted = it }
                                .map { tagItem }
                    }.toList()
                    .doOnNext {
                        dynamicPost.postTag.items = it
                        postDetailViewModel.dynamicPostViewModel.postList[0] = dynamicPost
                    }
                    .map { postDetailViewModel }
        }
    }

    override fun unsubscribe() {
        detailUseCase.unsubscribe()
        isProductWishlistedUseCase.unsubscribe()
        super.unsubscribe()
    }
}