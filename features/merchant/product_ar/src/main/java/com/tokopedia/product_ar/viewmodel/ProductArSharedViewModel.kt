package com.tokopedia.product_ar.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.ProductArComparissonFragmentModel

class ProductArSharedViewModel : ViewModel() {

    private val _arListData = MutableLiveData<ProductArComparissonFragmentModel>()
    val arListData: LiveData<ProductArComparissonFragmentModel>
        get() = _arListData


    fun setArListData(listOfArData: List<ModifaceUiModel>, processedPhoto: Bitmap, originalPhoto: Bitmap) {
        _arListData.value = ProductArComparissonFragmentModel(
                processedPhoto = processedPhoto,
                originalPhoto = originalPhoto,
                modifaceUiModel = listOfArData
        )
    }
}