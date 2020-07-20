package com.tokopedia.deals.common.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.deals.common.domain.GetDealsBrandCategoryActivityUseCase
import com.tokopedia.deals.common.utils.DealsDispatcherProvider
import com.tokopedia.deals.search.model.response.CuratedData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by firman on 22/06/20
 */

class DealsBrandCategoryActivityViewModel @Inject constructor(
        private val dealsBrandCategoryActivityUseCase: GetDealsBrandCategoryActivityUseCase,
        dispatcher: DealsDispatcherProvider
) : BaseViewModel(dispatcher.io()) {

    private val privateCuratedData = MutableLiveData<CuratedData>()
    val curatedData: LiveData<CuratedData>
        get() = privateCuratedData

    private val privateErrorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable>
        get() = privateErrorMessage

    fun getCategoryCombindedData() {
        launch {
            val result = dealsBrandCategoryActivityUseCase.getChildCategoryResult()
            when (result) {
                is Success -> {
                    privateCuratedData.postValue(result.data)
                }

                is Fail -> {
                    privateErrorMessage.postValue(result.throwable)
                }
            }
        }
    }
}