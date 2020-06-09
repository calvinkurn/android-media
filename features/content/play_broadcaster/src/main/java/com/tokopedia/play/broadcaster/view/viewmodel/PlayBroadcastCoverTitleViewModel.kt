package com.tokopedia.play.broadcaster.view.viewmodel

import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.model.PlayCoverUploadEntity
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by furqan on 09/06/2020
 */
class PlayBroadcastCoverTitleViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) dispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
        private val uploadImageUseCase: UploadImageUseCase<PlayCoverUploadEntity>,
        private val userSession: UserSessionInterface)
    : BaseViewModel(dispatcher) {

    fun uploadCover(imagePath: String) {
        launchCatchError(ioDispatcher, block = {
            val params = hashMapOf<String, RequestBody>()
            params[PARAM_WEB_SERVICE] = RequestBody.create(MediaType.parse(TEXT_PLAIN), DEFAULT_WEB_SERVICE)
            params[PARAM_RESOLUTION] = RequestBody.create(MediaType.parse(TEXT_PLAIN), RESOLUTION_500)
            params[PARAM_ID] = RequestBody.create(MediaType.parse(TEXT_PLAIN),
                    "${userSession.userId}${UUID.randomUUID()}${System.currentTimeMillis()}")

            val dataUploadedImage = uploadImageUseCase
                    .createObservable(uploadImageUseCase.createRequestParam(
                            imagePath,
                            DEFAULT_UPLOAD_PATH,
                            DEFAULT_UPLOAD_TYPE,
                            params))
                    .toBlocking()
                    .first()
                    .dataResultImageUpload

            var url = dataUploadedImage.data.picSrc
            Log.d("ImageUpload", url)
            if (url.contains(DEFAULT_RESOLUTION)) {
                url = url.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_500)
                Log.d("ImageUpload", url)
            }
        }) {
            it.printStackTrace()
        }
    }

    companion object {
        private const val PARAM_ID = "id"
        private const val PARAM_WEB_SERVICE = "web_service"
        private const val PARAM_RESOLUTION = "param_resolution"
        private const val DEFAULT_RESOLUTION = "100-square"
        private const val DEFAULT_WEB_SERVICE = "1"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"
        private const val TEXT_PLAIN = "text/plain"
        private const val RESOLUTION_500 = "500"
    }

}