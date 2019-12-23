package com.tokopedia.rechargeocr.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.rechargeocr.data.RechargeOcrResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeUploadImageViewModel @Inject constructor(private val rechargeUploadImageUseCase: RechargeUploadImageUseCase,
                                                       private val graphqlRepository: GraphqlRepository,
                                                       private val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : BaseViewModel(dispatcher) {

    val urlImage = MutableLiveData<String>()
    val resultDataOcr = MutableLiveData<String>()
    val errorActionOcr = MutableLiveData<String>()

    fun uploadImageRecharge(fileLocation: String?) {
        launchCatchError(block = {
            val dataUploadImage = withContext(dispatcher) {
                rechargeUploadImageUseCase.execute(fileLocation)
            }
            var url = dataUploadImage.dataUploadImageData.picSrc
            if (url.contains(DEFAULT_RESOLUTION)) {
                url = url.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_500)
            }
            urlImage.postValue(url)
        }) {
            urlImage.postValue("")
        }
    }

    fun getResultOcr(rawQuery: String, imageSrc: String) {
        launchCatchError(block = {
            val dataOcr = withContext(dispatcher) {
                val mapParam = mutableMapOf<String, Any>()
                mapParam[PARAM_IMAGE_OCR] = imageSrc
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeOcrResponse::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RechargeOcrResponse>()
            resultDataOcr.postValue(dataOcr.rechargeOcr.result)
        }) {
            errorActionOcr.postValue(it.message)
        }
    }

    companion object {
        private const val PARAM_IMAGE_OCR = "Image"
        private const val DEFAULT_RESOLUTION = "100-square"
        private const val RESOLUTION_500 = "500"
    }
}