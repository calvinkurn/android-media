package com.tokopedia.product.detail.bseducational.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.bseducational.data.ProductEducational
import com.tokopedia.product.detail.bseducational.di.TYPE_ARG_NAMED
import com.tokopedia.product.detail.bseducational.usecase.GetProductEducationalUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject
import javax.inject.Named

class ProductEducationalViewModel @Inject constructor(
        @Named(TYPE_ARG_NAMED) private val typeParam: String,
        private val productEducationalUseCase: GetProductEducationalUseCase,
        dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.io) {

    private val _educationalData = MutableLiveData<Result<ProductEducational>>()
    val educationalData: LiveData<Result<ProductEducational>>
        get() = _educationalData

    init {
        getEducationalData()
    }

    fun getEducationalData() {
        launchCatchError(block = {
            val value = productEducationalUseCase
                    .executeOnBackground(GetProductEducationalUseCase.createParams(typeParam))
            _educationalData.postValue(Success(value))
        }) {
            _educationalData.postValue(Fail(it))
        }
    }
}