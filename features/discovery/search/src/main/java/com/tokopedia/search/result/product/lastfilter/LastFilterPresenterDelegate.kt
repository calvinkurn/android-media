package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.search.result.domain.usecase.savelastfilter.SaveLastFilterInput
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import dagger.Lazy
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.INPUT_PARAMS

class LastFilterPresenterDelegate @Inject constructor(
    private val requestParamsGenerator: RequestParamsGenerator,
    private val chooseAddressPresenterDelegate: ChooseAddressPresenterDelegate,
    @param:Named(SearchConstant.SaveLastFilter.SAVE_LAST_FILTER_USE_CASE)
    private val saveLastFilterUseCase: Lazy<UseCase<Int>>,
): LastFilterPresenter {
    var categoryIdL2: String = ""

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
            categoryIdL2 = categoryIdL2,
        )

        val requestParams = RequestParams.create()
        requestParams.putObject(INPUT_PARAMS, saveLastFilterInput)

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