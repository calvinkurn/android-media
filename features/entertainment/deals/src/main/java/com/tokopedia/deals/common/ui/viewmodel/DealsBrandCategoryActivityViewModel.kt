package com.tokopedia.deals.common.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.data.entity.CuratedData
import com.tokopedia.deals.domain.GetChipsCategoryUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by firman on 22/06/20
 */

class DealsBrandCategoryActivityViewModel @Inject constructor(
    private val chipsCategoryCoroutine: GetChipsCategoryUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val privateCuratedData = MutableLiveData<CuratedData>()
    val curatedData: LiveData<CuratedData>
        get() = privateCuratedData

    private val privateErrorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable>
        get() = privateErrorMessage

    fun getCategoryCombindedData() {
        launch {
            runCatching {
                val result = chipsCategoryCoroutine(Unit)
                privateCuratedData.value = result
            }.onFailure {
                privateErrorMessage.value = it
            }
        }
    }
}
