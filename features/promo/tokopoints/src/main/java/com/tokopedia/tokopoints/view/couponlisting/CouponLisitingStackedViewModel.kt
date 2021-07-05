package com.tokopedia.tokopoints.view.couponlisting

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@TokoPointScope
class CouponLisitingStackedViewModel @Inject constructor(private val respositor: StackedCouponRepository) : BaseViewModel(Dispatchers.Main) {


    val startAdapter = MutableLiveData<Resources<TokoPointPromosEntity>>()
    val inStackedAdapter = MutableLiveData<TokoPointPromosEntity>()

    var category: Int? = null

    fun getCoupons(id: Int) {
        category = id
        startAdapter.value = Loading()
    }

    fun getList(pageNumber: Int) {
        launchCatchError(block = {
            category?.let {
                val catalogListingOuter = respositor.getCouponList(pageNumber, category as Int).getData<TokoPointPromosEntity>(TokoPointPromosEntity::class.java)
                if (catalogListingOuter != null) {
                    if (catalogListingOuter.coupon.coupons != null) {
                        startAdapter.value = Success(catalogListingOuter)
                    } else throw NullPointerException()
                } else throw NullPointerException()
            } ?: throw NullPointerException("category no available")
        }) {
            startAdapter.value = ErrorMessage(it.toString())
        }
    }

    fun getCouponInStack(stackId: String) {
        launchCatchError(block = {
            //handling the catalog listing and tabs
            val catalogListingOuter = respositor.getInStackedCouponList(stackId).getData<TokoPointPromosEntity>(TokoPointPromosEntity::class.java)
            if (catalogListingOuter != null) {
                if (catalogListingOuter.coupon.coupons != null) {
                    inStackedAdapter.value = catalogListingOuter
                }
            } else throw NullPointerException()
        }) {
        }

    }

}