package com.tokopedia.product.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.detail.view.widget.ProductVideoDataModel

/**
 * Created by Yehezkiel on 02/12/20
 */
class ProductDetailSharedViewModel : ViewModel() {

    val productVideoData: LiveData<List<ProductVideoDataModel>>
        get() = _productVideoData
    private val _productVideoData = MutableLiveData<List<ProductVideoDataModel>>()

    val productVideoDetailData: LiveData<List<ProductVideoDataModel>>
        get() = _productVideoDetailData
    private val _productVideoDetailData = MutableLiveData<List<ProductVideoDataModel>>()

    //Give video data from pdp fragment to video detail
    fun updateVideoDetailData(currentVideo: List<ProductVideoDataModel>) {
        _productVideoDetailData.postValue(currentVideo)
    }

    //Give back the data from video detail to pdp fragment
    fun updateVideoData(video: List<ProductVideoDataModel>) {
        _productVideoData.postValue(video)
    }
}