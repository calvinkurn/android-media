package com.tokopedia.tokopoints.view.couponlisting

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.view.model.CouponFilterBase
import com.tokopedia.tokopoints.view.util.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class StackedCouponActivtyViewModel @Inject constructor(bundle: Bundle, private val repository: StackedCouponRepository) : BaseViewModel(Dispatchers.Main) {

    private val slug: String? = bundle.getString(CommonConstant.EXTRA_SLUG)
    val couponFilterViewModel = MutableLiveData<Resources<CouponFilterBase>>()

     fun getFilter() {
        launchCatchError(block = {
            couponFilterViewModel.value = Loading()
            couponFilterViewModel.value = Success(repository.getFilter(slug ?: "").getSuccessData())
        }
        ) {
            couponFilterViewModel.value = ErrorMessage(it.toString())
        }
    }
}