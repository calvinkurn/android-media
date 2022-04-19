package com.tokopedia.gamification.giftbox.presentation.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.presentation.LidImagesDownloaderUseCase
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Named

class GiftBoxImageDownloadViewModel @Inject constructor(@Named(IO) workerDispatcher: CoroutineDispatcher,
                                                        val lidImagesDownloaderUseCase: LidImagesDownloaderUseCase,
                                                        val app: Application)
    : BaseAndroidViewModel(workerDispatcher, app) {

    fun downloadImages(urlList: List<String>, imageListLiveData: MutableLiveData<LiveDataResult<List<Bitmap>?>>) {
        launchCatchError(block = {
            val drawableList = lidImagesDownloaderUseCase.downloadImages(getApplication(), urlList)
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
            withTimeout(6000L) {
                val drawable = lidImagesDownloaderUseCase.downloadBgImage(app, url)
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