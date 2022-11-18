package com.tokopedia.search.result.product.filter.bottomsheetfilter

import android.content.Context
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.ScreenNameProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.filter.dynamicfilter.DynamicFilterModelProviderModule
import com.tokopedia.search.result.product.filter.dynamicfilter.MutableDynamicFilterModelProvider
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.lastfilter.LastFilterListener
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [
    DynamicFilterModelProviderModule::class,
])
object BottomSheetFilterModule {
    @JvmStatic
    @Provides
    @SearchScope
    fun provideBottomSheetFilterViewDelegate(
        @SearchContext
        context: Context,
        fragmentProvider: FragmentProvider,
        searchParameterProvider: SearchParameterProvider,
        filterController: FilterController,
        parameterListener: ProductListParameterListener,
        lastFilterListener: LastFilterListener,
        screenNameProvider: ScreenNameProvider,
        userSessionInterface: UserSessionInterface,
    ) : BottomSheetFilterViewDelegate {
        return BottomSheetFilterViewDelegate(
            context,
            fragmentProvider,
            searchParameterProvider,
            filterController,
            parameterListener,
            lastFilterListener,
            screenNameProvider,
            userSessionInterface,
        )
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideBottomSheetFilterView (
        bottomSheetFilterViewDelegate: BottomSheetFilterViewDelegate
    ) : BottomSheetFilterView {
        return bottomSheetFilterViewDelegate
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideBottomSheetFilterPresenter(
        view: BottomSheetFilterView,
        queryKeyProvider: QueryKeyProvider,
        requestParamsGenerator: RequestParamsGenerator,
        chooseAddressDelegate: ChooseAddressPresenterDelegate,
        @Named(SearchConstant.SearchProduct.GET_PRODUCT_COUNT_USE_CASE)
        getProductCountUseCase: Lazy<UseCase<String>>,
        @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
        getDynamicFilterUseCase: Lazy<UseCase<DynamicFilterModel>>,
        mutableDynamicFilterModelProvider: MutableDynamicFilterModelProvider,
    ) : BottomSheetFilterPresenter {
        return BottomSheetFilterPresenterDelegate(
            view,
            queryKeyProvider,
            requestParamsGenerator,
            chooseAddressDelegate,
            getProductCountUseCase,
            getDynamicFilterUseCase,
            mutableDynamicFilterModelProvider,
        )
    }
}
