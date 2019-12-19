package com.tokopedia.rechargeocr.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.rechargeocr.data.RechargeOcrResponse
import com.tokopedia.rechargeocr.data.RechargeUploadImageResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class RechargeUploadImageViewModel @Inject constructor(private val uploadImageUseCase: UploadImageUseCase<RechargeUploadImageResponse>,
                                                       private val graphqlRepository: GraphqlRepository,
                                                       private val userSession: UserSessionInterface,
                                                       private val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : BaseViewModel(dispatcher) {

    val urlImage = MutableLiveData<String>()

    fun uploadImageRecharge(fileLocation: String?) {
        launchCatchError(block = {
            val dataUploadImage = getUrlUploadImage(fileLocation)
            dataUploadImage.data.picSrc?.let {
                var url = it
                if (it.contains(DEFAULT_RESOLUTION)) {
                    url = url.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_500)
                }
                urlImage.value = url
            }
        }) {
            urlImage.value = ""
        }
    }

    suspend fun getUrlUploadImage(fileLocation: String?): RechargeUploadImageResponse {
        return withContext(dispatcher) {
            uploadImageUseCase.createObservable(createUploadParams(fileLocation))
                    .toBlocking().first().dataResultImageUpload
        }
    }

    fun getResultOcr(rawQuery: String, imageSrc: String,
                     onSuccessOcr: (String) -> Unit,
                     onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            val dataOcr = withContext(dispatcher) {
                val mapParam = mutableMapOf<String, Any>()
                mapParam[PARAM_IMAGE_OCR] = imageSrc
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeOcrResponse::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RechargeOcrResponse>()
            onSuccessOcr(dataOcr.rechargeOcr.result)
        }) {
            onError(it)
        }
    }

    private fun createUploadParams(fileLocation: String?): RequestParams {
        val maps = HashMap<String, RequestBody>()
        val webService = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                DEFAULT_WEB_SERVICE
        )
        val resolution = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                RESOLUTION_500
        )
        val id = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                userSession.userId + UUID.randomUUID() + System.currentTimeMillis()
        )
        maps[PARAM_WEB_SERVICE] = webService
        maps[PARAM_ID] = id
        maps[PARAM_RESOLUTION] = resolution
        return uploadImageUseCase.createRequestParam(
                fileLocation,
                DEFAULT_UPLOAD_PATH,
                DEFAULT_UPLOAD_TYPE,
                maps
        )
    }

    companion object {
        private const val PARAM_IMAGE_OCR = "Image"
        private const val PARAM_ID = "id"
        private const val PARAM_WEB_SERVICE = "web_service"
        private const val PARAM_RESOLUTION = "param_resolution"
        private const val DEFAULT_WEB_SERVICE = "1"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"
        private const val DEFAULT_RESOLUTION = "100-square"
        private const val RESOLUTION_500 = "500"
        private const val TEXT_PLAIN = "text/plain"
    }
}