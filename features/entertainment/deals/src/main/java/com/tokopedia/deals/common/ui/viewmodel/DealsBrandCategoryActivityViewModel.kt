package com.tokopedia.deals.common.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.deals.category.domain.GetChipsCategoryUseCase
import com.tokopedia.deals.common.utils.DealsDispatcherProvider
import com.tokopedia.deals.search.model.response.CuratedData
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by firman on 22/06/20
 */

class DealsBrandCategoryActivityViewModel @Inject constructor(
        private val chipsCategoryUseCase: GetChipsCategoryUseCase,
        dispatcher: DealsDispatcherProvider
) : BaseViewModel(dispatcher.io()) {

    private val privateCuratedData = MutableLiveData<CuratedData>()
    val curatedData: LiveData<CuratedData>
        get() = privateCuratedData

    private val privateErrorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable>
        get() = privateErrorMessage

    fun getCategoryCombindedData() {
        launch { chipsCategoryUseCase.execute(onGetCategorySuccess(), onErrorGetCategory()) }
    }

    private fun onGetCategorySuccess(): (CuratedData) -> Unit = { privateCuratedData.value = it }
    private fun onErrorGetCategory(): (Throwable) -> Unit = { privateErrorMessage.value = it }
}