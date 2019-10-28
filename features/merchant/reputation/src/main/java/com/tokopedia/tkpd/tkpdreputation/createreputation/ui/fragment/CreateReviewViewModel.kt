package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.BaseImageReviewViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.DefaultImageReviewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ImageReviewViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevGetForm
import com.tokopedia.tkpd.tkpdreputation.createreputation.usecase.GetProductReputationForm
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class CreateReviewViewModel @Inject constructor(@Named("Main")
                                                val dispatcher: CoroutineDispatcher,
                                                private val getProductReputationForm: GetProductReputationForm) : BaseViewModel(dispatcher) {

    private var imageData: MutableList<BaseImageReviewViewModel> = mutableListOf()
    private var reputationDataForm = MutableLiveData<Result<ProductRevGetForm>>()
    val getReputationDataForm  = reputationDataForm

    fun getImageList(selectedImage: ArrayList<String>): MutableList<BaseImageReviewViewModel> {
        when (selectedImage.size) {
            5 -> {
                imageData = (selectedImage.take(4).map {
                    ImageReviewViewModel(it, shouldDisplayOverlay = true)
                }).asReversed().toMutableList()
            }
            4 -> {
                imageData.addAll(selectedImage.take(3).map {
                    ImageReviewViewModel(it, shouldDisplayOverlay = true)
                }.asReversed())
            }
            else -> {
                imageData.addAll(selectedImage.map {
                    ImageReviewViewModel(it, shouldDisplayOverlay = false)
                }.asReversed())
            }
        }

        return imageData
    }

    fun initImageData(): MutableList<BaseImageReviewViewModel> {
        imageData.clear()
        imageData.add(DefaultImageReviewModel())
        return imageData
    }

    fun getProductReputation() {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(55368, 15267029))
            }
            reputationDataForm.value = Success(data)

        }) {
            reputationDataForm.value = Fail(it)

        }
    }
}