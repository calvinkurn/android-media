package com.tokopedia.logisticaddaddress.features.autocomplete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteMapper
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticaddaddress.domain.query.AutoCompleteQuery
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutoCompleteResultUi
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AutoCompleteViewModel
@Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val autoCompleteUseCase: GraphqlUseCase<AutocompleteResponse>,
        private val autoCompleteMapper: AutocompleteMapper) : BaseViewModel(dispatcher) {

    private val mAutoCompleteList = MutableLiveData<Result<List<AutoCompleteResultUi>>>()
    val autoCompleteList: LiveData<Result<List<AutoCompleteResultUi>>>
        get() = mAutoCompleteList

    fun getAutoCompleteList(keyword: String) {
        autoCompleteUseCase.setTypeClass(AutocompleteResponse::class.java)
        autoCompleteUseCase.setGraphqlQuery(AutoCompleteQuery.keroAutoCompleteGeocode)
        autoCompleteUseCase.setRequestParams(mapOf(
                "param" to keyword
        ))

        autoCompleteUseCase.execute(
                { response ->
                    mAutoCompleteList.value = Success(autoCompleteMapper.mapLean(response))
                },
                { e -> mAutoCompleteList.value = Fail(e) })
    }

}