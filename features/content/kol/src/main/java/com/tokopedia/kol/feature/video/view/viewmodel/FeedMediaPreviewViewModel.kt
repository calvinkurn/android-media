package com.tokopedia.kol.feature.video.view.viewmodel

import android.content.Context
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
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCaseSeller
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailWishlistedUseCase
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject

class FeedMediaPreviewViewModel @Inject constructor(baseDispatcher: CoroutineDispatcher,
                                                    private val userSession: UserSessionInterface,
                                                    private val getPostDetailUseCase: GetPostDetailWishlistedUseCase,
                                                    private val likeKolPostUseCase: LikeKolPostUseCase,
                                                    private val addWishListUseCase: AddToWishlistV2UseCase,
                                                    private val removeWishListUseCase: DeleteWishlistV2UseCase,
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
        getPostDetailUseCase.execute(GetPostDetailUseCaseSeller
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
        addWishListUseCase.cancelJobs()
        removeWishListUseCase.cancelJobs()
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

    fun toggleWishlist(isWishlistAction: Boolean, productId: String, position: Int, onFail: (String)->Unit, context: Context){
        if (isWishlistAction){
            addWishlist(productId, position, onFail, context)
        } else {
            removeWishlist(productId, position, onFail, context)
        }
    }

    private fun removeWishlist(productId: String, position: Int, onFail: (String) -> Unit, context: Context) {
        removeWishListUseCase.setParams(productId, userSession.userId)
        removeWishListUseCase.execute(
                onSuccess = {
                    val prodTags = postTagLive.value
                    (postDetailLive.value as? Success)?.data?.let {
                        (it.dynamicPostViewModel.postList.firstOrNull() as DynamicPostViewModel?)?.postTag
                    }

                    if (prodTags == null || position >= prodTags.items.size){
                        onFail.invoke(ERROR_CUSTOM_MESSAGE)
                    }

                    prodTags?.items?.get(position)?.isWishlisted = false
                    postTagLive.value = prodTags },

                onError = {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it)
                    onFail.invoke(errorMessage) })
    }

    private fun addWishlist(productId: String, position: Int, onFail: (String) -> Unit, context: Context) {
        addWishListUseCase.setParams(productId, userSession.userId)
        addWishListUseCase.execute(
                onSuccess = {
                    val prodTags = postTagLive.value ?:
                    (postDetailLive.value as? Success)?.data?.let {
                        (it.dynamicPostViewModel.postList.firstOrNull() as DynamicPostViewModel?)?.postTag
                    }

                    if (prodTags == null || position >= prodTags.items.size){
                        onFail.invoke(ERROR_CUSTOM_MESSAGE)
                    }

                    prodTags?.items?.get(position)?.isWishlisted = true
                    postTagLive.value = prodTags},

                onError = {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it)
                    onFail.invoke(errorMessage) })
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