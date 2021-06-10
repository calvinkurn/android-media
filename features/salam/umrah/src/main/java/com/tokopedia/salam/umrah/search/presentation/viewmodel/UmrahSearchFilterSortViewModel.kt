package com.tokopedia.salam.umrah.search.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.common.usecase.UmrahSearchParameterUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by M on 18/10/19
 */

class UmrahSearchFilterSortViewModel @Inject constructor(private val useCase: UmrahSearchParameterUseCase,
                                                         dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.main) {

    private val privateUmrahSearchParameter by lazy { MutableLiveData<Result<UmrahSearchParameterEntity>>() }
    val umrahSearchParameter: LiveData<Result<UmrahSearchParameterEntity>>
        get() = privateUmrahSearchParameter

    fun getUmrahSearchParameter(rawQuery: String) {
        launch {
            privateUmrahSearchParameter.value = useCase.executeUseCase(rawQuery)
        }
    }
}