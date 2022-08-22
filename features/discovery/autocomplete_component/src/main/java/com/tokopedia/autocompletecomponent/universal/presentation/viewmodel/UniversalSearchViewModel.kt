package com.tokopedia.autocompletecomponent.universal.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.autocompletecomponent.universal.UniversalConstant
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.presentation.mapper.UniversalSearchModelMapper
import com.tokopedia.autocompletecomponent.universal.presentation.model.UniversalDataView
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.State.Error
import com.tokopedia.discovery.common.State.Loading
import com.tokopedia.discovery.common.State.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

internal class UniversalSearchViewModel @Inject constructor(
    baseDispatcher: CoroutineDispatchers,
    @param:Named(UniversalConstant.UNIVERSAL_SEARCH_USE_CASE)
    private val universalSearchUseCase: UseCase<UniversalSearchModel>,
    private val universalSearchModelMapper: Mapper<UniversalSearchModel, UniversalDataView>,
): BaseViewModel(baseDispatcher.io) {

    private val universalSearchState = MutableLiveData<State<List<Visitable<*>>>>()
    private val universalSearchVisitableList = mutableListOf<Visitable<*>>()

    fun onViewCreated() {
        loadUniversalSearch()
    }

    private fun loadUniversalSearch() {
        updateUniversalSearchStateToLoading()
        universalSearchUseCase.execute(this::processGetUniversalSearchSuccess, this::error)
    }

    private fun updateUniversalSearchStateToLoading() {
        universalSearchState.postValue(Loading())
    }

    private fun updateUniversalSearchStateToSuccess() {
        universalSearchState.postValue(Success(universalSearchVisitableList))
    }

    private fun error(e: Throwable?) {

    }

    private fun processGetUniversalSearchSuccess(universalSearchModel: UniversalSearchModel) {
        processVisitableListFromModel(universalSearchModel)

        updateUniversalSearchStateToSuccess()
    }

    private fun processVisitableListFromModel(universalSearchModel: UniversalSearchModel) {
        val universalDataView = universalSearchModelMapper.convert(universalSearchModel)
        val visitableList = mutableListOf<Visitable<*>>()

        visitableList.addAll(universalDataView.carouselDataView)
        visitableList.addAll(universalDataView.listDataView)
        visitableList.addAll(universalDataView.doubleLineDataView)

        updateUniversalSearchVisitableList(visitableList)
    }

    private fun updateUniversalSearchVisitableList(visitableList: List<Visitable<*>>) {
        universalSearchVisitableList.clear()
        universalSearchVisitableList.addAll(visitableList)
    }

    fun getUniversalSearchState(): LiveData<State<List<Visitable<*>>>> = universalSearchState
}