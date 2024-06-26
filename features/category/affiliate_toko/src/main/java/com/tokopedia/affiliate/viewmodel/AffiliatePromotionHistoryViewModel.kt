package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliatePerformanceData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSharedProductCardsModel
import com.tokopedia.affiliate.usecase.AffiliatePerformanceUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import java.util.ArrayList
import javax.inject.Inject

class AffiliatePromotionHistoryViewModel @Inject constructor(
    private val affiliatePerformanceUseCase: AffiliatePerformanceUseCase
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var affiliateDataList =
        MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var totalItemsCount = MutableLiveData<Int>()
    private var errorMessage = MutableLiveData<Throwable>()

    fun getAffiliatePerformance(page: Int) {
        shimmerVisibility.value = true
        launchCatchError(
            block = {
                affiliatePerformanceUseCase.affiliatePerformance(page, PAGE_LIMIT)
                    .getAffiliateItemsPerformanceList?.itemPerformanceListData?.sectionData?.let {
                        totalItemsCount.value = it.itemTotalCount.orZero()
                        convertDataToVisitable(it)?.let { visitable ->
                            affiliateDataList.value = visitable
                        }
                    }
            },
            onError = {
                shimmerVisibility.value = false
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun convertDataToVisitable(
        data: AffiliatePerformanceData.GetAffiliateItemsPerformanceList.Data.SectionData
    ): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val tempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        data.items?.let { items ->
            for (product in items) {
                product?.let {
                    tempList.add(AffiliateSharedProductCardsModel(product))
                }
            }
            return tempList
        }
        return null
    }

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getAffiliateItemCount(): LiveData<Int> = totalItemsCount
    fun getAffiliateDataItems(): LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> =
        affiliateDataList

    companion object {
        private const val PAGE_LIMIT = 10
    }
}
