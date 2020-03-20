package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.product.addedit.common.domain.model.params.add.*
import com.tokopedia.product.addedit.common.domain.model.params.edit.ProductEditPriceParam
import com.tokopedia.product.addedit.common.domain.model.responses.ProductAddEditV3Response
import com.tokopedia.product.addedit.common.domain.usecase.EditPriceUseCase
import com.tokopedia.product.addedit.common.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_KILOGRAM
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AddEditProductShipmentViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val uploaderUseCase: UploaderUseCase,
        private val editPriceUseCase: EditPriceUseCase,
        private val productAddUseCase: ProductAddUseCase
) : BaseViewModel(coroutineDispatcher) {

    val _productUpdateResult = MutableLiveData<Result<ProductAddEditV3Response>>()

    private fun getWeight(weight: String) = weight.replace(".", "").toIntOrZero()

    fun isWeightValid(weight: String, unitWeight: Int, minWeight: Int): Boolean {
        var isValid = false
        val maxWeight = if (unitWeight == UNIT_KILOGRAM) MAX_WEIGHT_KILOGRAM else MAX_WEIGHT_GRAM
        if (getWeight(weight) in minWeight until maxWeight + 1) {
            isValid = true
        }

        return isValid
    }

    fun editPrice() {
        // TODO faisalramd make mapper
        val param = ProductAddParam(
                "Baju polos yang terbaik di tookpedo",
                20000,
                "IDR",
                9999,
                "LIMITED",
                "desc",
                1,
                "GR",
                20,
                "NEW",
                false,
                "",
                Catalog(
                        "1"
                ),
                Category(
                        "1"
                ),
                Menu(
                        "0",
                        ""
                ),
                Pictures(),
                Preorder(
                        1,
                        "1",
                        true
                )

        )

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