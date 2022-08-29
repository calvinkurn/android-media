package com.tokopedia.autocompletecomponent.universal.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.presentation.model.UniversalDataView
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.usecase.coroutines.UseCase

internal class UniversalSearchViewModelFactory(
    private val baseDispatcher: CoroutineDispatchers,
    private val universalSearchUseCase: UseCase<UniversalSearchModel>,
    private val universalSearchModelMapper: Mapper<UniversalSearchModel, UniversalDataView>,
    private val searchParameter: Map<String, Any> = mapOf(),
    private val context: Context,
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UniversalSearchViewModel::class.java)) {
            return createUniversalSearchViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createUniversalSearchViewModel(): UniversalSearchViewModel {
        return UniversalSearchViewModel(
            baseDispatcher,
            universalSearchUseCase,
            universalSearchModelMapper,
            searchParameter,
            context
        )
    }
}