package com.tokopedia.mvcwidget.multishopmvc.verticallist

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.mvcwidget.LiveDataResult
import com.tokopedia.mvcwidget.multishopmvc.data.MerchantCouponResponse
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MerchantCouponViewModel @Inject constructor(private val usecase: MerchantCouponUsecase) : BaseViewModel(Dispatchers.Main) {

    val couponData = MutableLiveData<LiveDataResult<MerchantCouponData>>()

    fun merchantCouponData(page: Int) {
        launchCatchError(block = {
            val data = usecase.getResponse(usecase.getQueryParams(page))
            data?.let {
                couponData.value = LiveDataResult.success(MerchantCouponData(data))
            }
        }) {
            println(it)
            couponData.value = LiveDataResult.error(it)
        }
    }
}

data class MerchantCouponData(val merchantCouponResponse: MerchantCouponResponse)
