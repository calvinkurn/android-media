package com.tokopedia.home_recom.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.LAYOUTTYPE_HORIZONTAL_ATC
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Lazy

/**
 * Created by yfsx on 30/08/21.
 */
class InfiniteRecomViewModel(
        private val userSessionInterface: UserSessionInterface,
        private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
        private val addWishListUseCase: Lazy<AddWishListUseCase>,
        private val removeWishListUseCase: Lazy<RemoveWishListUseCase>,
        private val topAdsWishlishedUseCase: Lazy<TopAdsWishlishedUseCase>,
        private val addToCartUseCase: Lazy<AddToCartUseCase>,
        private val miniCartListSimplifiedUseCase: Lazy<GetMiniCartListSimplifiedUseCase>,
        private val updateCartUseCase: Lazy<UpdateCartUseCase>,
        private val deleteCartUseCase: Lazy<DeleteCartUseCase>,
        private val dispatcher: RecommendationDispatcher
) : BaseViewModel(dispatcher.getMainDispatcher()) {

    val recommendationFirstLiveData: LiveData<List<RecommendationItemDataModel>> get() = _recommendationFirstLiveData
    private val _recommendationFirstLiveData = MutableLiveData<List<RecommendationItemDataModel>>()

    val miniCartData: LiveData<MutableMap<String, MiniCartItem>> get() = _miniCartData
    private val _miniCartData = MutableLiveData<MutableMap<String, MiniCartItem>>()


    fun getRecommendationFirstPage(pageName: String, productId: String, context: Context) {
        val localAddress = ChooseAddressUtils.getLocalizingAddressData(context)
        getMiniCart(localAddress?.shop_id ?: "")
        launchCatchError(dispatcher.getIODispatcher(), {
            val params = GetRecommendationRequestParam(
                    pageNumber = 1,
                    productIds = listOf(productId),
                    pageName = pageName,
                    isTokonow = true
            )
            val result = getRecommendationUseCase.get().getData(params)
            if (result.isEmpty()) {

            } else {
                _recommendationFirstLiveData.postValue(mappingMiniCartDataToRecommendation(result))
            }
        }) {

        }
    }

    fun getRecommendationNextPage(pageName: String, productId: String, pageNumber: Int) {
        launchCatchError(dispatcher.getIODispatcher(), {
            val params = GetRecommendationRequestParam(
                    pageNumber = pageNumber,
                    productIds = listOf(productId),
                    pageName = pageName,
                    isTokonow = true
            )
            val result = getRecommendationUseCase.get().getData(params)
            if (result.isEmpty()) {

            } else {
                val dataList = mutableListOf<RecommendationItemDataModel>()
                //need to get minicart data, then append qty to recom data
                result[0].recommendationItemList.forEach {
                    dataList.add(RecommendationItemDataModel(it))
                }
            }
        }) {

        }
    }

    private fun mappingMiniCartDataToRecommendation(recomData: List<RecommendationWidget>): List<RecommendationItemDataModel> {
        val recomItemList = mutableListOf<RecommendationItemDataModel>()
        if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
            val recomWidget = recomData.first().copy()
            if (recomWidget.layoutType == LAYOUTTYPE_HORIZONTAL_ATC) {
                recomWidget.recommendationItemList.forEach { item ->
                    miniCartData.value?.let {
                        if (item.isProductHasParentID()) {
                            var variantTotalItems = 0
                            it.values.forEach { miniCartItem ->
                                if (miniCartItem.productParentId == item.parentID.toString()) {
                                    variantTotalItems += miniCartItem.quantity
                                }
                            }
                            item.updateItemCurrentStock(variantTotalItems)
                        } else {
                            item.updateItemCurrentStock(it[item.productId.toString()]?.quantity
                                    ?: 0)
                        }
                    }
                    recomItemList.add(RecommendationItemDataModel(item))
                }
            }
        }
        return recomItemList
    }

    fun getMiniCart(shopId: String) {
        launchCatchError(dispatcher.getIODispatcher(), block = {
            miniCartListSimplifiedUseCase.get().setParams(listOf(shopId))
            val result = miniCartListSimplifiedUseCase.get().executeOnBackground()
            val data = result.miniCartItems.associateBy({ it.productId }) {
                it
            }
            _miniCartData.postValue(data.toMutableMap())
        }) {
        }
    }

}