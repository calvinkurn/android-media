package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.home.analytics.v2.BestSellerWidgetTracker
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home.constant.ConstantKey.CATEGORY_ID
import com.tokopedia.home_component.listener.BestSellerListener
import com.tokopedia.home_component.visitable.BestSellerChipDataModel
import com.tokopedia.home_component.visitable.BestSellerChipProductDataModel
import com.tokopedia.home_component.visitable.BestSellerDataModel
import com.tokopedia.home_component.visitable.BestSellerProductDataModel
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class BestSellerWidgetCallback(
    private val context: Context?,
    private val homeCategoryListener: HomeCategoryListener,
    private val homeRevampViewModel: HomeRevampViewModel,
): BestSellerListener {

    override fun onBestSellerClick(
        bestSellerDataModel: BestSellerDataModel,
        bestSellerProductDataModel: BestSellerProductDataModel,
        widgetPosition: Int
    ) {
        BestSellerWidgetTracker.sendClickTracker(
            bestSellerProductDataModel,
            bestSellerDataModel,
            homeCategoryListener.userId,
            widgetPosition,
        )
        RouteManager.route(context, bestSellerProductDataModel.applink)
    }

    override fun onBestSellerImpress(
        bestSellerDataModel: BestSellerDataModel,
        bestSellerProductDataModel: BestSellerProductDataModel,
        widgetPosition: Int
    ) {
        val impressionMap = BestSellerWidgetTracker.getImpressionTracker(
            bestSellerProductDataModel,
            bestSellerDataModel,
            homeCategoryListener.userId,
            widgetPosition
        ) as HashMap<String, Any>

        homeCategoryListener
            .getTrackingQueueObj()
            ?.putEETracking(impressionMap)
    }

    override fun onBestSellerFilterImpress(
        bestSellerChipProductDataModel: BestSellerChipDataModel,
        bestSellerDataModel: BestSellerDataModel,
    ) {
        val filterValue = UrlParamUtils.getParamMap(bestSellerChipProductDataModel.value)
        val categoryId = filterValue[CATEGORY_ID].toString()

        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
            BestSellerWidgetTracker.getFilterImpressionTracker(
                categoryId = categoryId,
                channelId = bestSellerDataModel.id,
                headerName = bestSellerDataModel.title,
                userId = homeCategoryListener.userId,
                position = bestSellerChipProductDataModel.position,
                ncpRank = bestSellerChipProductDataModel.ncpRank,
                totalFilterCount = bestSellerDataModel.chipCount,
                chipsValue = bestSellerChipProductDataModel.title,
            ) as HashMap<String, Any>
        )
    }

    override fun onBestSellerFilterScrolled(
        selectedChipProduct: BestSellerChipProductDataModel,
        bestSellerDataModel: BestSellerDataModel,
        widgetPosition: Int,
        chipsPosition: Int,
        scrollDirection: CarouselPagingGroupChangeDirection,
    ) {
        homeRevampViewModel.getRecommendationWidget(
            selectedChipProduct,
            bestSellerDataModel,
            scrollDirection
        )
    }

    override fun onBestSellerFilterClick(
        selectedChipProduct: BestSellerChipProductDataModel,
        bestSellerDataModel: BestSellerDataModel,
    ) {
        val filterValue = UrlParamUtils.getParamMap(selectedChipProduct.value)
        val categoryId = filterValue[CATEGORY_ID].toString()

        BestSellerWidgetTracker.sendFilterClickTracker(
            categoryId = categoryId,
            channelId = bestSellerDataModel.id,
            headerName = bestSellerDataModel.title,
            position = selectedChipProduct.chipPosition,
            ncpRank = selectedChipProduct.ncpRank,
            chipsValue = selectedChipProduct.title,
        )
        homeRevampViewModel.getRecommendationWidget(selectedChipProduct, bestSellerDataModel)
    }

    override fun onBestSellerSeeMoreTextClick(
        bestSellerDataModel: BestSellerDataModel,
        appLink: String,
    ) {
        BestSellerWidgetTracker.sendViewAllClickTracker(
            bestSellerDataModel.id,
            bestSellerDataModel.title,
            homeCategoryListener.userId,
        )
        RouteManager.route(context, appLink)
    }
}
