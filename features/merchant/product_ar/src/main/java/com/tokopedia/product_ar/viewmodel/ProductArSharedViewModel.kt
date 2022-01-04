package com.tokopedia.product_ar.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product_ar.model.ModifaceUiModel

class ProductArSharedViewModel : ViewModel() {

    private val _arListData = MutableLiveData<Pair<List<ModifaceUiModel>, Bitmap>>()
    val arListData: LiveData<Pair<List<ModifaceUiModel>, Bitmap>>
        get() = _arListData


    fun setArListData(listOfArData: List<ModifaceUiModel>, bitmap: Bitmap) {
        _arListData.value = listOfArData to bitmap
    }
}