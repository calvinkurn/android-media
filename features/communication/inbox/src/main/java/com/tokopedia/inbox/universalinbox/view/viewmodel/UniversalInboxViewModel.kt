package com.tokopedia.inbox.universalinbox.view.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.inbox.universalinbox.data.response.UniversalInboxRecommendationWithTDN
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_NAME
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSectionUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxShopInfoUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_DEVICE
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_SOURCE
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UniversalInboxViewModel @Inject constructor(
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addWishListV2UseCase: AddToWishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val userSession: UserSessionInterface,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main), DefaultLifecycleObserver {

    private val _firstPageRecommendation = MutableLiveData<Result<UniversalInboxRecommendationWithTDN>>()
    val firstPageRecommendation: LiveData<Result<UniversalInboxRecommendationWithTDN>>
        get() = _firstPageRecommendation

    private val _morePageRecommendation = MutableLiveData<Result<List<RecommendationItem>>>()
    val morePageRecommendation: LiveData<Result<List<RecommendationItem>>>
        get() = _morePageRecommendation

    fun dummy(): List<Any> {
        return listOf(
            UniversalInboxMenuSectionUiModel("Percakapan"),
            UniversalInboxMenuUiModel(
                title = "Chat Penjual",
                icon = IconUnify.CHAT,
                counter = 2
            ),
            UniversalInboxMenuUiModel(
                title = "Chat Pembeli",
                icon = IconUnify.SHOP,
                counter = 100,
                additionalInfo = UniversalInboxShopInfoUiModel(
                    avatar = userSession.shopAvatar,
                    shopName = userSession.shopName + " ${userSession.shopName}" + " ${userSession.shopName}" + " ${userSession.shopName}"
                )
            ),
            UniversalInboxMenuSectionUiModel("Lainnya"),
            UniversalInboxMenuUiModel(
                title = "Diskusi Produk",
                icon = IconUnify.DISCUSSION,
                counter = 0
            ),
            UniversalInboxMenuUiModel(
                title = "Ulasan",
                icon = IconUnify.STAR,
                counter = 99
            ),
            UniversalInboxMenuSeparatorUiModel()
        )
    }

    fun loadTopAdsAndFirstPageRecommendation() {
        viewModelScope.launch {
            withContext(dispatcher.io) {
                try {
                    val recommendationWidget = getRecommendationList(Int.ONE)
                    val topAdsBanner = getTopAdsBannerData()
                    val data = UniversalInboxRecommendationWithTDN(recommendationWidget, topAdsBanner)
                    _firstPageRecommendation.postValue(Success(data))
                } catch (throwable: Throwable) {
                    _firstPageRecommendation.postValue(Fail(throwable))
                }
            }
        }
    }

    fun loadMoreRecommendation(page: Int) {
        viewModelScope.launch {
            withContext(dispatcher.io) {
                try {
                    val recommendationWidget = getRecommendationList(page)
                    _morePageRecommendation.postValue(Success(recommendationWidget.recommendationItemList))
                } catch (throwable: Throwable) {
                    _morePageRecommendation.postValue(Fail(throwable))
                }
            }
        }
    }

    private suspend fun getTopAdsBannerData(): List<TopAdsImageViewModel> {
        return try {
            topAdsImageViewUseCase.getImageData(
                topAdsImageViewUseCase.getQueryMap( // TODO: dummy param from notif
                    "",
                    NotificationViewModel.TOP_ADS_SOURCE,
                    "",
                    NotificationViewModel.TOP_ADS_COUNT,
                    NotificationViewModel.TOP_ADS_DIMEN_ID,
                    ""
                )
            )
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            listOf()
        }
    }

    private suspend fun getRecommendationList(page: Int): RecommendationWidget {
        val recommendationParams = GetRecommendationRequestParam(
            pageNumber = page,
            xSource = DEFAULT_VALUE_X_SOURCE,
            pageName = PAGE_NAME,
            productIds = emptyList(),
            xDevice = DEFAULT_VALUE_X_DEVICE
        )
        return getRecommendationUseCase.getData(recommendationParams).first()
    }

    fun addWishlistV2(
        model: RecommendationItem,
        actionListener: WishlistV2ActionListener
    ) {
        viewModelScope.launch {
            withContext(dispatcher.main) {
                try {
                    val productId = model.productId.toString()
                    addWishListV2UseCase.setParams(productId, userSession.userId)
                    val result = withContext(dispatcher.io) { addWishListV2UseCase.executeOnBackground() }
                    if (result is Success) {
                        actionListener.onSuccessAddWishlist(result.data, productId)
                    } else if (result is Fail) {
                        actionListener.onErrorAddWishList(result.throwable, productId)
                    }
                } catch (throwable: Throwable) {
                    Timber.d(throwable)
                    actionListener.onErrorAddWishList(throwable, model.productId.toString())
                }
            }
        }
    }

    fun removeWishlistV2(
        model: RecommendationItem,
        actionListener: WishlistV2ActionListener
    ) {
        viewModelScope.launch {
            withContext(dispatcher.main) {
                try {
                    deleteWishlistV2UseCase.setParams(model.productId.toString(), userSession.userId)
                    val result = withContext(dispatcher.io) { deleteWishlistV2UseCase.executeOnBackground() }
                    if (result is Success) {
                        actionListener.onSuccessRemoveWishlist(result.data, model.productId.toString())
                    } else if (result is Fail) {
                        actionListener.onErrorRemoveWishlist(result.throwable, model.productId.toString())
                    }
                } catch (throwable: Throwable) {
                    Timber.d(throwable)
                    actionListener.onErrorAddWishList(throwable, model.productId.toString())
                }
            }
        }
    }
}
