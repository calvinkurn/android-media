package com.tokopedia.officialstore.official.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBannerUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreDynamicChannelUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreFeaturedUseCase
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class OfficialStoreHomeViewModel @Inject constructor(
        private val getOfficialStoreBannersUseCase: GetOfficialStoreBannerUseCase,
        private val getOfficialStoreFeaturedShopUseCase: GetOfficialStoreFeaturedUseCase,
        private val getOfficialStoreDynamicChannelUseCase: GetOfficialStoreDynamicChannelUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val userSessionInterface: UserSessionInterface,
        dispatchers: CoroutineDispatcher
) : BaseViewModel(dispatchers) {

    val userId: String
        get() = userSessionInterface.userId

    val officialStoreBannersResult: LiveData<Result<OfficialStoreBanners>> by lazy {
        _officialStoreBannersResult
    }

    val officialStoreFeaturedShopResult: LiveData<Result<OfficialStoreFeaturedShop>> by lazy {
        _officialStoreFeaturedShopResult
    }

    val officialStoreDynamicChannelResult: LiveData<Result<DynamicChannel>> by lazy {
        _officialStoreDynamicChannelResult
    }

    private val _officialStoreBannersResult by lazy {
        MutableLiveData<Result<OfficialStoreBanners>>()
    }

    private val _officialStoreFeaturedShopResult by lazy {
        MutableLiveData<Result<OfficialStoreFeaturedShop>>()
    }

    private val _officialStoreDynamicChannelResult by lazy {
        MutableLiveData<Result<DynamicChannel>>()
    }

    private val _officialStoreProductRecommendation by lazy {
        MutableLiveData<Result<RecommendationWidget>>()
    }

    fun loadFirstData(category: Category?) {
        launchCatchError(block = {
//            _officialStoreBannersResult.value = Success(getOfficialStoreBanners(category?.slug?: "").await())
            _officialStoreBannersResult.value = Success(getOfficialStoreBanners("test").await()) // for testing only
            _officialStoreFeaturedShopResult.value = Success(getOfficialStoreFeaturedShop(category?.categoryId?: "").await())
            _officialStoreDynamicChannelResult.value = Success(getOfficialStoreDynamicChannel("os-handphone").await())
            _officialStoreProductRecommendation.value = Success(getOfficialStoreProductRecommendation(category?.categoryId?: "").await())
            // TODO get product recommendation

        }) {
            // TODO just ignore or handle?
        }
    }

    fun loadMore() {
        // TODO get dynamic channel & product recommendation
    }

    private fun getOfficialStoreBanners(categoryId: String): Deferred<OfficialStoreBanners> {
        return async(Dispatchers.IO) {
            var banner = OfficialStoreBanners()
            try {
                getOfficialStoreBannersUseCase.params = GetOfficialStoreBannerUseCase.
                        createParams(categoryId)
                banner = getOfficialStoreBannersUseCase.executeOnBackground()
            } catch (t:  Throwable) {
                _officialStoreFeaturedShopResult.value = Fail(t)
            }
            banner
        }
    }


    private fun getOfficialStoreFeaturedShop(categoryId: String): Deferred<OfficialStoreFeaturedShop>  {
       return async(Dispatchers.IO) {
           var featuredShop = OfficialStoreFeaturedShop()
           try {
               getOfficialStoreFeaturedShopUseCase.params = GetOfficialStoreFeaturedUseCase.
                       createParams(categoryId.toIntOrNull() ?: 0)
               featuredShop = getOfficialStoreFeaturedShopUseCase.executeOnBackground()
           } catch (t: Throwable) {
               _officialStoreFeaturedShopResult.value = Fail(t)
           }

           featuredShop
        }
    }

    private fun getOfficialStoreDynamicChannel(channelType: String): Deferred<DynamicChannel> {
        return async(Dispatchers.IO) {
            var dynamicChannel = DynamicChannel()

            try {
                getOfficialStoreDynamicChannelUseCase.params = GetOfficialStoreDynamicChannelUseCase
                        .setupParams(channelType)
                dynamicChannel = getOfficialStoreDynamicChannelUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _officialStoreDynamicChannelResult.value = Fail(t)
            }

            dynamicChannel
        }
    }

    private fun getOfficialStoreProductRecommendation(categoryId: String): Deferred<RecommendationWidget> {
        return async(Dispatchers.IO) {
            var productRecommendation = RecommendationWidget(
                    emptyList(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    1,
                    0,
                    0,
                    true,
                    "official-store"
            )

            try {
                val pageNumber = 1
                val pageName = "official-store"
                val dataProduct = getRecommendationUseCase.createObservable(
                        getRecommendationUseCase.getOfficialStoreRecomParams(pageNumber, pageName, categoryId))
                        .toBlocking()
                dataProduct.first().get(0)?.let {
                    productRecommendation = it
                }
            } catch (t: Throwable) {
                _officialStoreProductRecommendation.value = Fail(t)
            }

            productRecommendation
        }
    }

}