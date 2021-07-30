package com.tokopedia.imagepicker_insta

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    workerDispatcher: CoroutineDispatcher,
    val photosUseCase: PhotosUseCase,
    val app: Application
) : BaseAndroidViewModel(workerDispatcher,app) {

    val photosLiveData:MutableLiveData<LiveDataResult<List<Asset>>> = MutableLiveData()
    fun getPhotos() {
        launchCatchError(block = {
            photosLiveData.postValue(LiveDataResult.loading())
            val result = photosUseCase.getPhotos(app)
            photosLiveData.postValue(LiveDataResult.success(result))
        },onError = {
            photosLiveData.postValue(LiveDataResult.error(it))
        })
    }
}