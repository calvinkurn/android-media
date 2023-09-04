package com.tokopedia.search.result.product.filter.bottomsheetfilter

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.GET_PRODUCT_COUNT_USE_CASE
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.filter.dynamicfilter.MutableDynamicFilterModelProvider
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.utils.createSearchProductDefaultFilter
import com.tokopedia.usecase.UseCase
import dagger.Lazy
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

@SearchScope
class BottomSheetFilterPresenterDelegate @Inject constructor(
    private val view: BottomSheetFilterView,
    private val queryKeyProvider: QueryKeyProvider,
    private val requestParamsGenerator: RequestParamsGenerator,
    private val chooseAddressDelegate: ChooseAddressPresenterDelegate,
    @param:Named(GET_PRODUCT_COUNT_USE_CASE)
    private val getProductCountUseCase: Lazy<UseCase<String>>,
    @param:Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    private val getDynamicFilterUseCase: Lazy<UseCase<DynamicFilterModel>>,
    private val mutableDynamicFilterModelProvider: MutableDynamicFilterModelProvider,
) : BottomSheetFilterPresenter,
    MutableDynamicFilterModelProvider by mutableDynamicFilterModelProvider {
    override var isBottomSheetFilterEnabled: Boolean = true
        private set

    override fun getProductCount(mapParameter: Map<String, String>?) {
        if (mapParameter == null) {
            view.setProductCount("0")
            return
        }

        val getProductCountRequestParams = requestParamsGenerator.createGetProductCountRequestParams(
            mapParameter,
            chooseAddressDelegate.getChooseAddressParams(),
        )
        val getProductCountSubscriber = createGetProductCountSubscriber()
        getProductCountUseCase.get().execute(
            getProductCountRequestParams,
            getProductCountSubscriber
        )
    }

    private fun createGetProductCountSubscriber(): Subscriber<String> {
        return object : Subscriber<String>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                setProductCount("0")
            }

            override fun onNext(productCountText: String) {
                setProductCount(productCountText)
            }
        }
    }

    private fun setProductCount(productCountText: String) {
        view.setProductCount(productCountText)
    }

    override fun openFilterPage(searchParameter: Map<String, Any>?) {
        if (searchParameter == null || !isBottomSheetFilterEnabled) return

        isBottomSheetFilterEnabled = false
        view.sendTrackingOpenFilterPage()
        view.openBottomSheetFilter(dynamicFilterModel, this)

        getDynamicFilter(searchParameter)
    }

    override fun openSortPage(searchParameter: Map<String, Any>?) {
        if (searchParameter == null || !isBottomSheetFilterEnabled) return

        isBottomSheetFilterEnabled = false
        view.openBottomSheetSort(dynamicFilterModel, this)

        getDynamicFilter(searchParameter)
    }

    private fun getDynamicFilter(searchParameter: Map<String, Any>) {
        if (dynamicFilterModel == null) {
            val getDynamicFilterRequestParams =
                requestParamsGenerator.createRequestDynamicFilterParams(
                    searchParameter,
                    chooseAddressDelegate.getChooseAddressParams(),
                )
            getDynamicFilterUseCase.get().execute(
                getDynamicFilterRequestParams,
                createGetDynamicFilterModelSubscriber()
            )
        }
    }

    private fun createGetDynamicFilterModelSubscriber(): Subscriber<DynamicFilterModel> {
        return object : Subscriber<DynamicFilterModel>() {
            override fun onCompleted() {}

            override fun onNext(dynamicFilterModel: DynamicFilterModel) {
                handleGetDynamicFilterSuccess(dynamicFilterModel)
            }

            override fun onError(e: Throwable) {
                handleGetDynamicFilterFailed()
            }
        }
    }

    private fun handleGetDynamicFilterSuccess(dynamicFilterModel: DynamicFilterModel) {
        if (!dynamicFilterModel.isEmpty()) {
            this.dynamicFilterModel = dynamicFilterModel
            getViewToSetDynamicFilterModel(dynamicFilterModel)
        } else {
            handleGetDynamicFilterFailed()
        }
    }

    private fun getViewToSetDynamicFilterModel(dynamicFilterModel: DynamicFilterModel) {
        view.setDynamicFilter(dynamicFilterModel)
    }

    private fun handleGetDynamicFilterFailed() {
        getViewToSetDynamicFilterModel(createSearchProductDefaultFilter())
    }

    override fun onBottomSheetFilterDismissed() {
        isBottomSheetFilterEnabled = true
    }

    override fun onApplySortFilter(mapParameter: Map<String, Any>) {
        val keywordFromFilter = mapParameter[SearchApiConst.Q] ?: ""
        val currentKeyword = queryKeyProvider.queryKey

        if (currentKeyword != keywordFromFilter)
            dynamicFilterModel = null
    }

    override fun clearDynamicFilter() {
        dynamicFilterModel = null
    }
}
