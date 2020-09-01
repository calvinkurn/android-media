package com.tokopedia.search.result.data.mapper.searchproduct

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.domain.model.*
import com.tokopedia.search.result.domain.model.SearchProductModel.*
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import rx.functions.Func1

internal class SearchProductMapper : Func1<GraphqlResponse?, SearchProductModel?> {

    override fun call(graphqlResponse: GraphqlResponse?): SearchProductModel? {
        graphqlResponse ?: return SearchProductModel()

        return SearchProductModel(
                searchProduct = graphqlResponse.getSearchProduct(),
                quickFilterModel = graphqlResponse.getQuickFilter(),
                topAdsModel = graphqlResponse.getTopAdsModel(),
                cpmModel = graphqlResponse.getCPMModel(),
                globalSearchNavigation = graphqlResponse.getGlobalSearchNavigation(),
                searchInspirationCarousel = graphqlResponse.getSearchInspirationCarousel(),
                searchInspirationWidget = graphqlResponse.getSearchInspirationWidget()
        )
    }

    private fun GraphqlResponse.getSearchProduct(): SearchProduct {
        return getData<AceSearchProductModel>(AceSearchProductModel::class.java)?.searchProduct
                ?: SearchProduct()
    }

    private fun GraphqlResponse.getQuickFilter(): DataValue {
        return getData<QuickFilterModel>(QuickFilterModel::class.java)?.quickFilterModel
                ?: DataValue()
    }

    private fun GraphqlResponse.getTopAdsModel(): TopAdsModel {
        return getData<ProductTopAdsModel>(ProductTopAdsModel::class.java)?.topAdsModel
                ?: TopAdsModel()
    }

    private fun GraphqlResponse.getCPMModel(): CpmModel {
        return getData<HeadlineAdsModel>(HeadlineAdsModel::class.java)?.cpmModel
                ?: CpmModel()
    }

    private fun GraphqlResponse.getGlobalSearchNavigation(): GlobalSearchNavigation {
        return getData<GlobalSearchNavigationModel>(GlobalSearchNavigationModel::class.java)?.globalSearchNavigation
                ?: GlobalSearchNavigation()
    }

    private fun GraphqlResponse.getSearchInspirationCarousel(): SearchInspirationCarousel {
        return getData<SearchInspirationCarouselModel>(SearchInspirationCarouselModel::class.java)?.searchInspirationCarousel
                ?: SearchInspirationCarousel()
    }

    private fun GraphqlResponse.getSearchInspirationWidget(): SearchInspirationWidget {
        return getData<SearchInspirationWidgetModel>(SearchInspirationWidgetModel::class.java)?.searchInspirationWidget
                ?: SearchInspirationWidget()
    }
}