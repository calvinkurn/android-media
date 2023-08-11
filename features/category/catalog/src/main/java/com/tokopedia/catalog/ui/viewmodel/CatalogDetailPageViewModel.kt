package com.tokopedia.catalog.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class CatalogDetailPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    private var _errors = MutableLiveData<Throwable>()
    val errors: LiveData<Throwable>
        get() = _errors

    private var _isAddProductSuccess = MutableLiveData<Boolean>()
    val isAddProductSuccess: LiveData<Boolean>
        get() = _isAddProductSuccess

    fun getA() {
        println("asdasd")
    }

}
