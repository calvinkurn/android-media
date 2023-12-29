package com.tokopedia.search.result.data.mapper.searchproduct

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.domain.model.AceSearchProductModel
import com.tokopedia.search.result.domain.model.AceSearchProductModelV5
import com.tokopedia.search.result.domain.model.GlobalSearchNavigationModel
import com.tokopedia.search.result.domain.model.HeadlineAdsModel
import com.tokopedia.search.result.domain.model.LastFilterModel
import com.tokopedia.search.result.domain.model.LastFilterModel.LastFilter
import com.tokopedia.search.result.domain.model.ProductTopAdsModel
import com.tokopedia.search.result.domain.model.QuickFilterModel
import com.tokopedia.search.result.domain.model.SearchInspirationCarouselModel
import com.tokopedia.search.result.domain.model.SearchInspirationWidgetModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.GlobalSearchNavigation
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationWidget
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchProduct
import com.tokopedia.search.result.domain.model.SearchProductV5
import com.tokopedia.search.result.domain.model.UserDOB
import com.tokopedia.search.result.domain.model.UserProfileDobModel
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import rx.functions.Func1

internal class SearchProductMapper : Func1<GraphqlResponse?, SearchProductModel?> {

    override fun call(graphqlResponse: GraphqlResponse?): SearchProductModel {
        graphqlResponse ?: return SearchProductModel()

        return SearchProductModel(
            searchProduct = graphqlResponse.getSearchProduct(),
            searchProductV5 = graphqlResponse.getSearchProductV5(),
            quickFilterModel = graphqlResponse.getQuickFilter(),
            topAdsModel = graphqlResponse.getTopAdsModel(),
            cpmModel = graphqlResponse.getCPMModel(),
            globalSearchNavigation = graphqlResponse.getGlobalSearchNavigation(),
            searchInspirationCarousel = graphqlResponse.getSearchInspirationCarousel(),
            searchInspirationWidget = graphqlResponse.getSearchInspirationWidget(),
            lastFilter = graphqlResponse.getLastFilter(),
            userDOB = graphqlResponse.getUserProfile(),
        )
    }

    private fun GraphqlResponse.getSearchProduct(): SearchProduct =
        getData<AceSearchProductModel>(AceSearchProductModel::class.java)
            ?.searchProduct
            ?: SearchProduct()

    private fun GraphqlResponse.getSearchProductV5(): SearchProductV5 =
        getData<AceSearchProductModelV5>(AceSearchProductModelV5::class.java)
            ?.searchProduct
            ?: SearchProductV5()

    private fun GraphqlResponse.getQuickFilter(): DataValue =
        getData<QuickFilterModel>(QuickFilterModel::class.java)
            ?.quickFilterModel
            ?: DataValue()

    private fun GraphqlResponse.getTopAdsModel(): TopAdsModel =
        getData<ProductTopAdsModel>(ProductTopAdsModel::class.java)
            ?.topAdsModel
            ?: TopAdsModel()

    private fun GraphqlResponse.getCPMModel(): CpmModel =
        getData<HeadlineAdsModel>(HeadlineAdsModel::class.java)
            ?.cpmModel
            ?: CpmModel()

    private fun GraphqlResponse.getGlobalSearchNavigation(): GlobalSearchNavigation =
        getData<GlobalSearchNavigationModel>(GlobalSearchNavigationModel::class.java)
            ?.globalSearchNavigation
            ?: GlobalSearchNavigation()

    private fun GraphqlResponse.getSearchInspirationCarousel(): SearchInspirationCarousel =
        getData<SearchInspirationCarouselModel>(SearchInspirationCarouselModel::class.java)
            ?.searchInspirationCarousel
            ?: SearchInspirationCarousel()

    private fun GraphqlResponse.getSearchInspirationWidget(): SearchInspirationWidget =
        getData<SearchInspirationWidgetModel>(SearchInspirationWidgetModel::class.java)
            ?.searchInspirationWidget
            ?: SearchInspirationWidget()

    private fun GraphqlResponse.getLastFilter(): LastFilter =
        getData<LastFilterModel>(LastFilterModel::class.java)
            ?.lastFilter
            ?: LastFilter()

    private fun GraphqlResponse.getUserProfile(): UserDOB =
        getData<UserProfileDobModel>(UserProfileDobModel::class.java)
            ?.userDOB
            ?: UserDOB()
}
