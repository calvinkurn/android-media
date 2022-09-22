package com.tokopedia.autocompletecomponent.universal.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.presentation.widget.errorstate.ErrorStateDataView
import com.tokopedia.autocompletecomponent.util.ChooseAddressWrapper
import com.tokopedia.autocompletecomponent.util.putChooseAddressParams
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.State.Error
import com.tokopedia.discovery.common.State.Loading
import com.tokopedia.discovery.common.State.Success
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

internal class UniversalSearchViewModel(
    baseDispatcher: CoroutineDispatchers,
    private val universalSearchUseCase: UseCase<UniversalSearchModel>,
    private val universalSearchModelMapper: Mapper<UniversalSearchModel, List<Visitable<*>>>,
    searchParameter: Map<String, Any>,
    private val chooseAddressWrapper: ChooseAddressWrapper,
): BaseViewModel(baseDispatcher.io) {

    private val searchParameter = searchParameter.toMutableMap()

    private val universalSearchState = MutableLiveData<State<List<Visitable<*>>>>()
    private val universalSearchVisitableList = mutableListOf<Visitable<*>>()

    fun loadData() {
        loadUniversalSearch()
    }

    private fun loadUniversalSearch() {
        updateUniversalSearchStateToLoading()
        universalSearchUseCase.execute(
            this::processGetUniversalSearchSuccess,
            this::processGetUniversalSearchError,
            createUniversalSearchParam(),
        )
    }

    private fun createUniversalSearchParam(): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putAll(searchParameter)
        requestParams.putChooseAddressParams(chooseAddressWrapper.getChooseAddressData())

        return requestParams
    }

    private fun updateUniversalSearchStateToLoading() {
        universalSearchState.postValue(Loading())
    }

    private fun updateUniversalSearchStateToSuccess() {
        universalSearchState.postValue(Success(universalSearchVisitableList))
    }

    private fun updateUniversalSearchStateToError() {
        universalSearchState.postValue(Error("", universalSearchVisitableList))
    }

    private fun processGetUniversalSearchSuccess(universalSearchModel: UniversalSearchModel) {
        processVisitableListFromModel(universalSearchModel)

        updateUniversalSearchStateToSuccess()
    }

    private fun processVisitableListFromModel(universalSearchModel: UniversalSearchModel) {
        val universalDataView = universalSearchModelMapper.convert(universalSearchModel)

        updateUniversalSearchVisitableList(universalDataView)
    }

    private fun updateUniversalSearchVisitableList(visitableList: List<Visitable<*>>) {
        universalSearchVisitableList.clear()
        universalSearchVisitableList.addAll(visitableList)
    }

    private fun processGetUniversalSearchError(e: Throwable?) {
        e?.printStackTrace()

        updateUniversalSearchVisitableList(listOf(createErrorStateDataView()))

        updateUniversalSearchStateToError()
    }

    private fun createErrorStateDataView(): ErrorStateDataView {
        return ErrorStateDataView()
    }

    fun getUniversalSearchState(): LiveData<State<List<Visitable<*>>>> = universalSearchState

    fun getSearchParameter() = searchParameter.toMap()
}