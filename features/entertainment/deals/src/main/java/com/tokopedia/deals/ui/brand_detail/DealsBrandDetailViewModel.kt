package com.tokopedia.deals.ui.brand_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.data.entity.DealsBrandDetail
import com.tokopedia.deals.domain.GetBrandDetailsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class DealsBrandDetailViewModel @Inject constructor(
    private val getBrandDetails: GetBrandDetailsUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _brandDetail = MutableLiveData<Result<DealsBrandDetail>>()
    val brandDetail: LiveData<Result<DealsBrandDetail>>
        get() = _brandDetail

    fun getBrandDetail(coordinates: String, seoUrl: String) {
        launch {
            try {
                val data = getBrandDetails(GetBrandDetailsUseCase.Param(coordinates, seoUrl))
                _brandDetail.value = Success(data)
            } catch (e: Exception) {
                _brandDetail.value = Fail(e)
            }
        }
    }

    companion object {
        const val PARAM_BRAND_DETAIL = "params"
    }
}
