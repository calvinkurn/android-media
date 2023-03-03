package com.tokopedia.search.result.shop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterCoroutineUseCaseModule
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.domain.usecase.SearchShopUseCaseModule
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.result.shop.presentation.model.ShopCpmDataView
import com.tokopedia.search.result.shop.presentation.model.ShopDataView
import com.tokopedia.search.utils.ChooseAddressWrapper
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named
import dagger.Lazy as daggerLazy

@Module(includes = [
    SearchShopUseCaseModule::class,
    GetDynamicFilterCoroutineUseCaseModule::class,
    ShopViewModelMapperModule::class
])
internal class SearchShopViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(SearchShopViewModel::class)
    @SearchScope
    fun providesSearchShopViewModel(
        searchParameter: SearchParameter,
        @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_USE_CASE)
        searchShopFirstPageUseCase: daggerLazy<UseCase<SearchShopModel>>,
        @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_USE_CASE)
        searchShopLoadMoreUseCase: daggerLazy<UseCase<SearchShopModel>>,
        @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_SHOP_USE_CASE)
        getDynamicFilterUseCase: daggerLazy<UseCase<DynamicFilterModel>>,
        @Named(SearchConstant.SearchShop.GET_SHOP_COUNT_USE_CASE)
        getShopCountUseCase: daggerLazy<UseCase<Int>>,
        shopCpmDataViewMapper: daggerLazy<Mapper<SearchShopModel, ShopCpmDataView>>,
        shopDataViewMapper: daggerLazy<Mapper<SearchShopModel, ShopDataView>>,
        userSession: daggerLazy<UserSessionInterface>,
        coroutineDispatchers: CoroutineDispatchers,
        chooseAddressWrapper: ChooseAddressWrapper,
    ): ViewModel = SearchShopViewModel(
        coroutineDispatchers,
        searchParameter.getSearchParameterMap(),
        searchShopFirstPageUseCase,
        searchShopLoadMoreUseCase,
        getDynamicFilterUseCase,
        getShopCountUseCase,
        shopCpmDataViewMapper,
        shopDataViewMapper,
        userSession,
        chooseAddressWrapper,
    )
}
