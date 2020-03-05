package com.tokopedia.travelhomepage.homepage.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECENT_SEARCH
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION
import com.tokopedia.travelhomepage.homepage.widget.TravelHomepageProductGridCardWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-19
 */

class GetEmptyModelsUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    fun requestEmptyViewModels(loadFromCloud: Boolean): List<TravelHomepageItemModel> {

        val travelHomepageBannerModel = TravelHomepageBannerModel()
        travelHomepageBannerModel.isLoadFromCloud = loadFromCloud

        val categoryListModel = TravelHomepageCategoryListModel()
        categoryListModel.isLoadFromCloud = loadFromCloud

        val orderListModel = TravelHomepageSectionModel(type = TYPE_ORDER_LIST)
        orderListModel.isLoadFromCloud = loadFromCloud

        val recentSearchModel = TravelHomepageSectionModel(type = TYPE_RECENT_SEARCH)
        recentSearchModel.isLoadFromCloud = loadFromCloud

        val recommendationModel = TravelHomepageSectionModel(type = TYPE_RECOMMENDATION)
        recommendationModel.isLoadFromCloud = loadFromCloud

        val destinationModel = TravelHomepageDestinationModel()
        destinationModel.isLoadFromCloud = loadFromCloud

        return listOf(travelHomepageBannerModel,
                categoryListModel,
                orderListModel,
                recentSearchModel,
                recommendationModel,
                destinationModel)
    }

    suspend fun getTravelLayoutSubhomepage(rawQuery: String, fromCloud: Boolean): Result<List<TravelHomepageItemModel>> {
        if (fromCloud) useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        else useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())

        useCase.clearRequest()

        return try {
            val graphqlRequest = GraphqlRequest(rawQuery, TravelLayoutSubhomepage.Response::class.java)
            useCase.addRequest(graphqlRequest)

            val subhomepageLayouts = useCase.executeOnBackground().getSuccessData<TravelLayoutSubhomepage.Response>().response
            Success(mappingToTravelHomepageItem(subhomepageLayouts.layoutList))
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun mappingToTravelHomepageItem(list: List<TravelLayoutSubhomepage.Data>): List<TravelHomepageItemModel> {
        val travelHomepageItems = mutableListOf<TravelHomepageItemModel>()
        for (item in list) {
            when (item.widgetType) {
                ConstantWidgetType.DYNAMIC_BANNER -> {
                    travelHomepageItems.add(TravelHomepageBannerModel())
                    travelHomepageItems.lastOrNull()?.layoutData = item
                }
                ConstantWidgetType.DUAL_PRODUCT_CARD, ConstantWidgetType.QUAD_PRODUCT_CARD -> {
                    travelHomepageItems.add(TravelHomepageProductCardModel())
                    travelHomepageItems.lastOrNull()?.layoutData = item
                }
                ConstantWidgetType.DYNAMIC_ICON -> {
                    travelHomepageItems.add(TravelHomepageCategoryListModel())
                    travelHomepageItems.lastOrNull()?.layoutData = item
                }
                ConstantWidgetType.SLIDER_PRODUCT_CARD -> {
                    travelHomepageItems.add(TravelHomepageSectionModel())
                    travelHomepageItems.lastOrNull()?.layoutData = item
                }
                ConstantWidgetType.DUAL_BANNER -> {
                    travelHomepageItems.add(TravelHomepageDestinationModel(spanSize = 2))
                    travelHomepageItems.lastOrNull()?.layoutData = item
                }
                ConstantWidgetType.SINGLE_BANNER -> {
                    travelHomepageItems.add(TravelHomepageDestinationModel(spanSize = 1))
                    travelHomepageItems.lastOrNull()?.layoutData = item
                }
                else -> {
                    travelHomepageItems.add(TravelHomepageCategoryListModel())
                    travelHomepageItems.lastOrNull()?.layoutData = item
                }
            }
        }
        return travelHomepageItems
    }
}