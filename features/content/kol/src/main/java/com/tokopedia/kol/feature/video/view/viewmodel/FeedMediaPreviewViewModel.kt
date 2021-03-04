package com.tokopedia.kol.feature.video.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTag
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kol.feature.post.view.viewmodel.PostDetailFooterModel
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailWishlistedUseCase
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject

class FeedMediaPreviewViewModel @Inject constructor(baseDispatcher: CoroutineDispatcher,
                                                    private val userSession: UserSessionInterface,
                                                    private val getPostDetailUseCase: GetPostDetailWishlistedUseCase,
                                                    private val likeKolPostUseCase: LikeKolPostUseCase,
                                                    private val addWishListUseCase: AddWishListUseCase,
                                                    private val removeWishListUseCase: RemoveWishListUseCase,
                                                    private val atcUseCase: AddToCartUseCase)
    : BaseViewModel(baseDispatcher){

    val isSessionActive: Boolean
        get() = userSession.isLoggedIn
    var postId = "0"
    var targetType = ""

    companion object{
        private const val POST_NOT_FOUND = "Post tidak ditemukan"
        private const val ERROR_CUSTOM_MESSAGE = "Terjadi kesalahan koneksi. Silakan coba lagi."
    }

    val postDetailLive = MutableLiveData<Result<PostDetailViewModel>>()
    val postFooterLive = MutableLiveData<Pair<PostDetailFooterModel, TemplateFooter?>>()
    val postTagLive = MutableLiveData<PostTag>()

    fun getPostDetail(){
        getPostDetailUseCase.execute(GetPostDetailUseCase
                .createRequestParams(userSession.userId, "",
                        GetDynamicFeedUseCase.FeedV2Source.Detail, postId),
                object : Subscriber<PostDetailViewModel>() {
                    override fun onNext(t: PostDetailViewModel?) {
                        if (t == null) {
                            onError(Throwable(POST_NOT_FOUND))
                            return
                        }
                        postDetailLive.value = Success(t)

                        val dynamicPost = t.dynamicPostViewModel.postList.firstOrNull() as DynamicPostViewModel?
                        postFooterLive.value = t.footerModel to dynamicPost?.template?.cardpost?.footer
                        dynamicPost?.postTag?.let {
                            postTagLive.value = it
                        }
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
        addWishListUseCase.unsubscribe()
        removeWishListUseCase.unsubscribe()
        atcUseCase.unsubscribe()
    }

    fun isMyOwnPost(authorID: String): Boolean = authorID == userSession.userId || authorID == userSession.shopId

    fun isMyShop(shopId: String): Boolean = shopId == userSession.shopId

    fun doLikePost(isLikeAction: Boolean, onFail: (Throwable) -> Unit) {
        likeKolPostUseCase.execute(LikeKolPostUseCase.getParam(postId.toInt(),
                if (isLikeAction) LikeKolPostUseCase.LikeKolPostAction.Like else LikeKolPostUseCase.LikeKolPostAction.Unlike),
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

    fun toggleWishlist(isWishlistAction: Boolean, productId: String, position: Int, onFail: (String)->Unit){
        if (isWishlistAction){
            addWishlist(productId, position, onFail)
        } else {
            removeWishlist(productId, position, onFail)
        }
    }

    private fun removeWishlist(productId: String, position: Int, onFail: (String) -> Unit) {
        removeWishListUseCase.createObservable(productId, userSession.userId,
                object : WishListActionListener {
                    override fun onSuccessRemoveWishlist(productId: String?) {
                        val prodTags = postTagLive.value ?:
                        (postDetailLive.value as? Success)?.data?.let {
                            (it.dynamicPostViewModel.postList.firstOrNull() as DynamicPostViewModel?)?.postTag
                        }

                        if (prodTags == null || position >= prodTags.items.size){
                            onErrorRemoveWishlist(ERROR_CUSTOM_MESSAGE, productId)
                            return
                        }

                        prodTags.items[position].isWishlisted = false
                        postTagLive.value = prodTags
                    }
                    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                        onFail.invoke(errorMessage ?: ERROR_CUSTOM_MESSAGE)
                    }

                    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}

                    override fun onSuccessAddWishlist(productId: String?) {}
        })
    }

    private fun addWishlist(productId: String, position: Int, onFail: (String) -> Unit) {
        addWishListUseCase.createObservable(productId, userSession.userId,
                object : WishListActionListener{
                    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                        onFail.invoke(errorMessage ?: ERROR_CUSTOM_MESSAGE)
                    }

                    override fun onSuccessAddWishlist(productId: String?) {
                        val prodTags = postTagLive.value ?:
                        (postDetailLive.value as? Success)?.data?.let {
                            (it.dynamicPostViewModel.postList.firstOrNull() as DynamicPostViewModel?)?.postTag
                        }

                        if (prodTags == null || position >= prodTags.items.size){
                            onErrorRemoveWishlist(ERROR_CUSTOM_MESSAGE, productId)
                            return
                        }

                        prodTags.items[position].isWishlisted = true
                        postTagLive.value = prodTags
                    }

                    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

                    override fun onSuccessRemoveWishlist(productId: String?) {}

                })
    }

    fun addToCart(tagItem: PostTagItem, success: (PostTagItem)->Unit,
                  fail: (Throwable?, PostTagItem)->Unit) {
        atcUseCase.execute(AddToCartUseCase.getMinimumParams(tagItem.id, tagItem.shop.first().shopId, productName = tagItem.text,
                price = tagItem.price, userId = userSession.userId),
                object: Subscriber<AddToCartDataModel>(){
                    override fun onNext(t: AddToCartDataModel?) {
                        if (t == null || t.data.success == 0)
                            onError(Throwable())
                        else
                            success(tagItem)
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        fail(e, tagItem)
                    }

                })
    }
}