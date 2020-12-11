package com.tokopedia.product.addedit.specification.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.domain.usecase.AnnotationCategoryUseCase
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductSpecificationViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val annotationCategoryUseCase: AnnotationCategoryUseCase
) : BaseViewModel(dispatcher.main) {

    private val mProductInputModel = MutableLiveData<ProductInputModel>()
    val productInputModel: LiveData<ProductInputModel>
        get() = mProductInputModel

    fun getSpecifications(categoryId: String) {
        launchCatchError(block = {
            val result = Success(withContext(dispatcher.io) {
                annotationCategoryUseCase.setParamsCategoryId(categoryId)
                annotationCategoryUseCase.executeOnBackground()
            })
        }, onError = {
           // no-op
        })
    }

    fun setProductInputModel(productInputModel: ProductInputModel?) {
        mProductInputModel.value = productInputModel
    }

}