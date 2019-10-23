package com.tokopedia.topads.detail_sheet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.topads.detail_sheet.data.source.TopAdsSheetRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Author errysuprayogi on 16,May,2019
 */
class TopAdsSheetViewModelFactory @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val repository: TopAdsSheetRepository
): ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TopAdsSheetViewModel(repository, dispatcher) as T
    }
}