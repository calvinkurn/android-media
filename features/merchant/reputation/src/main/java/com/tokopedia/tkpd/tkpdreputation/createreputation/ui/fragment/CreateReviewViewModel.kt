package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.BaseImageReviewViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.DefaultImageReviewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ImageReviewViewModel
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class CreateReviewViewModel @Inject constructor(@Named("Main")
                                                val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    private var imageData: MutableList<BaseImageReviewViewModel> = mutableListOf()

    fun getImageList(selectedImage: ArrayList<String>):MutableList<BaseImageReviewViewModel>  {
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
}