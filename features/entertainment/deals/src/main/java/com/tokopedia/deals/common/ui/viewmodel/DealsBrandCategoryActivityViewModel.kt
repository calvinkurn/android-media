package com.tokopedia.deals.common.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.deals.category.domain.GetChipsCategoryUseCase
import com.tokopedia.deals.common.utils.DealsDispatcherProvider
import com.tokopedia.deals.search.model.response.CuratedData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
        launchCatchError(block = {
            privateCuratedData.postValue(getChipCategory())
        }){
            privateErrorMessage.postValue(it)
        }
    }

    private suspend fun getChipCategory(): CuratedData {
        return try {
            return chipsCategoryUseCase.executeOnBackground()
        } catch (t: Throwable) {
            throw t
        }
    }
}