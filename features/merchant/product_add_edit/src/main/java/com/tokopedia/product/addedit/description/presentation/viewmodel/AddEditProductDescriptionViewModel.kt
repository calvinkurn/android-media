package com.tokopedia.product.addedit.description.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.domain.usecase.GetProductVariantUseCase
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductDescriptionViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val saveProductDraftUseCase: SaveProductDraftUseCase
) : BaseViewModel(coroutineDispatcher) {

    var categoryId: String = ""
    var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()
    var isEditMode: Boolean = false

    private val _productVariant = MutableLiveData<Result<List<ProductVariantByCatModel>>>()
    val productVariant: LiveData<Result<List<ProductVariantByCatModel>>>
        get() = _productVariant

    private val _saveProductDraftResult = MutableLiveData<Result<Long>>()
    val saveProductDraftResult: LiveData<Result<Long>>
        get() = _saveProductDraftResult

    fun getVariants(categoryId: String) {
        launchCatchError(block = {
            _productVariant.value = Success(withContext(Dispatchers.IO) {
                getProductVariantUseCase.params =
                        GetProductVariantUseCase.createRequestParams(categoryId)
                getProductVariantUseCase.executeOnBackground()
            })
        }, onError = {
            _productVariant.value = Fail(it)
        })
    }

    fun saveProductDraft(productDraft: ProductDraft, productId: Long, isUploading: Boolean) {
        launchCatchError(block = {
            saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(productDraft, productId, isUploading)
            _saveProductDraftResult.value = withContext(Dispatchers.IO) {
                saveProductDraftUseCase.executeOnBackground()
            }.let { Success(it) }
        }, onError = {
            _saveProductDraftResult.value = Fail(it)
        })
    }
}