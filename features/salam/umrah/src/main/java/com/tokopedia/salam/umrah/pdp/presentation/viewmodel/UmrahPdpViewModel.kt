package com.tokopedia.salam.umrah.pdp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.common.util.UmrahDispatchersProvider
import com.tokopedia.salam.umrah.pdp.presentation.usecase.UmrahPdpUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by M on 29/10/2019
 */
class UmrahPdpViewModel @Inject constructor(private val umrahPdpUseCase: UmrahPdpUseCase,
                                            dispatcher: UmrahDispatchersProvider)
    : BaseViewModel(dispatcher.Main) {

    var slugName: String = ""
    private val privatePdpResult = MutableLiveData<Result<UmrahProductModel.UmrahProduct>>()
    val pdpResult: LiveData<Result<UmrahProductModel.UmrahProduct>>
        get() = privatePdpResult

    fun getUmrahPdp(searchQuery: String) {
        launch {
            val result = umrahPdpUseCase.execute(searchQuery, slugName)
            privatePdpResult.value = result
        }
    }
}