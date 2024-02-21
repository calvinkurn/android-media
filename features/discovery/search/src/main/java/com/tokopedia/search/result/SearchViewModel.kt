package com.tokopedia.search.result

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.usecase.thematic.ThematicModel
import com.tokopedia.home_component.usecase.thematic.ThematicUsecaseUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.tokopedia.search.utils.mvvm.SearchViewModel as ISearchViewModel

internal class SearchViewModel(
    searchState: SearchState,
    coroutineDispatcher: CoroutineDispatchers,
    private val thematicUseCase: UseCase<ThematicModel>
) : BaseViewModel(coroutineDispatcher.main), ISearchViewModel<SearchState> {

    private val _stateFlow = MutableStateFlow(searchState)

    override val stateFlow: StateFlow<SearchState>
        get() = _stateFlow.asStateFlow()

    val activeTabPosition : Int
        get() = stateFlow.value.activeTabPosition

    fun showAutoCompleteView() {
        _stateFlow.update { it.openAutoComplete() }
    }

    fun showAutoCompleteHandled() {
        _stateFlow.update { it.openAutoCompleteHandled() }
    }

    fun setActiveTab(position: Int) {
        _stateFlow.update { it.activeTab(position) }
    }

    fun getThematic() {
        val requestParam = RequestParams()
        requestParam.putString(
            ThematicUsecaseUtil.THEMATIC_PARAM,
            ThematicUsecaseUtil.THEMATIC_PAGE_SRP
        )
        thematicUseCase.execute({ resultModel ->
            _stateFlow.update {
                it.updateThematicModel(resultModel)
            }
        }, { }, requestParam)
    }
}
