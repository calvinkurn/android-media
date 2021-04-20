package com.tokopedia.product.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.detail.view.fragment.ProductVideoDetailDataModel
import com.tokopedia.product.detail.view.widget.ProductVideoDataModel
import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest

/**
 * Created by Yehezkiel on 02/12/20
 */
class ProductDetailSharedViewModel : ViewModel() {

    val productVideoData: LiveData<List<ProductVideoDataModel>>
        get() = _productVideoData
    private val _productVideoData = MutableLiveData<List<ProductVideoDataModel>>()

    val productVideoDetailData: LiveData<ProductVideoDetailDataModel>
        get() = _productVideoDetailData
    private val _productVideoDetailData = MutableLiveData<ProductVideoDetailDataModel>()

    val rateEstimateRequest: LiveData<RatesEstimateRequest>
        get() = _rateEstimateRequest
    private val _rateEstimateRequest = MutableLiveData<RatesEstimateRequest>()

    val isAddressChanged: LiveData<Boolean>
        get() = _isAddressChanged
    private val _isAddressChanged = MutableLiveData<Boolean>()

    //Give video data from pdp fragment to video detail
    fun updateVideoDetailData(currentVideoData: ProductVideoDetailDataModel) {
        _productVideoDetailData.value = currentVideoData
    }

    //Give back the data from video detail to pdp fragment
    fun updateVideoDataInPreviousFragment(video: List<ProductVideoDataModel>) {
        _productVideoData.value = video
    }

    //Give request data to shipping bottom sheet
    fun setRequestData(data: RatesEstimateRequest) {
        _rateEstimateRequest.value = data
    }

    fun setAddressChanged(shouldChanged:Boolean) {
        _isAddressChanged.value = shouldChanged
    }
}