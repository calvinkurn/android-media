package com.tokopedia.homecredit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.homecredit.domain.model.ImageDetail
import com.tokopedia.homecredit.domain.usecase.HomeCreditUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.io.File
import javax.inject.Inject

class HomeCreditViewModel @Inject constructor(
    private val homeCreditUseCase: HomeCreditUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _imageDetailLiveData = MutableLiveData<Result<ImageDetail>>()
    var imageDetailLiveData: LiveData<Result<ImageDetail>> = _imageDetailLiveData


    fun computeImageArray(
        imageByte: ByteArray,
        mCaptureNativeSize: Size,
        filePath: File
    ) {
        homeCreditUseCase.saveDetail(
            imageByte, mCaptureNativeSize, filePath,
            ::onSuccessSave,
            ::onFailSave
        )
    }

    private fun onFailSave(it: Throwable) {
        _imageDetailLiveData.value = Fail(it)
    }

    private fun onSuccessSave(imageDetail: ImageDetail) {
        _imageDetailLiveData.value = Success(imageDetail)

    }

    override fun onCleared() {
        super.onCleared()
        homeCreditUseCase.cancelJobs()
    }

}
