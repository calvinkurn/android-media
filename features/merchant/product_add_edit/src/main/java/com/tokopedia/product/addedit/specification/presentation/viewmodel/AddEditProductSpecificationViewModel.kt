package com.tokopedia.product.addedit.specification.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.model.Values
import com.tokopedia.product.addedit.specification.domain.usecase.AnnotationCategoryUseCase
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.variant.presentation.extension.getValueOrDefault
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

    private val mAnnotationCategoryData = MutableLiveData<List<AnnotationCategoryData>>()
    val annotationCategoryData: LiveData<List<AnnotationCategoryData>>
        get() = mAnnotationCategoryData

    private val mValidateSpecificationInputModelResult = MutableLiveData<Pair<Boolean, List<SpecificationInputModel>>>()
    val validateSpecificationInputModelResult: LiveData<Pair<Boolean, List<SpecificationInputModel>>>
        get() = mValidateSpecificationInputModelResult

    private val mErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = mErrorMessage

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

    fun getItemSelected(annotationCategoryList: List<AnnotationCategoryData>) =
        annotationCategoryList.map { annotationCategoryData ->
            val productInputModel = mProductInputModel.value
            var valueResult: Values? = null
            val required = annotationCategoryData.isMandatory

            if (productInputModel != null) {
                val specificationList = productInputModel.detailInputModel.specifications.orEmpty()
                valueResult = annotationCategoryData.data.firstOrNull { value ->
                    specificationList.any { it.id == value.id }
                }
            }

            return@map if (valueResult != null) {
                SpecificationInputModel(
                    id = valueResult.id,
                    data = valueResult.name,
                    specificationVariant = annotationCategoryData.variant,
                    required = required)
            } else if (annotationCategoryData.isCustomAnnotType) {
                val specificationList = mProductInputModel.getValueOrDefault().detailInputModel.specifications.orEmpty()
                val selectedSpecification = specificationList.firstOrNull {
                    it.specificationVariant == annotationCategoryData.variant
                } ?: SpecificationInputModel(
                    required = required,
                    specificationVariant = annotationCategoryData.variant,
                )
                selectedSpecification.apply {
                    isTextInput = true
                    variantId = annotationCategoryData.variantId.toString()
                }
            } else {
                SpecificationInputModel(
                    required = required,
                    specificationVariant = annotationCategoryData.variant,
                )
            }
        }

    fun getHasSpecification(specificationList: List<SpecificationInputModel>) = specificationList.any {
        it.id.isNotBlank() || it.data.isNotEmpty()
    }

    fun validateSpecificationInputModel(specificationList: List<SpecificationInputModel>) {
        val validatedSpecificationList = specificationList.map { it.getValidatedData() }
        val isRequiredFieldEmpty = specificationList.any { it.errorMessageRes.isMoreThanZero() }

        mValidateSpecificationInputModelResult.value =
            Pair(!isRequiredFieldEmpty, validatedSpecificationList)
    }

    fun updateProductInputModelSpecifications(specificationList: List<SpecificationInputModel>) {
        mProductInputModel.value?.apply {
            detailInputModel.specifications = specificationList.filter {
                it.id.isNotBlank() || it.data.isNotEmpty()
            }
        }
    }
    fun removeSpecification() {
        updateProductInputModelSpecifications(emptyList())
        mAnnotationCategoryData.value = mAnnotationCategoryData.value // trigger observer change
    }
}
