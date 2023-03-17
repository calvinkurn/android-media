package com.tokopedia.dilayanitokopedia.domain.mapper.widgets

import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.FEATURED_SHOP
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.LOADING_RECOMMENDATION_FEED
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.MIX_LEFT_CAROUSEL
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType.Companion.MIX_TOP_CAROUSEL
import com.tokopedia.dilayanitokopedia.ui.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.ui.home.constant.HomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.FeaturedShopMapper.mapToFeaturedShop
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.LeftCarouselMapper.mapToLeftCarousel
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.SliderBannerMapper.mapSliderBannerModel
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.TopCarouselMapper.mapTopCarouselModel
import com.tokopedia.dilayanitokopedia.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.ui.home.presentation.uimodel.HomeLoadingStateUiModel
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLayoutItemUiModel

object HomeLayoutMapper {

    private val SUPPORTED_LAYOUT_TYPES = listOf(

        LEGO_6_IMAGE,
        BANNER_CAROUSEL,
        MIX_TOP_CAROUSEL,
        MIX_LEFT_CAROUSEL,

        // Additional Loading
        LOADING_RECOMMENDATION_FEED
    )

    fun MutableList<HomeLayoutItemUiModel>.mapHomeLayoutList(
        response: List<HomeLayoutResponse>
    ) {
        clear()
        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach { layoutResponse ->
            mapToHomeUiModel(layoutResponse)?.let { item ->
                add(item)
            }
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
        response: HomeLayoutResponse
    ): HomeLayoutItemUiModel? {
        val loadedState = com.tokopedia.dilayanitokopedia.ui.home.constant.HomeLayoutItemState.LOADED
        val notLoadedState = com.tokopedia.dilayanitokopedia.ui.home.constant.HomeLayoutItemState.NOT_LOADED

        return when (response.layout) {
            // Layout content data already returned from dynamic channel query, set state to loaded.
            LEGO_6_IMAGE -> mapLegoBannerDataModel(response, loadedState)
            BANNER_CAROUSEL -> mapSliderBannerModel(response, loadedState)
            MIX_TOP_CAROUSEL -> mapTopCarouselModel(response, loadedState)
            MIX_LEFT_CAROUSEL -> mapToLeftCarousel(response, loadedState)

            // Layout need to fetch content data from other GQL, set state to not loaded.
            FEATURED_SHOP -> mapToFeaturedShop(response, notLoadedState)

            // endregion
            else -> null
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.addLoadingIntoList() {
        val loadingLayout = HomeLoadingStateUiModel(id = LOADING_STATE)
        add(
            HomeLayoutItemUiModel(
                loadingLayout,
                com.tokopedia.dilayanitokopedia.ui.home.constant.HomeLayoutItemState.LOADED,
                null
            )
        )
    }
}
