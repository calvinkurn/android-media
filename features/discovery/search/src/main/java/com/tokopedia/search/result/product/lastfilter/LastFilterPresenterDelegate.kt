package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.search.result.domain.usecase.savelastfilter.SaveLastFilterInput
import com.tokopedia.search.result.product.CategoryIdL2Provider
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import dagger.Lazy
import rx.Subscriber

class LastFilterPresenterDelegate(
    private val requestParamsGenerator: RequestParamsGenerator,
    private val chooseAddressPresenterDelegate: ChooseAddressPresenterDelegate,
    private val saveLastFilterUseCase: Lazy<UseCase<Int>>,
    private val categoryIdL2Provider: CategoryIdL2Provider,
): LastFilterPresenter {
    override fun updateLastFilter(
        searchParameter: Map<String, Any>,
        savedOptionList: List<SavedOption>,
    ) {
        val searchParams = requestParamsGenerator.createInitializeSearchParam(
            searchParameter,
            chooseAddressPresenterDelegate.getChooseAddressParams(),
        )
        val saveLastFilterInput = SaveLastFilterInput(
            lastFilter = savedOptionList,
            mapParameter = searchParams.parameters,
            categoryIdL2 = categoryIdL2Provider.categoryIdL2,
        )

        val requestParams = RequestParams.create()
        requestParams.putObject(SearchConstant.SaveLastFilter.INPUT_PARAMS, saveLastFilterInput)

        saveLastFilterUseCase.get().unsubscribe()
        saveLastFilterUseCase.get().execute(requestParams, emptySubscriber())
    }

    override fun closeLastFilter(searchParameter: Map<String, Any>) {
        updateLastFilter(searchParameter, listOf())
    }

    private fun <T> emptySubscriber() = object : Subscriber<T>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable?) {}

        override fun onNext(t: T?) {}
    }
}