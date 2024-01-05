package com.tokopedia.gamification.giftbox.presentation.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gamification.giftbox.presentation.LidImagesDownloaderUseCase
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class GiftBoxImageDownloadViewModel @Inject constructor(
    private val lidImagesDownloaderUseCase: LidImagesDownloaderUseCase,
    private val application: Application,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {
    companion object {
        const val TIMEOUT_DURATION = 6000L
    }

    fun downloadImages(urlList: List<String>, imageListLiveData: MutableLiveData<LiveDataResult<List<Bitmap>?>>) {
        launchCatchError(block = {
            val drawableList = lidImagesDownloaderUseCase.downloadImages(application, urlList)
            if (urlList.size == drawableList.size) {
                imageListLiveData.postValue(LiveDataResult.success(drawableList))
            } else {
                imageListLiveData.postValue(LiveDataResult.error(Throwable("Unable to download")))
            }
        }, onError = {
            imageListLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun downloadImage(url: String, imageLiveData: MutableLiveData<LiveDataResult<Bitmap?>>) {
        launchCatchError(block = {
            var responseReceived = false
            withTimeout(TIMEOUT_DURATION) {
                val drawable = lidImagesDownloaderUseCase.downloadBgImage(application, url)
                if (drawable != null) {
                    responseReceived = true
                    imageLiveData.postValue(LiveDataResult.success(drawable))
                }
            }
            if (!responseReceived) {
                imageLiveData.postValue(LiveDataResult.error(RuntimeException("Timeout exception")))
            }

        }, onError = {
            imageLiveData.postValue(LiveDataResult.error(it))
        })
    }
}
