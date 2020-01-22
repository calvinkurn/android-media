package com.tokopedia.tokopoints.view.couponlisting

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.PromoCouponEntity
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity
import com.tokopedia.tokopoints.view.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import rx.Subscriber
import java.lang.Error
import java.lang.NullPointerException
import java.util.HashMap
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
            }
        }) {

        }

    }

}