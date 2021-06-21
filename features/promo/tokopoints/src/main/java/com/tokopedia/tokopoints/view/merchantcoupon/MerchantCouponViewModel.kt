package com.tokopedia.tokopoints.view.merchantcoupon

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.view.model.merchantcoupon.MerchantCouponResponse
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MerchantCouponViewModel @Inject constructor(private val usecase: MerchantCouponUsecase) : BaseViewModel(Dispatchers.Main) {

    val couponData = MutableLiveData<Resources<MerchantCouponData>>()

    var category: String = ""
    var firstSetup = true
    fun setCategoryRootId(id: String) {
        firstSetup = false
        category = id
        couponData.value = Loading()
    }

    fun merchantCouponData(page: Int) {
        launchCatchError(block = {
            usecase.params = MerchantCouponUsecase.createParams(page,category)
            val data = usecase.executeOnBackground().getSuccessData<MerchantCouponResponse>()
            if (data != null) {
                couponData.value = Success(MerchantCouponData(data, firstSetup))
            } else throw NullPointerException()
        }) {
            couponData.value = ErrorMessage(it.toString())
        }
    }
}


data class MerchantCouponData(val merchantCouponResponse: MerchantCouponResponse, val firstSetup: Boolean)
