package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.product.addedit.common.domain.mapper.AddProductInputMapper
import com.tokopedia.product.addedit.common.domain.model.responses.ProductAddEditV3Response
import com.tokopedia.product.addedit.common.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.description.model.DescriptionInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AddEditProductPreviewViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val uploaderUseCase: UploaderUseCase,
        private val addProductInputMapper: AddProductInputMapper,
        private val productAddUseCase: ProductAddUseCase
) : BaseViewModel(coroutineDispatcher) {
    val _productUpdateResult = MutableLiveData<Result<ProductAddEditV3Response>>()

    fun addProduct(detailInputModel: DetailInputModel,
                   descriptionInputModel: DescriptionInputModel,
                   shipmentInputModel: ShipmentInputModel) {
        val param = addProductInputMapper
                .mapInputToRemoteModel(detailInputModel, descriptionInputModel, shipmentInputModel)

        launchCatchError(block = {
            _productUpdateResult.value = Success(withContext(Dispatchers.IO) {
                productAddUseCase.params = ProductAddUseCase.createRequestParams(param)
                return@withContext productAddUseCase.executeOnBackground()
            })
        }, onError = {
            _productUpdateResult.value = Fail(it)
        })
    }

    fun uploadImage() {
        val filePath = File("/sdcard/Download/test.jpg")
        val sourceId = "VqbcmM" // TODO faisalramd move to constant
        val params = uploaderUseCase.createParams(
                sourceId = sourceId,
                filePath = filePath
        )

        launch(coroutineContext) {
            val result = uploaderUseCase(params)
            withContext(Dispatchers.Main) {
                when (result) {
                    is UploadResult.Success -> result.uploadId
                    is UploadResult.Error -> ""
                }
            }
        }
    }
}