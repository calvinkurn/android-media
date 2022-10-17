package com.tokopedia.dilayanitokopedia.home.domain.mapper

import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.FEATURED_SHOP
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.MIX_LEFT_CAROUSEL
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.PRODUCT_RECOM
import com.tokopedia.dilayanitokopedia.common.model.DtChooseAddressWidgetUiModel
import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.dilayanitokopedia.home.domain.mapper.FeaturedShopMapper.mapToFeaturedShop
import com.tokopedia.dilayanitokopedia.home.domain.mapper.LeftCarouselMapper.mapToLeftCarousel
import com.tokopedia.dilayanitokopedia.home.domain.mapper.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.dilayanitokopedia.home.domain.mapper.ProductRecomMapper.mapProductRecomDataModel
import com.tokopedia.dilayanitokopedia.home.domain.mapper.SliderBannerMapper.mapSliderBannerModel
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import timber.log.Timber

object HomeLayoutMapper {


//    fun MutableList<HomeLayoutItemUiModel>.addLoadingIntoList() {
//        val loadingLayout = HomeLoadingStateUiModel(id = LOADING_STATE)
//        add(HomeLayoutItemUiModel(loadingLayout, HomeLayoutItemState.LOADED))
//    }

    private val SUPPORTED_LAYOUT_TYPES = listOf(
//        CATEGORY,
//        LEGO_3_IMAGE,
        LEGO_6_IMAGE,
        BANNER_CAROUSEL,
        PRODUCT_RECOM,
//        REPURCHASE_PRODUCT,
//        EDUCATIONAL_INFORMATION,
//        SHARING_EDUCATION,
//        SHARING_REFERRAL,
//        MAIN_QUEST,
        MIX_LEFT_CAROUSEL,
//        MIX_LEFT_CAROUSEL_ATC,
//        MEDIUM_PLAY_WIDGET,
//        SMALL_PLAY_WIDGET,
        FEATURED_SHOP
    )

    fun MutableList<HomeLayoutItemUiModel>.addEmptyStateIntoList(
        @HomeStaticLayoutId id: String,
        serviceType: String
    ) {
        val chooseAddressUiModel = DtChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))
//        when (id) {
//            EMPTY_STATE_OUT_OF_COVERAGE -> {
//                val layout = TokoNowEmptyStateOocUiModel(id, SOURCE, serviceType)
//                add(HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED))
//            }
//            EMPTY_STATE_FAILED_TO_FETCH_DATA -> {
//                add(HomeLayoutItemUiModel(TokoNowServerErrorUiModel, HomeLayoutItemState.LOADED))
//            }
//            else -> {
//                add(HomeLayoutItemUiModel(HomeEmptyStateUiModel(id), HomeLayoutItemState.LOADED))
//            }
//        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapHomeLayoutList(
        response: List<HomeLayoutResponse>,
//        hasTickerBeenRemoved: Boolean,
//        removeAbleWidgets: List<HomeRemoveAbleWidget>,
//        miniCartData: MiniCartSimplifiedData?,
        localCacheModel: LocalCacheModel,
//        isLoggedIn: Boolean
    ) {
        val chooseAddressUiModel = DtChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))

//        if (!hasTickerBeenRemoved) {
//            val ticker = HomeTickerUiModel(id = TICKER_WIDGET_ID, tickers = emptyList())
//            add(HomeLayoutItemUiModel(ticker, HomeLayoutItemState.NOT_LOADED))
//        }

        Timber.d("responseeew! $response")
        response.forEach { layoutResponse ->
            Timber.d("responseeew!2212 ${layoutResponse.layout}")

        }

        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach { layoutResponse ->
            Timber.d("responseeew!222 ${layoutResponse.layout}")

//            if (removeAbleWidgets.none { layoutResponse.layout == it.type && it.isRemoved }) {
//
            mapToHomeUiModel(
                layoutResponse,
//                    miniCartData,
                localCacheModel
            )?.let { item ->
                add(item)
            }
//
//                addSwitcherUiModel(layoutResponse, localCacheModel, isLoggedIn)
//            }
        }


    }

    /**
     * Map dynamic channel layout response to ui model.
     *
     * @param response layout response from dynamic channel query.
     * @param miniCartData mini cart data to set ATC quantity for each products.
     * @param localCacheModel address data cache from choose address widget.
     *
     * @see HomeLayoutItemState.LOADED
     * @see HomeLayoutItemState.NOT_LOADED
     */
    private fun mapToHomeUiModel(
        response: HomeLayoutResponse,
//        miniCartData: MiniCartSimplifiedData? = null,
        localCacheModel: LocalCacheModel
    ): HomeLayoutItemUiModel? {
        val serviceType = localCacheModel.service_type
        val warehouseId = localCacheModel.warehouse_id
        val loadedState = HomeLayoutItemState.LOADED
        val notLoadedState = HomeLayoutItemState.NOT_LOADED

        return when (response.layout) {
            // region Dynamic Channel Component
            // Layout content data already returned from dynamic channel query, set state to loaded.
//                LEGO_3_IMAGE,
            LEGO_6_IMAGE -> mapLegoBannerDataModel(response, loadedState)

            //TODO - need to recheck what wrong at banner carousel
//            BANNER_CAROUSEL -> mapSliderBannerModel(response, loadedState)

                PRODUCT_RECOM -> mapProductRecomDataModel(response, loadedState)
//                EDUCATIONAL_INFORMATION  -> mapEducationalInformationUiModel(response, loadedState, serviceType)
//                MIX_LEFT_CAROUSEL_ATC -> mapToLeftCarouselAtc(response, loadedState, miniCartData)
//                // endregion
//
//                // region TokoNow Component
//                // Layout need to fetch content data from other GQL, set state to not loaded.
//                CATEGORY -> mapToCategoryLayout(response, notLoadedState)
//                REPURCHASE_PRODUCT -> mapRepurchaseUiModel(response, notLoadedState)
//                MAIN_QUEST -> mapQuestUiModel(response, notLoadedState)
//                SHARING_EDUCATION -> mapSharingEducationUiModel(response, notLoadedState, serviceType)
//                SHARING_REFERRAL -> mapSharingReferralUiModel(response, notLoadedState, warehouseId)
            MIX_LEFT_CAROUSEL -> mapToLeftCarousel(response, loadedState)
            FEATURED_SHOP -> mapToFeaturedShop(response, loadedState)

//                MEDIUM_PLAY_WIDGET -> mapToMediumPlayWidget(response, notLoadedState)
//                SMALL_PLAY_WIDGET -> mapToSmallPlayWidget(response, notLoadedState)
            // endregion
            else -> null
        }
    }
}
