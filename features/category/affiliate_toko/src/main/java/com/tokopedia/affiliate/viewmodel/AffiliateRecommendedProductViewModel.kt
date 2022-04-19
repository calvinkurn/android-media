package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateRecommendedProductData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredPromotionCardModel
import com.tokopedia.affiliate.usecase.AffiliateRecommendedProductUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateRecommendedProductViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateRecommendedProductUseCase: AffiliateRecommendedProductUseCase,
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var affiliateDataList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var pageInfo = MutableLiveData<AffiliateRecommendedProductData.RecommendedAffiliateProduct.Data.PageInfo>()
    private var errorMessage = MutableLiveData<String>()
    private val pageLimit = 20
    var isUserBlackListed : Boolean = false

    fun getAffiliateRecommendedProduct(identifier : String,page : Int) {
        shimmerVisibility.value = true
        launchCatchError(block = {
            affiliateRecommendedProductUseCase.affiliateGetRecommendedProduct(identifier,page,pageLimit).recommendedAffiliateProduct?.data?.let {
                pageInfo.value = it.pageInfo
                affiliateDataList.value = convertDataToVisitable(it)
            }
        }, onError = {
            shimmerVisibility.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }

    fun convertDataToVisitable(it: AffiliateRecommendedProductData.RecommendedAffiliateProduct.Data?): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val tempList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        it?.cards?.firstOrNull()?.items?.let { items ->
            for (product in items){
                product?.let {
                    product.isLinkGenerationAllowed = !isUserBlackListed
                    tempList.add(AffiliateStaggeredPromotionCardModel(product))
                }
            }
        }
        return tempList
    }

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getAffiliateItemCount(): LiveData<AffiliateRecommendedProductData.RecommendedAffiliateProduct.Data.PageInfo> = pageInfo
    fun getAffiliateDataItems() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliateDataList
}
