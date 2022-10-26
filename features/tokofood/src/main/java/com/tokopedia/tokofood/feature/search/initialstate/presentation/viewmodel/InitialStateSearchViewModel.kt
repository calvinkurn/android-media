package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.search.initialstate.domain.usecase.GetInitSearchStateUseCase
import com.tokopedia.tokofood.feature.search.initialstate.domain.usecase.RemoveSearchHistoryUseCase
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.InitialStateWrapperUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RemoveSearchHistoryUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InitialStateSearchViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val initialStateUseCase: Lazy<GetInitSearchStateUseCase>,
    private val removeSearchHistoryUseCase: Lazy<RemoveSearchHistoryUseCase>
) : BaseViewModel(coroutineDispatchers.main) {

    val initialStateWrapper: LiveData<Result<InitialStateWrapperUiModel>>
        get() = _initialStateWrapper

    private val _initialStateWrapper = MutableLiveData<Result<InitialStateWrapperUiModel>>()

    val removeSearchHistory: LiveData<Result<RemoveSearchHistoryUiModel>>
        get() = _removeSearchHistory

    private val _removeSearchHistory = MutableLiveData<Result<RemoveSearchHistoryUiModel>>()

    fun fetchInitialState(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {
             val response = withContext(coroutineDispatchers.io) {
                 initialStateUseCase.get().execute(localCacheModel)
             }
            _initialStateWrapper.value = Success(response)
        }, onError = {
            _initialStateWrapper.value = Fail(it)
        })
    }

    fun removeRemoveRecentSearch(key: String) {
        launchCatchError(block = {
            val response = withContext(coroutineDispatchers.io) {
                removeSearchHistoryUseCase.get().execute(key)
            }
            _removeSearchHistory.value = Success(response)
        }, onError = {
            _removeSearchHistory.value = Fail(it)
        })
    }
}