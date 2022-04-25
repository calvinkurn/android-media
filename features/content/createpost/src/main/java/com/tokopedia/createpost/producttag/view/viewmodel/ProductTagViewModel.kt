package com.tokopedia.createpost.producttag.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class ProductTagViewModel @Inject constructor(

): ViewModel() {

    private val _productTagSourceList = MutableStateFlow<List<ProductTagSource>>(emptyList())

    fun processProductTagSource(productTagSourceRaw: String) {
        viewModelScope.launchCatchError(block = {
            val split = productTagSourceRaw.split(",")
            _productTagSourceList.value = split.map {
                ProductTagSource.mapFromString(it)
            }
        }) { }
    }
}