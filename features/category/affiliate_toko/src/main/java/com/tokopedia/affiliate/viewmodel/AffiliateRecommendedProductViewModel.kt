package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredPromotionCardModel
import com.tokopedia.affiliate.usecase.AffiliatePerformanceUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AffiliateRecommendedProductViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val affiliatePerformanceUseCase: AffiliatePerformanceUseCase,
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var affiliateDataList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var totalItemsCount = MutableLiveData<Int>()
    private var errorMessage = MutableLiveData<String>()
    private val pageLimit = 20

    fun getAffiliateRecommendedProduct(identifier : String,page : Int) {
        if(page == PAGE_ZERO)
            shimmerVisibility.value = true
        launchCatchError(block = {
            if(page == PAGE_ZERO)
                shimmerVisibility.value = false
            affiliatePerformanceUseCase.affiliatePerformance(page,pageLimit).getAffiliateItemsPerformanceList?.data?.sectionData?.let {
                totalItemsCount.value = it.itemTotalCount
                val tempList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
                it.items?.let { items ->
                    for (product in items) {
                        product?.let {
                            tempList.add(AffiliateStaggeredPromotionCardModel(product))
                        }
                    }
                    affiliateDataList.value = ArrayList()
                }
            }
        }, onError = {
            shimmerVisibility.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getAffiliateItemCount(): LiveData<Int> = totalItemsCount
    fun getAffiliateDataItems() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliateDataList
}
