package com.tokopedia.product.addedit.specification.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.model.Values
import com.tokopedia.product.addedit.specification.domain.usecase.AnnotationCategoryUseCase
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductSpecificationViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val annotationCategoryUseCase: AnnotationCategoryUseCase
) : BaseViewModel(dispatcher.main) {

    private val mProductInputModel = MutableLiveData<ProductInputModel>()
    val productInputModel: LiveData<ProductInputModel> get() = mProductInputModel

    private val mAnnotationCategoryData = MutableLiveData<List<AnnotationCategoryData>>()
    val annotationCategoryData: LiveData<List<AnnotationCategoryData>> get() = mAnnotationCategoryData

    private val mErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = mErrorMessage

    fun setProductInputModel(productInputModel: ProductInputModel?) {
        mProductInputModel.value = productInputModel
    }

    fun getSpecifications(categoryId: String) {
        launchCatchError(block = {
            val result = Success(withContext(dispatcher.io) {
                annotationCategoryUseCase.setParamsCategoryId(categoryId)
                annotationCategoryUseCase.executeOnBackground()
            })
            mAnnotationCategoryData.value = result.data.drogonAnnotationCategoryV2.data
        }, onError = {
            mErrorMessage.value = it.message
        })
    }

    fun getItemSelected(annotationCategoryList: List<AnnotationCategoryData>) = annotationCategoryList.map { annotationCategoryData ->
        val productInputModel = mProductInputModel.value
        var valueResult: Values? = null

        if (productInputModel != null) {
            val specificationList = productInputModel.detailInputModel.specifications.orEmpty()
            valueResult = annotationCategoryData.data.firstOrNull { value ->
                specificationList.any { it.id == value.id.toString() }
            }
        }

        return@map if (valueResult != null) {
            SpecificationInputModel(
                    id = valueResult.id.toString(),
                    data = valueResult.name)
        } else {
            SpecificationInputModel()
        }
    }

    fun getHasSpecification(specificationList: List<SpecificationInputModel>) = specificationList.any {
        it.id.isNotBlank()
    }

    fun updateProductInputModelSpecifications(specificationList: List<SpecificationInputModel>) {
        mProductInputModel.value?.apply {
            detailInputModel.specifications = specificationList.filter {
                it.id.isNotBlank()
            }
        }
    }

    fun removeSpecification() {
        updateProductInputModelSpecifications(emptyList())
        mAnnotationCategoryData.value = mAnnotationCategoryData.value // trigger observer change
    }
}