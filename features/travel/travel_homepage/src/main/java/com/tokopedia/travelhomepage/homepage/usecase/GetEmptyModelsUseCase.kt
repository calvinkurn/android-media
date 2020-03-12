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
        for ((position, item) in list.withIndex()) {
            item.position = position
            when (item.widgetType) {
                ConstantWidgetType.SLIDER_BANNER -> {
                    val model = TravelHomepageBannerModel()
                    model.layoutData = item
                    travelHomepageItems.add(model)
                }
                ConstantWidgetType.DUO_PRODUCT_CARD, ConstantWidgetType.QUAD_PRODUCT_CARD -> {
                    val model = TravelHomepageProductCardModel()
                    model.layoutData = item
                    travelHomepageItems.add(model)
                }
                ConstantWidgetType.DYNAMIC_ICON -> {
                    val model = TravelHomepageCategoryListModel()
                    model.layoutData = item
                    travelHomepageItems.add(model)
                }
                ConstantWidgetType.SLIDER_PRODUCT_CARD -> {
                    val model = TravelHomepageSectionModel()
                    model.layoutData = item
                    travelHomepageItems.add(model)
                }
                ConstantWidgetType.DUAL_BANNER -> {
                    val model = TravelHomepageDestinationModel(spanSize = 2)
                    model.layoutData = item
                    travelHomepageItems.add(model)
                }
                ConstantWidgetType.SINGLE_BANNER -> {
                    val model = TravelHomepageDestinationModel(spanSize = 1)
                    model.layoutData = item
                    travelHomepageItems.add(model)
                }
                ConstantWidgetType.DYNAMIC_BANNER -> {
                    val model = TravelHomepageDestinationModel(spanSize = 0)
                    model.layoutData = item
                    travelHomepageItems.add(model)
                }
                ConstantWidgetType.LEGO_BANNER -> {
                    val model = TravelHomepageLegoBannerModel()
                    model.layoutData = item
                    travelHomepageItems.add(model)
                }
                else -> {
                    val model = TravelHomepageSectionModel()
                    model.layoutData = item
                    travelHomepageItems.add(model)
                }
            }
        }
        return travelHomepageItems
    }
}