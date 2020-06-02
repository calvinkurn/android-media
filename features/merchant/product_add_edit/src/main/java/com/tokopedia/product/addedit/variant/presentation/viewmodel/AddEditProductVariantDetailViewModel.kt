package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddEditProductVariantDetailViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel(coroutineDispatcher) {

    var productInputModel = MutableLiveData<ProductInputModel>()

}