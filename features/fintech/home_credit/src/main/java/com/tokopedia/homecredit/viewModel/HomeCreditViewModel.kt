package com.tokopedia.homecredit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.homecredit.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.homecredit.domain.model.ImageDetail
import com.tokopedia.homecredit.domain.usecase.HomeCreditUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class HomeCreditViewModel @Inject constructor(
    private val homeCreditUseCase: HomeCreditUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _imageDetailLiveData = MutableLiveData<Result<ImageDetail>>()
    var imageDetailLiveData: LiveData<Result<ImageDetail>> = _imageDetailLiveData


    fun computeImageArray(
        imageByte: ByteArray,
        mCaptureNativeSize: Size?

    ) {
        homeCreditUseCase.saveDetail(
            ::onSuccessSave,
            ::onFailSave,
            imageByte, mCaptureNativeSize

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
